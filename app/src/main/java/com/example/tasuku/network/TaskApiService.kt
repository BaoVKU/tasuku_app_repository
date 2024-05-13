package com.example.tasuku.network

import com.example.tasuku.model.CommentResponse
import com.example.tasuku.model.TaskOperation
import com.example.tasuku.model.TaskResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApiService {
    @GET("task")
    suspend fun getTaskById(@Query("task_id") id: Int): Response<TaskResponse>

    @GET("task/comment")
    suspend fun getTaskComments(@Query("task_id") id: Int): Response<List<CommentResponse>>

    @Multipart
    @POST("task")
    suspend fun createTask(
        @Part("mode") mode: RequestBody,
        @Part("groupId") groupId: RequestBody,
        @Part("from") from: RequestBody,
        @Part("to") to: RequestBody,
        @Part("start") start: RequestBody,
        @Part("end") end: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("members") members: RequestBody,
        @Part files: List<MultipartBody.Part?>
    ): Response<Boolean>

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Boolean>

    @DELETE("task/attachment/{id}")
    suspend fun deleteAttachment(@Path("id") id: Int): Response<Boolean>

    @Multipart
    @POST("task/update")
    suspend fun updateTask(
        @Part("taskId") taskId: RequestBody,
        @Part("mode") mode: RequestBody,
        @Part("groupId") groupId: RequestBody,
        @Part("from") from: RequestBody,
        @Part("to") to: RequestBody,
        @Part("start") start: RequestBody,
        @Part("end") end: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("members") members: RequestBody,
        @Part files: List<MultipartBody.Part?>
    ): Response<Boolean>

    @FormUrlEncoded
    @POST("task/operate")
    suspend fun operateTask(
        @Field("task_id") taskId: Int,
        @Field("operation") operation: String
    ): Response<TaskOperation>

    @Multipart
    @POST("task/comment")
    suspend fun createComment(
        @Part("task_id") taskId: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part files: List<MultipartBody.Part?>
    ): Response<Boolean>

    @DELETE("comment/delete/{id}")
    suspend fun deleteComment(@Path("id") id: Int): Response<Boolean>

}