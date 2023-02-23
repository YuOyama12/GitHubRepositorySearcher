package com.yuoyama12.githubrepositorysearcher.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.githubrepositorysearcher.data.Repos
import com.yuoyama12.githubrepositorysearcher.network.GitHubService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val TAG = "GitHubRepositorySearcher"
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val githubService: GitHubService
): ViewModel() {

    private var _repos = MutableLiveData(Repos())
    val repos: LiveData<Repos> get() = _repos

    private var _onSearch = MutableLiveData(false)
    val onSearch: LiveData<Boolean> get() = _onSearch

    fun loadRepos(query: String) {
        _onSearch.value = true

        viewModelScope.launch {
            githubService.fetchRepos(query).enqueue(
                object : Callback<Repos> {
                    override fun onResponse(call: Call<Repos>, response: Response<Repos>) {
                    if (response.isSuccessful) {
                        response.body()?.let { repos ->
                            _repos.value = repos
                            _onSearch.value = false
                        }
                    } else {
                        val msg = "HTTP error. HTTP status code: ${response.code()}"
                        Log.e(TAG, msg)
                        _onSearch.value = false
                    }
                }
                    override fun onFailure(call: Call<Repos>, t: Throwable) {
                        Log.e(TAG, "${t.stackTrace}")
                        _onSearch.value = false
                    }
                }
            )
        }
    }
}