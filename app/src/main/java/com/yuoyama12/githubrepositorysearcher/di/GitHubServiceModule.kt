package com.yuoyama12.githubrepositorysearcher.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yuoyama12.githubrepositorysearcher.network.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.github.com/"

@InstallIn(SingletonComponent::class)
@Module
object GitHubServiceModule {

    @Provides
    fun provideGitHubService(): GitHubService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GitHubService::class.java)
    }
}