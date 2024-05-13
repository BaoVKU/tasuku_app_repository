package com.example.tasuku.model

data class GroupResponse(
    val group: Group,
    val tasks: List<TaskResponse>
)
