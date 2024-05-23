package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.GroupResponse
import com.example.tasuku.model.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed class GroupDetailUiState {
    object Loading : GroupDetailUiState()
    data class Success(val data: GroupResponse) : GroupDetailUiState()
    data class Error(val message: String) : GroupDetailUiState()
}

class GroupDetailViewModel(
    private val groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _groupDetailUiState =
        MutableStateFlow<GroupDetailUiState>(GroupDetailUiState.Loading)
    val groupDetailUiState: StateFlow<GroupDetailUiState> = _groupDetailUiState.asStateFlow()

    private val _groupMembersUiState =
        MutableStateFlow<GroupMembersUiState>(GroupMembersUiState.Loading)
    val groupMembersUiState: StateFlow<GroupMembersUiState> = _groupMembersUiState.asStateFlow()

    var taskList by mutableStateOf(listOf<TaskResponse>())

    val authUserId = sharedPreferences.getInt("user_id", -1)

    private val joinKey: String = checkNotNull(savedStateHandle["joinKey"])

    init {
        sharedPreferences.edit().putString("joinKey", joinKey).apply()
        getGroup(joinKey)
        getGroupMembers(joinKey)
    }

    private fun getGroup(joinKey: String) {
        viewModelScope.launch {
            _groupDetailUiState.value = GroupDetailUiState.Loading
            try {
                val response = groupRepository.getGroup(joinKey)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _groupDetailUiState.value = GroupDetailUiState.Success(data)
                        taskList = (_groupDetailUiState.value as GroupDetailUiState.Success).data.tasks
                    } else {
                        _groupDetailUiState.value = GroupDetailUiState.Error("No group was found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _groupDetailUiState.value =
                        GroupDetailUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: Exception) {
                _groupDetailUiState.value = GroupDetailUiState.Error("Error occurred: ${e.message}")
            }
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

    fun refreshGroup() {
        getGroup(joinKey)
    }

    fun leaveGroup(context: Context,groupId: Int) {
        viewModelScope.launch {
            try {
                val response = groupRepository.leaveGroup(groupId)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Left group", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("GroupDetailViewModel", "Failed to leave group")
                }
            } catch (e: Exception) {
                Log.e("GroupDetailViewModel", "Error occurred: ${e.message}")
            }
        }
    }
    fun resetTaskList(){
        taskList = (_groupDetailUiState.value as GroupDetailUiState.Success).data.tasks
    }
}