package com.yuoyama12.githubrepositorysearcher.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repo(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String = "",
    @Json(name = "owner") val owner: Owner = Owner(),
    @Json(name = "stargazers_count") val stargazersCount: String = "",
    @Json(name = "watchers_count") val watchersCount: String = "",
    @Json(name = "html_url") val htmlUrl: String = ""
)
