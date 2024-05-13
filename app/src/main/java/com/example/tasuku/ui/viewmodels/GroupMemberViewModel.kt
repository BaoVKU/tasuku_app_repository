package com.example.tasuku.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.Group
import com.example.tasuku.model.GroupMemberResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed class GroupInfoUiState {
    data object Loading : GroupInfoUiState()
    data class Success(val data: GroupMemberResponse) : GroupInfoUiState()
    data class Error(val message: String) : GroupInfoUiState()
}

class GroupMemberViewModel(
    private val groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _groupMemberResponseUiState =
        MutableStateFlow<GroupInfoUiState>(GroupInfoUiState.Loading)
    val groupMemberResponseUiState: StateFlow<GroupInfoUiState> =
        _groupMemberResponseUiState.asStateFlow()

    private val joinKey: String = checkNotNull(savedStateHandle["joinKey"])

    init {
        getGroupInformation(joinKey)
    }

    private fun getGroupInformation(joinKey: String) {
        viewModelScope.launch {
            _groupMemberResponseUiState.value = GroupInfoUiState.Loading
            try {
                val response = groupRepository.getGroupInformation(joinKey)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _groupMemberResponseUiState.value = GroupInfoUiState.Success(data)
                    } else {
                        _groupMemberResponseUiState.value = GroupInfoUiState.Error("No data found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _groupMemberResponseUiState.value =
                        GroupInfoUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: Exception) {
                _groupMemberResponseUiState.value =
                    GroupInfoUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
