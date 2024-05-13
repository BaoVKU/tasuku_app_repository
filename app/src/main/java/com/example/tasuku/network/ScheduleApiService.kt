package com.example.tasuku.network

import com.example.tasuku.model.Schedule
import retrofit2.Response
import retrofit2.http.GET

interface ScheduleApiService {
    @GET("calendar")
    suspend fun getEvents(): Response<List<Schedule>>
}