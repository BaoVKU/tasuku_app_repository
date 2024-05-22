package com.example.tasuku.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val user: User,
    val bearer: String,
    val jwt: String,
)
