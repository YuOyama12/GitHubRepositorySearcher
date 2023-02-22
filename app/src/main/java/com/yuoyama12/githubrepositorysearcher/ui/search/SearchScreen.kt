package com.yuoyama12.githubrepositorysearcher.ui.search

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.squareup.picasso.Picasso
import com.yuoyama12.githubrepositorysearcher.R
import com.yuoyama12.githubrepositorysearcher.data.Repos
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme
import com.squareup.picasso.Target

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
        val repos by viewModel.repos.observeAsState(Repos())

        Column(
            modifier = Modifier
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
                    onClick = {
                        viewModel.loadRepos(query)
                        query = ""
                    },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(repos.items) { item ->
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Row {
                            Column(
                                modifier = Modifier.weight(0.75f)
                            ) {
                                HeaderAndBody(
                                    header = stringResource(R.string.repo_owner_header),
                                    body = item.owner.name
                                )

                                HeaderAndBody(
                                    header = stringResource(R.string.repo_title_header),
                                    body = item.name
                                )

                                Row {
                                    IconAndBody(
                                        icon = painterResource(R.drawable.ic_watch_count_24),
                                        body = item.watchersCount
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                                    IconAndBody(
                                        icon = painterResource(R.drawable.ic_star_count_24),
                                        body = item.stargazersCount
                                    )
                                }
                            }

                            NetworkImage(
                                url = item.owner.avatarUrl,
                                modifier = Modifier
                                    .weight(0.25f)
                                    .size(75.dp)
                            )
                        }

                        Text(
                            text = item.htmlUrl,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(Color.LightGray)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun HeaderAndBody(header: String, body: String) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp)
    ) {
        Text(
            text = header,
            modifier = Modifier.padding(end = 8.dp),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(text = body)
    }
}

@Composable
fun IconAndBody(icon: Painter, body: String) {
    Row(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null
        )

        Text(text = body)
    }
}

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier
) {

    var image by remember { mutableStateOf<Bitmap?>(null) }
    var drawable by remember { mutableStateOf<Drawable?>(null) }

    DisposableEffect(url) {
        val picasso = Picasso.get()

        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap
            }
        }

        picasso.load(url).error(R.drawable.ic_error_24).into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }

    if (image != null) {
        Image(
            bitmap = image!!.asImageBitmap(),
            modifier = modifier,
            contentDescription = null
        )
    } else if (drawable != null) {
        Image(
            painter = painterResource(R.drawable.ic_error_24),
            contentDescription = null)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    GitHubRepositorySearcherTheme {
        SearchScreen()
    }
}
