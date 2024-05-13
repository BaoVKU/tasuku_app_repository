package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class TaskMember(
    val id: Int,
    val name: String,
    @SerializedName("task_members_id")
    val taskMembersId: Int,
    val avatar: String,
    @SerializedName("is_completed")
    val isCompleted: Int?,
    @SerializedName("is_important")
    val isImportant: Int?,
)
