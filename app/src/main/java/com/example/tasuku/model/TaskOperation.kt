package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class TaskOperation(
    val id: Int,
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("is_completed")
    val isCompleted: Int?,
    @SerializedName("is_important")
    val isImportant: Int?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
