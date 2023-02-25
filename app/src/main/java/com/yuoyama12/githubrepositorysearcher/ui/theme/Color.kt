package com.yuoyama12.githubrepositorysearcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.yuoyama12.githubrepositorysearcher.R

val Green400 = Color(0xFF7CCF69)
val Green700 = Color(0xFF2D8D33)
val Green900 = Color(0xFF006B21)
val LightGreen300 = Color(0xFF00FF75)

@Composable
fun urlTextColor() =
    if (isSystemInDarkTheme()) Color.DarkGray
        else Color.Gray

@Composable
fun starIconColor() =
    if (isSystemInDarkTheme()) colorResource(R.color.star_dark_yellow)
        else colorResource(R.color.star_yellow)

@Composable
fun watcherIconColor() = MaterialTheme.colors.secondary