package com.example.tasuku.data.repositories

import com.example.tasuku.model.CommentResponse
import com.example.tasuku.model.TaskOperation
import com.example.tasuku.model.TaskResponse
import com.example.tasuku.network.TaskApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part

interface TaskRepository {
    suspend fun getTaskById(id: Int): Response<TaskResponse>
    suspend fun getTaskComments(id: Int): Response<List<CommentResponse>>
    suspend fun createTask(
        mode: RequestBody,
        groupId: RequestBody,
        from: RequestBody,
        to: RequestBody,
        start: RequestBody,
        end: RequestBody,
        title: RequestBody,
        description: RequestBody,
        members: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean>

    suspend fun deleteTask(id: Int): Response<Boolean>
    suspend fun deleteAttachment(id: Int): Response<Boolean>
    suspend fun updateTask(
        taskId: RequestBody,
        mode: RequestBody,
        groupId: RequestBody,
        from: RequestBody,
        to: RequestBody,
        start: RequestBody,
        end: RequestBody,
        title: RequestBody,
        description: RequestBody,
        members: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean>

    suspend fun operateTask(taskId: Int, operation: String): Response<TaskOperation>
    suspend fun createComment(
        taskId: RequestBody,
        comment: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean>
    suspend fun deleteComment(id: Int): Response<Boolean>
}

class TaskRepositoryImpl(private val taskApiService: TaskApiService) : TaskRepository {
    override suspend fun getTaskById(id: Int): Response<TaskResponse> =
        taskApiService.getTaskById(id)

    override suspend fun getTaskComments(id: Int): Response<List<CommentResponse>> =
        taskApiService.getTaskComments(id)

    override suspend fun createTask(
        mode: RequestBody,
        groupId: RequestBody,
        from: RequestBody,
        to: RequestBody,
        start: RequestBody,
        end: RequestBody,
        title: RequestBody,
        description: RequestBody,
        members: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean> =
        taskApiService.createTask(
            mode = mode,
            groupId = groupId,
            from = from,
            to = to,
            start = start,
            end = end,
            title = title,
            description = description,
            members = members,
            files = files
        )

    override suspend fun deleteTask(id: Int): Response<Boolean> = taskApiService.deleteTask(id)

    override suspend fun deleteAttachment(id: Int): Response<Boolean> =
        taskApiService.deleteAttachment(id)

    override suspend fun updateTask(
        taskId: RequestBody,
        mode: RequestBody,
        groupId: RequestBody,
        from: RequestBody,
        to: RequestBody,
        start: RequestBody,
        end: RequestBody,
        title: RequestBody,
        description: RequestBody,
        members: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean> =
        taskApiService.updateTask(
            taskId = taskId,
            mode = mode,
            groupId = groupId,
            from = from,
            to = to,
            start = start,
            end = end,
            title = title,
            description = description,
            members = members,
            files = files
        )

    override suspend fun operateTask(taskId: Int, operation: String): Response<TaskOperation> =
        taskApiService.operateTask(taskId, operation)

    override suspend fun createComment(
        taskId: RequestBody,
        comment: RequestBody,
        files: List<MultipartBody.Part?>
    ): Response<Boolean> = taskApiService.createComment(taskId, comment, files)

    override suspend fun deleteComment(id: Int): Response<Boolean> = taskApiService.deleteComment(id)
}