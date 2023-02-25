package com.yuoyama12.githubrepositorysearcher.component

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.yuoyama12.githubrepositorysearcher.R
import com.yuoyama12.githubrepositorysearcher.data.Owner
import com.yuoyama12.githubrepositorysearcher.data.Repo
import com.yuoyama12.githubrepositorysearcher.data.Repos
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme
import com.yuoyama12.githubrepositorysearcher.ui.theme.starIconColor
import com.yuoyama12.githubrepositorysearcher.ui.theme.urlTextColor
import com.yuoyama12.githubrepositorysearcher.ui.theme.watcherIconColor
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.round

private val startPadding = 6.dp
@Composable
fun RepositoryList(
    modifier: Modifier = Modifier,
    repos: Repos
) {
    val uriHandler = LocalUriHandler.current

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(repos) {
        if (repos.items.isNotEmpty()) {
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(repos.items) { item ->
            Column(modifier = Modifier.padding(all = 6.dp)) {
                Card(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.LightGray))
                        .shadow(6.dp)
                        .clickable {
                            uriHandler.openUri(item.htmlUrl)
                        }
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Row {
                            Column(
                                modifier = Modifier.weight(0.75f)
                            ) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = item.owner.name,
                                    modifier = Modifier.padding(start = startPadding, bottom = 6.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(modifier = Modifier.padding(start = startPadding)) {
                                    NumericValueWithIcon(
                                        icon = painterResource(R.drawable.ic_watch_count_24),
                                        iconColor = watcherIconColor(),
                                        value = item.watchersCount
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                                    NumericValueWithIcon(
                                        icon = painterResource(R.drawable.ic_star_count_24),
                                        iconColor = starIconColor(),
                                        value = item.stargazersCount
                                    )
                                }
                            }

                            NetworkImage(
                                url = item.owner.avatarUrl,
                                modifier = Modifier
                                    .weight(0.25f)
                                    .size(105.dp)
                                    .border(BorderStroke(0.5.dp, MaterialTheme.colors.primary))
                            )
                        }

                        Text(
                            text = item.htmlUrl,
                            modifier = Modifier.padding(start = startPadding, bottom = 6.dp),
                            color = urlTextColor(),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                )
            }
        }
    }
}

@Composable
fun NumericValueWithIcon(
    icon: Painter,
    iconColor: Color? = null,
    value: String
) {
    val alignedValue =
        try {
            value.toInt().alignDigit()
        } catch (e: IllegalArgumentException) {
            value
        }

    Row(
        modifier = Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconColor == null) {
            Icon(
                painter = icon,
                contentDescription = null,
            )
        } else {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.padding(start = 4.dp))

        Text(
            text = alignedValue,
            modifier = Modifier.width(54.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun Int.alignDigit(): String {
    val digitUpperLimit = 5
    val digitCount = this.toString().length

    return when (digitCount <= digitUpperLimit) {
        true -> this.toString()
        else -> {
            val divisor = 1000.0.pow((digitCount - 1) / 3)
            val alignedNumber = round(this / divisor).toInt()
            val annotation: Char? = when(digitCount) {
                6 -> 'k'
                in 7 .. 9 -> 'm'
                else -> null
            }

            if(annotation == null) this.toString()
                else "${alignedNumber}${annotation}"
        }
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
fun RepositoryListPreview() {
    val avatarUrlForPreview = "https://avatars.githubusercontent.com/u/94959504?v=4"
    val repoListForPreview =
        listOf(
            Repo(
                id = 0,
                name = "YuOyama12",
                owner = Owner("YuOyama12", avatarUrlForPreview),
                htmlUrl = "https://github.com/YuOyama12",
                stargazersCount = "10",
                watchersCount = "30"
            ),
            Repo(
                id = 1,
                name = "hogehogehogehoge",
                owner = Owner("hogehogehogehoge", avatarUrlForPreview),
                htmlUrl = "https://github.com/hogehogehogehoge",
                stargazersCount = "100000",
                watchersCount = "51000000"
            )
        )
    val reposForPreview = Repos(repoListForPreview.size, repoListForPreview)

    GitHubRepositorySearcherTheme {
        RepositoryList(repos = reposForPreview)
    }
}