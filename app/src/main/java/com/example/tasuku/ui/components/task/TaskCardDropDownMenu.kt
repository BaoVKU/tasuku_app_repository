package com.example.tasuku.ui.components.task

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.tasuku.R
import com.example.tasuku.ui.components.layout.ConfirmDialog
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.TaskUpdateScreenDestination

@Composable
fun TaskCardDropDownMenu(
    modifier: Modifier = Modifier,
    isMenuExpanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onShowTaskEdit:() -> Unit,
    onDeleteTask:()->Unit
) {
    var isOpenTaskDeleteConfirmDialog by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        DropdownMenuItem(text = {
            Text(text = "Edit")
        },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "edit_icon"
                )
            }, onClick = onShowTaskEdit)
        DropdownMenuItem(text = {
            Text(text = "Delete", color = MaterialTheme.colorScheme.error)
        },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "delete_icon",
                    tint = MaterialTheme.colorScheme.error
                )
            }, onClick = {
                isOpenTaskDeleteConfirmDialog = true
            })
    }
    if(isOpenTaskDeleteConfirmDialog){
        ConfirmDialog(
            confirmText = "Are you sure you want to delete this task?",
            onDismissRequest = { isOpenTaskDeleteConfirmDialog = false },
            onConfirm = {
                isOpenTaskDeleteConfirmDialog = false
                onDeleteTask()
            }
        )
    }
}