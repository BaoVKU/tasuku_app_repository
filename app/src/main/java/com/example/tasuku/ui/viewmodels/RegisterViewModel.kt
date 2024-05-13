package com.example.tasuku.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.AuthenticationRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed class RegisterUiState{
    object Loading: RegisterUiState()
    data class Success(val user: User): RegisterUiState()
    data class Error(val message: String): RegisterUiState()
    object Idle: RegisterUiState()
}

data class RegisterFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = ""
)

class RegisterViewModel(private val authenticationRepository: AuthenticationRepository) :ViewModel(){
    private var _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    var registerFormState: RegisterFormState by mutableStateOf(RegisterFormState())

    fun register(name: String, email: String, password: String, passwordConfirmation: String){
        viewModelScope.launch{
            _uiState.value = RegisterUiState.Loading
            _uiState.value = try {
                val response =
                    authenticationRepository.register(name, email, password, passwordConfirmation)
                if (response.isSuccessful) {
                    val userName = response.body()?.name ?: ""
                    val userEmail = response.body()?.email ?: ""
                    val updatedAt = response.body()?.updatedAt ?: ""
                    val createdAt = response.body()?.createdAt ?: ""
                    val id = response.body()?.id ?: 0
                    RegisterUiState.Success(User(
                        name = userName,
                        email = userEmail,
                        updatedAt = updatedAt,
                        createdAt = createdAt,
                        id = id
                    ))
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    RegisterUiState.Error(errorResponse.message)
                }
            } catch (e: IOException) {
                RegisterUiState.Error("Network error")
            } catch (e: HttpException) {
                RegisterUiState.Error("Invalid credentials")
            }
        }
    }
}