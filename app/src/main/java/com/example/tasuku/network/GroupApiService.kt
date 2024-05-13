package com.example.tasuku.network

import com.example.tasuku.model.Group
import com.example.tasuku.model.GroupJoinResponse
import com.example.tasuku.model.GroupMember
import com.example.tasuku.model.GroupMemberResponse
import com.example.tasuku.model.GroupResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupApiService {
    @GET("group/list")
    suspend fun getGroups(): Response<List<Group>>

    @GET("workspace/group/{joinKey}")
    suspend fun getGroup(@Path("joinKey") joinKey: String): Response<GroupResponse>

    @GET("group/information/{joinKey}")
    suspend fun getGroupInformation(@Path("joinKey") joinKey: String): Response<GroupMemberResponse>

    @GET("group/members/{joinKey}")
    suspend fun getGroupMembers(@Path("joinKey") joinKey: String): Response<List<GroupMember>>

    @FormUrlEncoded
    @POST("group/create")
    suspend fun createGroup(
        @Field("name") name: String,
        @Field("description") description: String
    ): Response<Boolean>

    @FormUrlEncoded
    @POST("group/join")
    suspend fun joinGroup(
        @Field("joinKey") joinKey: String
    ): Response<GroupJoinResponse>

    @DELETE("group/leave/{id}")
    suspend fun leaveGroup(@Path("id") groupId: Int): Response<Boolean>
}