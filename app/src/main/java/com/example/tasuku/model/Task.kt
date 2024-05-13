package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class Task (
    val id: Int,
    @SerializedName("task_creator")
    val taskCreator: String,
    @SerializedName("creator_name")
    val creatorName: String,
    @SerializedName("creator_avatar")
    val creatorAvatar: String,
    @SerializedName("group_id")
    val groupId: Int?,
    val mode: Int,
    val title: String,
    val description: String,
    val start: String,
    val end: String,
    @SerializedName("created_at")
    val createdAt: String,
)