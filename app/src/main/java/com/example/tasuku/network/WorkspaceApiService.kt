package com.example.tasuku.network

import com.example.tasuku.model.TaskResponse
import retrofit2.Response
import retrofit2.http.GET

interface WorkspaceApiService {
    @GET("workspace")
    suspend fun getWorkspaceTaskList(): Response<List<TaskResponse>>
}