package com.example.tasuku.network

import com.example.tasuku.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApiService {
    @GET("user")
    suspend fun getUser(): Response<User>

    @Multipart
    @POST("profile/update")
    suspend fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address") address: RequestBody,
        @Part("description") description: RequestBody,
        @Part avatar: MultipartBody.Part?
        ): Response<Boolean>
}