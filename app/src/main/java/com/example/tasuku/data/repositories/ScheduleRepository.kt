package com.example.tasuku.data.repositories

import com.example.tasuku.model.Schedule
import com.example.tasuku.network.ScheduleApiService
import retrofit2.Response

interface ScheduleRepository {
    suspend fun getEvents(): Response<List<Schedule>>
}

class ScheduleRepositoryImpl(private val scheduleApiService: ScheduleApiService) : ScheduleRepository {
    override suspend fun getEvents(): Response<List<Schedule>> = scheduleApiService.getEvents()
}