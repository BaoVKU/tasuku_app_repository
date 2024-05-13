package com.example.tasuku.model

data class TaskResponse(
    val task: Task,
    val members: List<TaskMember>,
    val attachments: List<Attachment>,
)
