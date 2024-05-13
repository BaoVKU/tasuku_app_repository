package com.example.tasuku.ui.components.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.getHumanReadableDate
import com.example.tasuku.model.Task
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.TaskUpdateScreenDestination

@Composable
fun TaskCardHeader(
    modifier: Modifier = Modifier,
    authUserId: Int,
    isMenuExpanded: Boolean = false,
    onMoreExpand: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit,
    task: Task,
    onDeleteTask: (Int) -> Unit
) {
    val modeIconId = when (task.mode) {
        1 -> R.drawable.ic_group_members
        2 -> R.drawable.ic_private
        else -> R.drawable.ic_public
    }
    Row(verticalAlignment = Alignment.CenterVertically,modifier = modifier) {
        Box {
            AsyncImage(
                model =
                ImageRequest.Builder(LocalContext.current).data(BaseUrl.URL + task.creatorAvatar)
                    .crossfade(true).build(), contentDescription = "task_owner_avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape)
            )
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = task.creatorName, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = getHumanReadableDate(task.createdAt),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    painter = painterResource(id = modeIconId),
                    contentDescription = "public_icon",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (authUserId.toString() == task.taskCreator) {
            Column {
                IconButton(onClick = onMoreExpand) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "more_icon"
                    )
                }
                TaskCardDropDownMenu(
                    isMenuExpanded = isMenuExpanded,
                    onDismissRequest = onDismissRequest,
                    onShowTaskEdit = { onNavigateWithArgs(TaskUpdateScreenDestination,task.id) },
                    onDeleteTask = { onDeleteTask(task.id) }
                )
            }
        }
    }

}