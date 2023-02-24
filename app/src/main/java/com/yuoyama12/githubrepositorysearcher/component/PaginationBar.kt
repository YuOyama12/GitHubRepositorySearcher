package com.yuoyama12.githubrepositorysearcher.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.githubrepositorysearcher.R
import com.yuoyama12.githubrepositorysearcher.ui.search.perPageNumberList
import com.yuoyama12.githubrepositorysearcher.ui.theme.GitHubRepositorySearcherTheme

private val subTextSize = 12.sp
private val requiredMinWidthOfText = 32.dp
@Composable
fun PaginationBar(
    modifier: Modifier = Modifier,
    currentPageNumber: Int,
    perPageNumber: Int,
    maxPageCount: Int,
    onForwardIconClicked: () -> Unit,
    onBackIconClicked: () -> Unit,
    onPageNumberSelected: (Int) -> Unit,
    onPerPageSelected: (Int) -> Unit
) {
    var pageNumberOptionExpanded by remember { mutableStateOf(false)}
    var perPageOptionExpanded by remember { mutableStateOf(false)}

    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (1 < currentPageNumber) {
                    onBackIconClicked()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }

        TextButton(
            onClick = { pageNumberOptionExpanded = !pageNumberOptionExpanded },
            modifier = Modifier.border(BorderStroke(0.6.dp, MaterialTheme.colors.primary))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText),
                    text = currentPageNumber.toString(),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = pageNumberOptionExpanded,
                onDismissRequest = { pageNumberOptionExpanded = false }
            ) {
                for (i in 1 .. maxPageCount) {
                    DropdownMenuItem(
                        onClick = {
                            if (currentPageNumber != i) {
                                onPageNumberSelected(i)
                            }
                            pageNumberOptionExpanded = false
                        },
                        content = { Text(text = i.toString()) }
                    )

                    Divider()
                }
            }
        }

        Text(
            text = stringResource(R.string.max_page_count_header_for_pagination),
            modifier = Modifier.padding(start = 4.dp),
            fontSize = subTextSize,
        )

        Text(
            text = maxPageCount.toString(),
            modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText.minus(8.dp)),
            fontSize = subTextSize,
            textAlign = TextAlign.End,
            maxLines = 1
        )

        IconButton(
            onClick = {
                if (currentPageNumber < maxPageCount) {
                    onForwardIconClicked()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }

        TextButton(
            onClick = { perPageOptionExpanded = !perPageOptionExpanded },
            modifier = Modifier.border(BorderStroke(0.6.dp, MaterialTheme.colors.primary))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText),
                    text = perPageNumber.toString(),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = perPageOptionExpanded,
                onDismissRequest = { perPageOptionExpanded = false }
            ) {
                for (i in perPageNumberList) {
                    DropdownMenuItem(
                        onClick = {
                            if (perPageNumber != i) {
                                onPerPageSelected(i)
                            }
                            perPageOptionExpanded = false
                        },
                        content = { Text(text = i.toString()) }
                    )

                    Divider()
                }
            }
        }

        Text(
            text = stringResource(R.string.per_page_header_for_pagination),
            modifier = Modifier.padding(start = 4.dp, end = 12.dp),
            fontSize = subTextSize
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaginationBarPreview() {
    GitHubRepositorySearcherTheme {
        PaginationBar(
            modifier = Modifier.wrapContentSize(),
            currentPageNumber = 1,
            perPageNumber = 30,
            maxPageCount = 100,
            onBackIconClicked = {},
            onForwardIconClicked = {},
            onPageNumberSelected = {},
            onPerPageSelected = {}
        )
    }
}
