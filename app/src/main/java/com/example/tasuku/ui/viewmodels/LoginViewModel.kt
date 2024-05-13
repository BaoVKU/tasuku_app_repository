package com.example.tasuku.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.AuthenticationRepository
import com.example.tasuku.data.repositories.UserRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed class LoginUiState {
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data object Loading : LoginUiState()
    data object Idle : LoginUiState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val remember: Boolean = false
)

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var loginFormState: LoginFormState by mutableStateOf(LoginFormState())

    fun login(email: String, password: String, remember: Boolean) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            _uiState.value = try {
                val response = authenticationRepository.login(email, password, remember)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        LoginUiState.Success(token)
                    } else {
                        LoginUiState.Error("Token is null")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    LoginUiState.Error(errorResponse.message)
                }
            } catch (e: IOException) {
                LoginUiState.Error("Network error")
            } catch (e: HttpException) {
                LoginUiState.Error("Invalid credentials")
            }
            if (_uiState.value is LoginUiState.Success) {
                sharedPreferences.edit()
                    .putString("api_token", (_uiState.value as LoginUiState.Success).token).apply()
            }
        }
    }
}

