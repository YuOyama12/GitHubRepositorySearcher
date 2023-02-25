package com.yuoyama12.githubrepositorysearcher.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuoyama12.githubrepositorysearcher.R

@Composable
fun SortingMenu(
    expand: Boolean,
    currentSortType: SortType,
    menuIcon: ImageVector = Icons.Default.List,
    onIconClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClicked: (SortType) -> Unit,
) {
    IconButton(
        onClick = { onIconClicked() }
    ) {
        Icon(
            imageVector = menuIcon,
            contentDescription = null
        )
    }

    DropdownMenu(
        expanded = expand,
        onDismissRequest = { onDismissRequest() }
    ) {
        for (sortType in SortType.values()) {
            DropdownMenuItem(
                onClick = {
                    onItemClicked(sortType)
                    onDismissRequest()
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sortType.getDisplayedName(),
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(end = 4.dp)
                    )

                    if (sortType == currentSortType) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.weight(0.2f)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.weight(0.2f).alpha(0f)
                        )
                    }
                }
            }
        }
    }
}

enum class SortType(val sort: String, val order: String) {
    BestMatch("", ""),
    MostStars("stars", "desc"),
    FewestStars("stars", "asc"),
    RecentlyUpdated("updated", "desc"),
    LeastRecentlyUpdated("updated", "asc")
}

@Composable fun SortType.getDisplayedName(): String {
    return when(this) {
        SortType.BestMatch -> stringResource(R.string.repository_sort_type_best_match)
        SortType.MostStars -> stringResource(R.string.repository_sort_type_most_stars)
        SortType.FewestStars -> stringResource(R.string.repository_sort_type_fewest_stars)
        SortType.RecentlyUpdated -> stringResource(R.string.repository_sort_type_recently_updated)
        SortType.LeastRecentlyUpdated -> stringResource(R.string.repository_sort_type_least_recently_updated)
    }
}
