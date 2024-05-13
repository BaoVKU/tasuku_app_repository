package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val avatar: String,
    val comment: String,
    @SerializedName("created_at")
    val createdAt: String,
)