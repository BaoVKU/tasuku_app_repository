package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("attachment_id")
    val attachmentId: Int,
    val name: String,
    val extension: String,
    val type: String,
    val url: String,
)
