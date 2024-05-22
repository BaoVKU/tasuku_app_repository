package com.example.tasuku.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.Group
import com.example.tasuku.model.GroupMemberResponse
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
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

sealed class ChannelCreateUiState {
    data object Loading : ChannelCreateUiState()
    data class Success(val channel: Channel?) : ChannelCreateUiState()
    data class Error(val message: String) : ChannelCreateUiState()
}

class GroupMemberViewModel(
    private val groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _groupMemberResponseUiState =
        MutableStateFlow<GroupInfoUiState>(GroupInfoUiState.Loading)
    val groupMemberResponseUiState: StateFlow<GroupInfoUiState> =
        _groupMemberResponseUiState.asStateFlow()

    private val _channelCreateUiState = MutableStateFlow<ChannelCreateUiState>(ChannelCreateUiState.Loading)
    val channelCreateUiState: StateFlow<ChannelCreateUiState> = _channelCreateUiState.asStateFlow()

    private val joinKey: String = checkNotNull(savedStateHandle["joinKey"])
    val authUserId = sharedPreferences.getInt("user_id", -1)

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

    fun createChannelIfExist(partnerId: Int) {
        viewModelScope.launch {
            _channelCreateUiState.value = ChannelCreateUiState.Loading
            val result = ChatClient.instance().createChannel(
                channelType = "messaging",
                channelId = "user-$authUserId-and-user-$partnerId-channel",
                memberIds = listOf("$authUserId", "$partnerId"),
                extraData = emptyMap()
            ).await()

            _channelCreateUiState.value = if (result.isSuccess) {
                ChannelCreateUiState.Success(result.getOrNull())
            } else {
                ChannelCreateUiState.Error("Error creating channel")
            }
        }
    }

    fun setChannelCreateUiStateToLoading() {
        _channelCreateUiState.value = ChannelCreateUiState.Loading
    }

}
