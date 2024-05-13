package com.example.tasuku.ui.components.task

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.model.TaskMember
import com.example.tasuku.model.TaskResponse

@Composable
fun TaskCardFooter(
    modifier: Modifier = Modifier,
    userWhoIsMember: TaskMember?,
    onOperateTask: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        var isCompleted by remember { mutableStateOf(userWhoIsMember?.isCompleted == 1) }
        var isImportant by remember { mutableStateOf(userWhoIsMember?.isImportant == 1) }

        val doneIcon = if (isCompleted) R.drawable.ic_done_filled else R.drawable.ic_done
        val importantIcon =
            if (isImportant) R.drawable.ic_important_filled else R.drawable.ic_important
        IconButton(
            onClick = {
                onOperateTask("done")
                isCompleted = !isCompleted
            },
            enabled = userWhoIsMember != null,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = doneIcon),
                contentDescription = "done_icon",
                tint = if (isCompleted) Color(
                    74,
                    222,
                    128
                ) else MaterialTheme.colorScheme.onSurface,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .padding(vertical = 8.dp)
        )
        IconButton(
            onClick = {
                onOperateTask("important")
                isImportant = !isImportant
            },
            enabled = userWhoIsMember != null,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = importantIcon),
                contentDescription = "important_icon",
                tint = if (isImportant) Color(
                    248,
                    113,
                    113
                ) else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}