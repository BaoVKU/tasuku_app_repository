package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class Group(
    val id: Int,
    @SerializedName("creator_id")
    val creatorId: Int,
    val name: String,
    val description: String,
    @SerializedName("join_key")
    val joinKey: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("member_count")
    val memberCount: Int,
)