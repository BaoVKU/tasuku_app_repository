package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.Group
import com.example.tasuku.model.GroupJoinResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed class GroupListUiState {
    data object Loading : GroupListUiState()
    data class Success(val data: List<Group>) : GroupListUiState()
    data class Error(val message: String) : GroupListUiState()
}

sealed class GroupJoinUiState {
    data object Loading : GroupJoinUiState()
    data class Success(val data: GroupJoinResponse) : GroupJoinUiState()
    data class Error(val message: String) : GroupJoinUiState()
}

data class GroupCreateFormState(
    val name: String = "",
    val description: String = "",
)

data class GroupJoinFormState(
    val key: String = "",
)

class GroupViewModel(
    private val groupRepository: GroupRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _groupListUiState = MutableStateFlow<GroupListUiState>(GroupListUiState.Loading)
    val groupListUiState: StateFlow<GroupListUiState> = _groupListUiState.asStateFlow()

    private val _groupJoinUiState = MutableStateFlow<GroupJoinUiState>(GroupJoinUiState.Loading)

    var groupCreateFormState: GroupCreateFormState by mutableStateOf(GroupCreateFormState())
    var groupJoinFormState: GroupJoinFormState by mutableStateOf(GroupJoinFormState())

    val authUserId = sharedPreferences.getInt("user_id", -1)

    init {
        getGroups()
    }

    private fun getGroups() {
        viewModelScope.launch {
            _groupListUiState.value = GroupListUiState.Loading
            try {
                val response = groupRepository.getGroups()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _groupListUiState.value = GroupListUiState.Success(data)
                    } else {
                        _groupListUiState.value = GroupListUiState.Error("No groups were found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _groupListUiState.value =
                        GroupListUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: Exception) {
                _groupListUiState.value = GroupListUiState.Error("Error occurred: ${e.message}")
            }
        }
    }

    fun createGroup(context: Context, name: String, description: String) {
        viewModelScope.launch {
            try {
                val response = groupRepository.createGroup(name, description)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Group created successfully", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("GroupViewModel", "Failed to create group")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Error occurred: ${e.message}")
            }
        }
    }

    fun joinGroup(context: Context, joinKey: String) {
        viewModelScope.launch {
            _groupJoinUiState.value = GroupJoinUiState.Loading
            try {
                val response = groupRepository.joinGroup(joinKey)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _groupJoinUiState.value = GroupJoinUiState.Success(data)
                        Toast.makeText(context, data.responseText, Toast.LENGTH_LONG).show()
                    } else {
                        _groupJoinUiState.value = GroupJoinUiState.Error("No group was found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _groupJoinUiState.value =
                        GroupJoinUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: Exception) {
                _groupJoinUiState.value = GroupJoinUiState.Error("Error occurred: ${e.message}")
            }
        }
    }

    fun leaveGroup(context: Context, groupId: Int) {
        viewModelScope.launch {
            try {
                val response = groupRepository.leaveGroup(groupId)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Left group", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("GroupViewModel", "Failed to leave group")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Error occurred: ${e.message}")
            }
        }
    }
}