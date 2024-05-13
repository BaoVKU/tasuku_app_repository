package com.example.tasuku.ui.components.task


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasuku.calculateTaskCompletion
import com.example.tasuku.model.TaskResponse
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.TaskDetailScreenDestination

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    authUserId: Int,
    taskResponse: TaskResponse,
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onOperateTask: (Int, String) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable {
            onNavigateWithArgs(
                TaskDetailScreenDestination,
                taskResponse.task.id
            )
        }
    ) {
        Column {
            LinearProgressIndicator(
                progress = calculateTaskCompletion(taskResponse.members),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            TaskCardHeader(
                isMenuExpanded = isMenuExpanded,
                authUserId = authUserId,
                onMoreExpand = { isMenuExpanded = true },
                onDismissRequest = { isMenuExpanded = false },
                onNavigateWithArgs = onNavigateWithArgs,
                modifier = modifier.padding(8.dp),
                task = taskResponse.task,
                onDeleteTask = onDeleteTask
            )
            TaskCardContent(taskResponse = taskResponse)
            Divider(thickness = 1.dp)
            val userWhoIsMember = taskResponse.members.find { it.id == authUserId }
            TaskCardFooter(userWhoIsMember = userWhoIsMember, onOperateTask = { operation ->
                onOperateTask(taskResponse.task.id, operation)
            })
        }
    }
}