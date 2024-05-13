package com.example.tasuku.data.repositories

import com.example.tasuku.model.Group
import com.example.tasuku.model.GroupJoinResponse
import com.example.tasuku.model.GroupMember
import com.example.tasuku.model.GroupMemberResponse
import com.example.tasuku.model.GroupResponse
import com.example.tasuku.network.GroupApiService
import retrofit2.Response

interface GroupRepository {
    suspend fun getGroups(): Response<List<Group>>
    suspend fun getGroup(joinKey: String): Response<GroupResponse>
    suspend fun getGroupInformation(joinKey: String): Response<GroupMemberResponse>
    suspend fun getGroupMembers(joinKey: String): Response<List<GroupMember>>
    suspend fun createGroup(name: String, description: String): Response<Boolean>
    suspend fun joinGroup(joinKey: String): Response<GroupJoinResponse>
    suspend fun leaveGroup(groupId: Int): Response<Boolean>
}

class GroupRepositoryImpl(private val groupApiService: GroupApiService) : GroupRepository {
    override suspend fun getGroups(): Response<List<Group>> = groupApiService.getGroups()
    override suspend fun getGroup(joinKey: String): Response<GroupResponse> = groupApiService.getGroup(joinKey)
    override suspend fun getGroupInformation(joinKey: String): Response<GroupMemberResponse> = groupApiService.getGroupInformation(joinKey)
    override suspend fun getGroupMembers(joinKey: String): Response<List<GroupMember>> = groupApiService.getGroupMembers(joinKey)
    override suspend fun createGroup(name: String, description: String): Response<Boolean> = groupApiService.createGroup(name, description)
    override suspend fun joinGroup(joinKey: String): Response<GroupJoinResponse> = groupApiService.joinGroup(joinKey)
    override suspend fun leaveGroup(groupId: Int): Response<Boolean> = groupApiService.leaveGroup(groupId)
}