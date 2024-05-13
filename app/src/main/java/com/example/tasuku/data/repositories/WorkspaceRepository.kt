package com.example.tasuku.data.repositories

import com.example.tasuku.model.TaskResponse
import com.example.tasuku.network.WorkspaceApiService
import retrofit2.Response

interface WorkspaceRepository {
    suspend fun getWorkspaceTaskList(): Response<List<TaskResponse>>
}

class WorkspaceRepositoryImpl(private val workspaceApiService: WorkspaceApiService) : WorkspaceRepository {
    override suspend fun getWorkspaceTaskList(): Response<List<TaskResponse>> =  workspaceApiService.getWorkspaceTaskList()
}