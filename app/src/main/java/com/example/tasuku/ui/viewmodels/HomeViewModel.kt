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
import com.example.tasuku.data.repositories.TaskRepository
import com.example.tasuku.data.repositories.UserRepository
import com.example.tasuku.data.repositories.WorkspaceRepository
import com.example.tasuku.model.TaskResponse
import com.example.tasuku.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class UserUiState {
    data object Loading : UserUiState()
    data class Success(val data: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val data: List<TaskResponse>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val workspaceRepository: WorkspaceRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    val authUserId = sharedPreferences.getInt("user_id", -1)
    val authUserAvatar = sharedPreferences.getString("user_avatar", "")

    var taskList by mutableStateOf(listOf<TaskResponse>())

    init {
        sharedPreferences.edit().putString("joinKey", "").apply()
        getTasks()
    }

    fun getTasks(){
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading
            try {
                val response = workspaceRepository.getWorkspaceTaskList()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _homeUiState.value = HomeUiState.Success(data)
                        taskList = (_homeUiState.value as HomeUiState.Success).data
                    } else {
                        _homeUiState.value = HomeUiState.Error("No task was found")
                    }
                } else {
                    _homeUiState.value = HomeUiState.Error("Failed to get data")
                }
            } catch (e: IOException) {
                HomeUiState.Error("Network error")
            } catch (e: HttpException) {
                HomeUiState.Error("Invalid response")
            }
        }
    }

    fun resetTaskList(){
        taskList = (_homeUiState.value as HomeUiState.Success).data
    }
}