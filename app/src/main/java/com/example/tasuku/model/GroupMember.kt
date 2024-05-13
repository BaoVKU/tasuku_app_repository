package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class GroupMember(
    val id: Int,
    val name: String,
    val avatar: String,
    @SerializedName("group_member_id")
    val groupMemberId: Int
)
