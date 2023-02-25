package com.yuoyama12.githubrepositorysearcher.network

import com.yuoyama12.githubrepositorysearcher.data.Repos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("search/repositories")
    fun fetchRepos(
        @Query("q") query: String,
        @Query("page") page: String,
        @Query("per_page") perPage: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Call<Repos>
}
