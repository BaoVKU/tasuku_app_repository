package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.TaskRepository
import com.example.tasuku.getPathFromUri
import com.example.tasuku.model.CommentResponse
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

sealed class CommentUiState {
    data object Loading : CommentUiState()
    data class Success(val data: List<CommentResponse>) : CommentUiState()
    data class Error(val message: String) : CommentUiState()
}

data class CommentFormState(
    val comment: String = ""
)

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _taskUiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val taskUiState: StateFlow<TaskUiState> = _taskUiState.asStateFlow()
    val authUserId = sharedPreferences.getInt("user_id", -1)

    private val _commentUiState = MutableStateFlow<CommentUiState>(CommentUiState.Loading)
    val commentUiState: StateFlow<CommentUiState> = _commentUiState.asStateFlow()

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    var commentFormState: CommentFormState by mutableStateOf(CommentFormState())

    init {
        getTaskById(taskId)
        getTaskComments(taskId)
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

    private fun getTaskComments(id: Int) {
        viewModelScope.launch {
            _commentUiState.value = CommentUiState.Loading
            try {
                val response = taskRepository.getTaskComments(id)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _commentUiState.value = CommentUiState.Success(data)
                    } else {
                        _commentUiState.value = CommentUiState.Error("No comments were found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _commentUiState.value = CommentUiState.Error(errorResponse.message)
                }
            } catch (e: Exception) {
                _commentUiState.value = CommentUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun createComment(
        context: Context,
        taskId: Int,
        comment: String,
        uris: List<Uri>
    ){
        val taskIdPart = taskId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val commentPart = comment.toRequestBody("text/plain".toMediaTypeOrNull())

        val files: List<File?> = uris.map { uri -> getPathFromUri(context, uri)?.let { File(it) } }
        val parts: List<MultipartBody.Part?> = files.map { file ->
            val part = file?.asRequestBody("image/*".toMediaTypeOrNull())
            part?.let {
                MultipartBody.Part.createFormData("files[]", file.name, it)
            }
        }

        viewModelScope.launch {
            try {
                val response = taskRepository.createComment(taskIdPart, commentPart, parts)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Comment created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("TaskDetailViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TaskDetailViewModel", "Error: ${e.message}")
            }
        }
    }

    fun deleteComment(context: Context, commentId: Int) {
        viewModelScope.launch {
            try {
                val response = taskRepository.deleteComment(commentId)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("TaskDetailViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TaskDetailViewModel", "Error: ${e.message}")
            }
        }
    }
}