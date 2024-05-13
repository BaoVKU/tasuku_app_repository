package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class BearerTokenResponse(
    @SerializedName("token")
    val token: String
)
