package com.yuoyama12.githubrepositorysearcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green400 = Color(0xFF7CCF69)
val Green700 = Color(0xFF2D8D33)
val Green900 = Color(0xFF006B21)
val LightGreen300 = Color(0xFF00FF75)

@Composable
fun urlTextColor() =
    if (isSystemInDarkTheme()) Color.DarkGray
    else Color.Gray