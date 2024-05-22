package com.example.tasuku.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasuku.data.repositories.AuthenticationRepository
import com.example.tasuku.data.repositories.UserRepository
import com.example.tasuku.dateToDdMmYyyy
import com.example.tasuku.getPathFromUri
import com.example.tasuku.model.ErrorResponse
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

data class UserProfileFormState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val birthday: String = "",
    val gender: Int = 0,
    val address: String = "",
    val description: String = ""
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    private var _userUiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    var userProfileFormState: UserProfileFormState by mutableStateOf(UserProfileFormState())

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _userUiState.value = UserUiState.Loading
            try {
                val response = userRepository.getUser()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _userUiState.value = UserUiState.Success(data)
                        userProfileFormState = userProfileFormState.copy(
                            name = data.name,
                            email = data.email,
                            phoneNumber = data.phoneNumber ?: "",
                            birthday = dateToDdMmYyyy(data.birthday ?: ""),
                            gender = data.gender ?: 0,
                            address = data.address ?: "",
                            description = data.description ?: ""
                        )
                    } else {
                        _userUiState.value = UserUiState.Error("No user was found")
                    }
                } else {
                    // Get the raw JSON error response
                    val errorJsonString = response.errorBody()?.string()
                    // Parse the JSON error response to get the error message
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorJsonString ?: "")
                    _userUiState.value =
                        UserUiState.Error("Error: ${errorResponse.message}")
                }
            } catch (e: IOException) {
                UserUiState.Error("Network error")
            } catch (e: HttpException) {
                UserUiState.Error("Invalid response")
            }
        }
    }

    fun updateProfile(
        context: Context,
        name: String,
        email: String,
        phoneNumber: String,
        birthday: String,
        gender: Int,
        address: String,
        description: String,
        uri: Uri
    ) {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumberPart = phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val birthdayPart = birthday.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderPart = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val addressPart = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val file: File? = getPathFromUri(context, uri)?.let { File(it) }
        val avatarPart: MultipartBody.Part? = file?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                file.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        viewModelScope.launch {
            try {
                val response = userRepository.updateProfile(
                    name = namePart,
                    email = emailPart,
                    phoneNumber = phoneNumberPart,
                    birthday = birthdayPart,
                    gender = genderPart,
                    address = addressPart,
                    description = descriptionPart,
                    avatar = avatarPart
                )
                if (response.isSuccessful) {
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ProfileViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("ProfileViewModel", "Network error", e)
            } catch (e: HttpException) {
                Log.e("ProfileViewModel", "Invalid response", e)
            }
        }
    }

    fun logout(context: Context){
        viewModelScope.launch {
            try {
                ChatClient.instance().disconnect(flushPersistence = true).await()
                val response = authenticationRepository.logout()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ProfileViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("ProfileViewModel", "Network error", e)
            } catch (e: HttpException) {
                Log.e("ProfileViewModel", "Invalid response", e)
            }
        }
    }
}