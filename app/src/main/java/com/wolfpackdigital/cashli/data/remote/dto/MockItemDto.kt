package com.wolfpackdigital.cashli.data.remote.dto

import com.google.gson.annotations.SerializedName

class MockItemDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("priority")
    val priority: String
)
