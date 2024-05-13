package com.example.tasuku.model

data class GroupMemberResponse(
    val group: Group,
    val members: List<GroupMember>
)
