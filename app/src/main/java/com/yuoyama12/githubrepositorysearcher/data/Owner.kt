package com.yuoyama12.githubrepositorysearcher.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Owner (
    @Json(name = "login")
    val owner: String = "",
    @Json(name = "avatar_url")
    val avatarUrl: String = "",
)