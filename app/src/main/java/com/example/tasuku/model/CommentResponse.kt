package com.example.tasuku.model

data class CommentResponse(
    val comment: Comment,
    val attachments: List<Attachment>
)