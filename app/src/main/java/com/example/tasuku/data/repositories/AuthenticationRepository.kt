package com.example.tasuku.data.repositories

import com.example.tasuku.model.LoginResponse
import com.example.tasuku.model.User
import com.example.tasuku.network.AuthenticationApiService
import retrofit2.Response

interface AuthenticationRepository {
    suspend fun login(
        email: String,
        password: String,
        remember: Boolean
    ): Response<LoginResponse>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Response<User>

    suspend fun logout(): Response<Boolean>
}

class AuthenticationRepositoryImpl(private val authenticationApiService: AuthenticationApiService) :
    AuthenticationRepository {
    override suspend fun login(
        email: String,
        password: String,
        remember: Boolean
    ): Response<LoginResponse> = authenticationApiService.login(email, password, remember)

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Response<User> = authenticationApiService.register(name, email, password, passwordConfirmation)

    override suspend fun logout(): Response<Boolean> = authenticationApiService.logout()
    }