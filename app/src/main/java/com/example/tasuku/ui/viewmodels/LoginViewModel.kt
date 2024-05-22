package com.example.tasuku.ui.viewmodels

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.data.repositories.AuthenticationRepository
import com.example.tasuku.data.repositories.UserRepository
import com.example.tasuku.model.ErrorResponse
import com.example.tasuku.model.LoginResponse
import com.example.tasuku.model.User
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed class LoginUiState {
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data object Loading : LoginUiState()
    data object Idle : LoginUiState()
}

data class LoginFormState(
    val email: String = "thisisbaomail@gmail.com",
    val password: String = "MZBGu!S!4DjYq8S",
    val remember: Boolean = false
)

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var loginFormState: LoginFormState by mutableStateOf(LoginFormState())

    @SuppressLint("SuspiciousIndentation")
    fun login(email: String, password: String, remember: Boolean) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = authenticationRepository.login(email, password, remember)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _uiState.value = LoginUiState.Success(data)

                        sharedPreferences.edit()
                            .putInt("user_id", data.user.id).apply()
                        sharedPreferences.edit()
                            .putString("user_email", data.user.email).apply()
                        sharedPreferences.edit()
                            .putString("user_name", data.user.name).apply()
                        sharedPreferences.edit()
                            .putString("user_avatar", data.user.avatar).apply()

                        sharedPreferences.edit()
                            .putString("api_token", data.bearer).apply()
                        sharedPreferences.edit()
                            .putString("jwt_token", data.jwt).apply()

                     val user  = io.getstream.chat.android.models.User(
                         id = data.user.id.toString(),
                         name = data.user.name,
                         image = BaseUrl.URL + data.user.avatar,
                            extraData = mutableMapOf("email" to data.user.email)
                     )
                        Log.e("ChatClient", "$user")
                        Log.e("ChatClient", data.jwt)
                        ChatClient.instance().connectUser(user, data.jwt).enqueue { result ->
                            if (result.isSuccess) {
                                Log.e("ChatClient", "$result")
                            } else {
                                Log.e("ChatClient", "Failed to connect to chat")
                            }
                        }
                    } else {
                        _uiState.value = LoginUiState.Error("Token is null")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _uiState.value = LoginUiState.Error(errorResponse.message)
                }
            } catch (e: IOException) {
                _uiState.value = LoginUiState.Error("Network error")
            } catch (e: HttpException) {
                _uiState.value = LoginUiState.Error("Invalid credentials")
            }
        }
    }
}

