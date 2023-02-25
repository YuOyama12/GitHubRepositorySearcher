package com.yuoyama12.githubrepositorysearcher.ui.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuoyama12.githubrepositorysearcher.R
import com.yuoyama12.githubrepositorysearcher.component.NoDataImage
import com.yuoyama12.githubrepositorysearcher.component.OnSearchIndicator
import com.yuoyama12.githubrepositorysearcher.component.PaginationBar
import com.yuoyama12.githubrepositorysearcher.component.RepositoryList
import com.yuoyama12.githubrepositorysearcher.data.Repos
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme

private const val DEFAULT_PAGE_NUMBER = 1
val perPageNumberList = listOf(10, 20, 50)

@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { padding ->
        val context = LocalContext.current

        var query by rememberSaveable { mutableStateOf("") }
        var currentPageNumber by rememberSaveable { mutableStateOf(DEFAULT_PAGE_NUMBER) }
        var perPageNumber by rememberSaveable { mutableStateOf(perPageNumberList[0]) }

        val displayedMaxPageCount by viewModel.displayedMaxPageCount.observeAsState(1)
        val totalCount by viewModel.totalCount.observeAsState(1)

        val repos by viewModel.repos.observeAsState(Repos())
        val onSearch by viewModel.onSearch.observeAsState(false)

        Box(modifier = Modifier.padding(padding)){
            Column(modifier = Modifier.fillMaxWidth()) {
                Row {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .weight(0.8f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (query.trim().isNotEmpty()) {
                                    currentPageNumber = DEFAULT_PAGE_NUMBER
                                    viewModel.loadReposWithNewQuery(query, currentPageNumber, perPageNumber)
                                }
                            }
                        )
                    )

                    Button(
                        onClick = {
                            if (query.trim().isNotEmpty()) {
                                currentPageNumber = DEFAULT_PAGE_NUMBER
                                viewModel.loadReposWithNewQuery(query, currentPageNumber, perPageNumber)
                            }
                        },
                        modifier = Modifier
                            .align(CenterVertically)
                            .weight(0.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.3.dp)
                        .background(Color.LightGray)
                        .shadow(1.dp)
                )

                if (repos.items.isEmpty()) {
                    NoDataImage(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally),
                        textBelowImage = stringResource(R.string.no_data_image_description)
                    )
                } else {
                    RepositoryList(
                        modifier = Modifier.weight(1f),
                        repos = repos
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.3.dp)
                        .background(Color.LightGray)
                        .shadow(1.dp)
                )

                PaginationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    currentPageNumber = currentPageNumber,
                    perPageNumber = perPageNumber,
                    maxPageCount = displayedMaxPageCount,
                    onForwardIconClicked = {
                        currentPageNumber++
                        viewModel.loadReposWithCurrentQuery(
                            currentPageNumber,
                            perPageNumber,
                            isPerPageNumChanged = false
                        )
                    },
                    onBackIconClicked = {
                        currentPageNumber--
                        viewModel.loadReposWithCurrentQuery(
                            currentPageNumber,
                            perPageNumber,
                            isPerPageNumChanged = false
                        )
                    },
                    onPageNumberSelected = { num ->
                        currentPageNumber = num
                        viewModel.loadReposWithCurrentQuery(
                            currentPageNumber,
                            perPageNumber,
                            isPerPageNumChanged = false
                        )
                    },
                    onPerPageSelected = { num ->
                        perPageNumber = num
                        currentPageNumber = DEFAULT_PAGE_NUMBER
                        viewModel.loadReposWithCurrentQuery(
                            currentPageNumber,
                            perPageNumber,
                            isPerPageNumChanged = true
                        )
                    }
                )
            }
        }

        if (onSearch) {
            OnSearchIndicator(modifier = Modifier.fillMaxSize())
        }

        if (totalCount > MAX_LOADABLE_DATA_AT_ONCE) {
            Toast.makeText(
                context,
                stringResource(R.string.too_much_results_message),
                Toast.LENGTH_LONG)
                .show()

            viewModel.resetActualMaxPageCount()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    GitHubRepositorySearcherTheme {
        SearchScreen()
    }
}
