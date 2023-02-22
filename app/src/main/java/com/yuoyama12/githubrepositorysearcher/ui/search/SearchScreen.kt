package com.yuoyama12.githubrepositorysearcher.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuoyama12.githubrepositorysearcher.R
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme

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
        var query by remember { mutableStateOf("") }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
        ) {
            Row {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.padding(end = 2.dp)
                )

                Button(
                    onClick = { viewModel.loadRepos(query) },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }

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
