package com.example.tasuku.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("birthday")
    val birthday: String? = null,
    @SerializedName("gender")
    val gender: Int? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null
)