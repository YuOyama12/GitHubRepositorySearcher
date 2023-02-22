package com.yuoyama12.githubrepositorysearcher.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repos(
    @Json(name = "total_count") val totalCount: Int = 0,
    @Json(name = "items") val items: List<Repo> = emptyList(),
)
