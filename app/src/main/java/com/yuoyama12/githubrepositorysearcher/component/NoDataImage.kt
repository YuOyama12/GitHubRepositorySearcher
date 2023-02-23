package com.yuoyama12.githubrepositorysearcher.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuoyama12.githubrepositorysearcher.R

@Composable
fun NoDataImage(
    modifier: Modifier = Modifier,
    textBelowImage: String = ""
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 4.dp),
                painter = painterResource(R.drawable.ic_search_24),
                colorFilter = ColorFilter.tint(Color.Gray),
                contentDescription = null
            )

            Text(
                text = textBelowImage,
                color = Color.Gray
            )
        }
    }
}