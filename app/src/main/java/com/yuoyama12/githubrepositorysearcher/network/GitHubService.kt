package com.yuoyama12.githubrepositorysearcher.network

import com.yuoyama12.githubrepositorysearcher.data.Repos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("search/repositories")
    fun fetchRepos(
        @Query("q") query: String,
    ): Call<Repos>
}
