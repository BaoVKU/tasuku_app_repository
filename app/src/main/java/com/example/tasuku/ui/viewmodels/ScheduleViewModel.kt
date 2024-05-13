package com.example.tasuku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.ScheduleRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed class ScheduleUiState {
    data object Loading: ScheduleUiState()
    data class Success(val data: List<Schedule>): ScheduleUiState()
    data class Error(val message: String): ScheduleUiState()
}

class ScheduleViewModel(private val scheduleRepository: ScheduleRepository): ViewModel(){
    private val _scheduleUiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val scheduleUiState:StateFlow<ScheduleUiState> = _scheduleUiState.asStateFlow()

    init {
        getEvents()
    }

    private fun getEvents(){
        viewModelScope.launch {
            _scheduleUiState.value = ScheduleUiState.Loading
            try {
                val response = scheduleRepository.getEvents()
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        _scheduleUiState.value = ScheduleUiState.Success(data)
                    } else {
                        _scheduleUiState.value = ScheduleUiState.Error("No data found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _scheduleUiState.value =
                        ScheduleUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: Exception){
                _scheduleUiState.value = ScheduleUiState.Error("Error: ${e.message}")
            }
        }
    }
}