package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.data.repositories.TaskRepository
import com.example.tasuku.getPathFromUri
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.GroupMember
import com.example.tasuku.model.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

sealed class GroupMembersUiState {
    data object Loading : GroupMembersUiState()
    data class Success(val data: List<GroupMember>) : GroupMembersUiState()
    data class Error(val message: String) : GroupMembersUiState()
}

sealed class AddTaskUiState {
    data object Loading : AddTaskUiState()
    data object Success : AddTaskUiState()
    data class Error(val message: String) : AddTaskUiState()
}

sealed class UpdateTaskUiState {
    data object Loading : UpdateTaskUiState()
    data object Success : UpdateTaskUiState()
    data class Error(val message: String) : UpdateTaskUiState()
}

data class TaskAddAndUpdateFormState(
    val mode: Int = 2,
    val checkedMemberList: List<Pair<Int, Boolean>> = mutableListOf(),
    val from: String = "dd/mm/yyyy",
    val to: String = "dd/mm/yyyy",
    val start: String = "hh:mm",
    val end: String = "hh:mm",
    val title: String = "",
    val description: String = "",
)

class TaskAddAndUpdateViewModel(
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository,
    sharedPreferences: SharedPreferences,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val taskId: Int ?= savedStateHandle["taskId"]

    private val _groupMembersUiState =
        MutableStateFlow<GroupMembersUiState>(GroupMembersUiState.Loading)
    val groupMembersUiState: StateFlow<GroupMembersUiState> = _groupMembersUiState.asStateFlow()

    private val _addTaskUiState = MutableStateFlow<AddTaskUiState>(AddTaskUiState.Loading)
    val addTaskUiState: StateFlow<AddTaskUiState> = _addTaskUiState.asStateFlow()

    private val _updateTaskUiState = MutableStateFlow<UpdateTaskUiState>(UpdateTaskUiState.Loading)
    val updateTaskUiState: StateFlow<UpdateTaskUiState> = _updateTaskUiState.asStateFlow()

    private val _taskUiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val taskUiState: StateFlow<TaskUiState> = _taskUiState.asStateFlow()

    var taskAddAndUpdateFormState: TaskAddAndUpdateFormState by mutableStateOf(
        TaskAddAndUpdateFormState()
    )

    private val joinKey = sharedPreferences.getString("joinKey", "")

    init {
        if (joinKey!!.isNotEmpty()) {
            getGroupMembers(joinKey)
        }
        if(taskId != null){
            getTaskById(taskId)
        }
    }

    private fun getGroupMembers(joinKey: String) {
        viewModelScope.launch {
            _groupMembersUiState.value = GroupMembersUiState.Loading
            try {
                val response = groupRepository.getGroupMembers(joinKey)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _groupMembersUiState.value = GroupMembersUiState.Success(data)
                        val groupMembers =
                            (_groupMembersUiState.value as GroupMembersUiState.Success).data
                        val checkedMemberList: MutableList<Pair<Int, Boolean>> = mutableListOf()
                        groupMembers.forEach { member ->
                            checkedMemberList.add(Pair(member.id, false))
                        }
                        taskAddAndUpdateFormState =
                            taskAddAndUpdateFormState.copy(checkedMemberList = checkedMemberList)

                    } else {
                        _groupMembersUiState.value =
                            GroupMembersUiState.Error("No group members were found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _groupMembersUiState.value = GroupMembersUiState.Error(errorResponse.message)
                }
            } catch (e: Exception) {
                _groupMembersUiState.value = GroupMembersUiState.Error("Network error")
            }
        }
    }

    fun updateCheckedMemberList(memberId: Int, isChecked: Boolean) {
        val checkedMemberList = taskAddAndUpdateFormState.checkedMemberList.toMutableList()
        val index = checkedMemberList.indexOfFirst { it.first == memberId }
        checkedMemberList[index] = Pair(memberId, isChecked)
        taskAddAndUpdateFormState =
            taskAddAndUpdateFormState.copy(checkedMemberList = checkedMemberList)
    }


    fun createTask(
        context: Context,
        mode: Int,
        from: String,
        to: String,
        start: String,
        end: String,
        title: String,
        description: String,
        checkedMemberList: List<Pair<Int, Boolean>>,
        uris: List<Uri>
    ) {
        val modePart = mode.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val groupIdPart =
            joinKey?.toRequestBody("text/plain".toMediaTypeOrNull()) ?: "".toRequestBody(
                "text/plain".toMediaTypeOrNull()
            )
        val fromPart = from.toRequestBody("text/plain".toMediaTypeOrNull())
        val toPart = to.toRequestBody("text/plain".toMediaTypeOrNull())
        val startPart = start.toRequestBody("text/plain".toMediaTypeOrNull())
        val endPart = end.toRequestBody("text/plain".toMediaTypeOrNull())
        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val membersString =
            checkedMemberList.filter { it.second }.joinToString(",") { it.first.toString() }
        val membersPart = membersString.toRequestBody("text/plain".toMediaTypeOrNull())

        val files: List<File?> = uris.map { uri -> getPathFromUri(context, uri)?.let { File(it) } }
        val parts: List<MultipartBody.Part?> = files.map { file ->
            val part = file?.asRequestBody("image/*".toMediaTypeOrNull())
            part?.let {
                MultipartBody.Part.createFormData("files[]", file.name, it)
            }
        }


        viewModelScope.launch {
            _addTaskUiState.value = AddTaskUiState.Loading
            try {

                val response = taskRepository.createTask(
                    mode = modePart,
                    groupId = groupIdPart,
                    from = fromPart,
                    to = toPart,
                    start = startPart,
                    end = endPart,
                    title = titlePart,
                    description = descriptionPart,
                    members = membersPart,
                    files = parts
                )
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _addTaskUiState.value = AddTaskUiState.Success
                        Toast.makeText(context, "Task created", Toast.LENGTH_SHORT).show()
                    } else {
                        _addTaskUiState.value = AddTaskUiState.Error("No task was created")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _addTaskUiState.value = AddTaskUiState.Error(errorResponse.message)
                }
            } catch (e: Exception) {
                _addTaskUiState.value = AddTaskUiState.Error("Network error")
            }
        }
    }

    private fun getTaskById(id: Int) {
        viewModelScope.launch {
            _taskUiState.value = TaskUiState.Loading
            try {
                val response = taskRepository.getTaskById(id)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _taskUiState.value = TaskUiState.Success(data)
                    } else {
                        _taskUiState.value = TaskUiState.Error("No task was found")
                    }
                } else {
                    _taskUiState.value = TaskUiState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _taskUiState.value = TaskUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun setTaskUiStateToLoading(){
        _taskUiState.value = TaskUiState.Loading
    }

    fun deleteAttachment(id: Int){
        viewModelScope.launch {
            try {
                val response = taskRepository.deleteAttachment(id)
                if (response.isSuccessful) {
                    Log.d("TaskAddAndUpdateViewModel", "Attachment deleted")
                } else {
                    Log.e("TaskAddAndUpdateViewModel", "Failed to delete attachment")
                }
            } catch (e: Exception) {
                Log.e("TaskAddAndUpdateViewModel", "Network error")
            }
        }
    }

    fun updateTask(
        context: Context,
        taskId: Int,
        mode: Int,
        from: String,
        to: String,
        start: String,
        end: String,
        title: String,
        description: String,
        checkedMemberList: List<Pair<Int, Boolean>>,
        uris: List<Uri>
    ) {
        val taskIdPart = taskId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val modePart = mode.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val groupIdPart =
            joinKey?.toRequestBody("text/plain".toMediaTypeOrNull()) ?: "".toRequestBody(
                "text/plain".toMediaTypeOrNull()
            )
        val fromPart = from.toRequestBody("text/plain".toMediaTypeOrNull())
        val toPart = to.toRequestBody("text/plain".toMediaTypeOrNull())
        val startPart = start.toRequestBody("text/plain".toMediaTypeOrNull())
        val endPart = end.toRequestBody("text/plain".toMediaTypeOrNull())
        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val membersString =
            checkedMemberList.filter { it.second }.joinToString(",") { it.first.toString() }
        val membersPart = membersString.toRequestBody("text/plain".toMediaTypeOrNull())

        val files: List<File?> = uris.map { uri -> getPathFromUri(context, uri)?.let { File(it) } }
        val parts: List<MultipartBody.Part?> = files.map { file ->
            val part = file?.asRequestBody("image/*".toMediaTypeOrNull())
            part?.let {
                MultipartBody.Part.createFormData("files[]", file.name, it)
            }
        }


        viewModelScope.launch {
            _updateTaskUiState.value = UpdateTaskUiState.Loading
            try {
                val response = taskRepository.updateTask(
                    taskId = taskIdPart,
                    mode = modePart,
                    groupId = groupIdPart,
                    from = fromPart,
                    to = toPart,
                    start = startPart,
                    end = endPart,
                    title = titlePart,
                    description = descriptionPart,
                    members = membersPart,
                    files = parts
                )
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _updateTaskUiState.value = UpdateTaskUiState.Success
                        Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
                    } else {
                        _updateTaskUiState.value = UpdateTaskUiState.Error("No task was updated")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _updateTaskUiState.value = UpdateTaskUiState.Error(errorResponse.message)
                }
            } catch (e: Exception) {
                _updateTaskUiState.value = UpdateTaskUiState.Error("Network error")
            }
        }
    }
}