package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.TaskRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.TaskOperation
import com.example.tasuku.model.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed class TaskUiState {
    data object Loading : TaskUiState()
    data class Success(val data: TaskResponse) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

sealed class TaskOperationUiState {
    data object Loading : TaskOperationUiState()
    data class Success(val data: TaskOperation) : TaskOperationUiState()
    data class Error(val message: String) : TaskOperationUiState()
}

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _taskOperationUiState = MutableStateFlow<TaskOperationUiState>(TaskOperationUiState.Loading)
    val taskOperationUiState: StateFlow<TaskOperationUiState> = _taskOperationUiState.asStateFlow()

    fun operateTask(taskId: Int, operation: String) {
        viewModelScope.launch {
            _taskOperationUiState.value = TaskOperationUiState.Loading
            try {
                val response = taskRepository.operateTask(taskId, operation)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _taskOperationUiState.value = TaskOperationUiState.Success(data)
                    } else {
                        _taskOperationUiState.value = TaskOperationUiState.Error("Failed to operate task")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    TaskOperationUiState.Error(errorResponse.message)
                }
            } catch (e: IOException) {
                _taskOperationUiState.value = TaskOperationUiState.Error("Network error")
            } catch (e: HttpException) {
                _taskOperationUiState.value = TaskOperationUiState.Error("Invalid response")
            }
        }
    }

    fun deleteTask(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                val response = taskRepository.deleteTask(id)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                } else {
                   Log.e("TaskViewModel", "Failed to delete task")
                }
            } catch (e: IOException) {
                Log.e("TaskViewModel", "Network error")
            } catch (e: HttpException) {
                Log.e("TaskViewModel", "Invalid response")
            }
        }
    }
}