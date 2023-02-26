package com.yuoyama12.githubrepositorysearcher.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.githubrepositorysearcher.component.SortType
import com.yuoyama12.githubrepositorysearcher.data.Repos
import com.yuoyama12.githubrepositorysearcher.network.GitHubService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val TAG = "GitHubRepositorySearcher"
const val MAX_LOADABLE_DATA_AT_ONCE = 1000

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val githubService: GitHubService
): ViewModel() {

    private val _repos = MutableLiveData(Repos())
    val repos: LiveData<Repos> get() = _repos

    private val currentQuery = MutableLiveData("")

    private val _displayedMaxPageCount = MutableLiveData(1)
    val displayedMaxPageCount: LiveData<Int> get() = _displayedMaxPageCount

    private val _totalCount = MutableLiveData(1)
    val totalCount: LiveData<Int> get() = _totalCount

    private val _currentSortType = MutableLiveData(SortType.BestMatch)
    val currentSortType: LiveData<SortType> get() = _currentSortType

    private val _onSearch = MutableLiveData(false)
    val onSearch: LiveData<Boolean> get() = _onSearch


    fun loadReposWithNewQuery(query: String, pageNumber: Int, perPageNumber: Int) {
        currentQuery.value = query
        loadRepos(query, pageNumber, perPageNumber, isPerPageNumChanged = false)
    }

    fun loadReposWithCurrentQuery(pageNumber: Int, perPageNumber: Int, isPerPageNumChanged: Boolean) {
        loadRepos(null, pageNumber, perPageNumber,isPerPageNumChanged)
    }

    private fun loadRepos(
        query: String?,
        pageNumber: Int,
        perPageNumber: Int,
        isPerPageNumChanged: Boolean
    ) {
        val _query: String = query ?: currentQuery.value ?: ""
        if (_query.trim().isEmpty()) return

        _onSearch.value = true

        viewModelScope.launch {
            githubService
                .fetchRepos(
                    _query,
                    pageNumber.toString(),
                    perPageNumber.toString(),
                    currentSortType.value?.sort ?: "",
                    currentSortType.value?.order ?: ""
                ).enqueue(
                    object : Callback<Repos> {
                        override fun onResponse(call: Call<Repos>, response: Response<Repos>) {
                            if (response.isSuccessful) {
                                response.body()?.let { result ->
                                    _repos.value = result
                                    _onSearch.value = false

                                    if (query != null || isPerPageNumChanged) {
                                        _displayedMaxPageCount.value =
                                            getMaxPageCount(result.totalCount, perPageNumber)
                                    }

                                    if (query != null)
                                        _totalCount.value = result.totalCount

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

    private fun getMaxPageCount(totalCount: Int, perPageNumber: Int): Int {
        return if (totalCount == 0) {
             1
        } else {
            val _totalCount =
                if (totalCount > MAX_LOADABLE_DATA_AT_ONCE) MAX_LOADABLE_DATA_AT_ONCE
                    else totalCount

            val maxPageCount =
                when (_totalCount%perPageNumber) {
                    0 -> _totalCount/perPageNumber
                    else -> _totalCount/perPageNumber + 1
                }

            maxPageCount
        }
    }

    fun resetTotalCount() {
        _totalCount.value = 1
    }

    fun resetCurrentQuery() {
        currentQuery.value = ""
    }

    fun setSortType(sortType: SortType) {
        _currentSortType.value = sortType
    }

}