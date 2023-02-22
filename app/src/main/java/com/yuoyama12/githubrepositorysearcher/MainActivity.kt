package com.yuoyama12.githubrepositorysearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.yuoyama12.githubrepositorysearcher.ui.search.SearchScreen
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubRepositorySearcherTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    SearchScreen()
                }
            }
        }
    }
}

