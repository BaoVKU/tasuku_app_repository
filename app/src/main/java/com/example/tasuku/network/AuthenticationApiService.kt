package com.example.tasuku.network

import com.example.tasuku.model.LoginResponse
import com.example.tasuku.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String,
        @Field("remember")
        remember: Boolean
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")
        name: String,
        @Field("email")
        email: String,
        @Field("password")
        password: String,
        @Field("password_confirmation")
        passwordConfirmation: String
    ): Response<User>

    @POST("logout")
    suspend fun logout(): Response<Boolean>
}