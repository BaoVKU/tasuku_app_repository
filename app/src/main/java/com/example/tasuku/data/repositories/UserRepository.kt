package com.example.tasuku.data.repositories

import com.example.tasuku.model.User
import com.example.tasuku.network.UserApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part

interface UserRepository {
    suspend fun getUser(): Response<User>
    suspend fun updateProfile(
        name: RequestBody,
        email: RequestBody,
        phoneNumber: RequestBody,
        birthday: RequestBody,
        gender: RequestBody,
        address: RequestBody,
        description: RequestBody,
        avatar: MultipartBody.Part?
    ): Response<Boolean>
}

class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {
    override suspend fun getUser(): Response<User> = userApiService.getUser()

    override suspend fun updateProfile(
        name: RequestBody,
        email: RequestBody,
        phoneNumber: RequestBody,
        birthday: RequestBody,
        gender: RequestBody,
        address: RequestBody,
        description: RequestBody,
        avatar: MultipartBody.Part?
    ): Response<Boolean> = userApiService.updateProfile(
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        birthday = birthday,
        gender = gender,
        address = address,
        description = description,
        avatar = avatar
    )
}