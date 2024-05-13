package com.example.tasuku.ui.components.group

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
import com.example.tasuku.ui.navigation.GroupMemberScreenDestination
import com.example.tasuku.ui.navigation.NavigationDestination

@Composable
fun GroupDropDownMenu(
    modifier: Modifier = Modifier,
    isMenuExpanded: Boolean = false,
    isGroupOwner: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onNavigateWithString: () -> Unit,
    onCopyJoinKey: () -> Unit,
    onLeaveGroup: () -> Unit
) {
    var isOpenLeaveGroupConfirmDialog by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = isMenuExpanded, onDismissRequest = onDismissRequest, modifier = modifier
    ) {
        DropdownMenuItem(text = {
            Text(text = "Copy join key")
        },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = "copy_icon"
                )
            }, onClick = onCopyJoinKey
        )
        DropdownMenuItem(
            text = {
                Text(text = "Members")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group_members),
                    contentDescription = "group_members_icon"
                )
            },
            onClick = onNavigateWithString
        )

        if (!isGroupOwner) {
            DropdownMenuItem(text = {
                Text(text = "Leave", color = MaterialTheme.colorScheme.error)
            },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "leave_icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                }, onClick = { isOpenLeaveGroupConfirmDialog = true }
            )
        }
    }
    if (isOpenLeaveGroupConfirmDialog) {
        ConfirmDialog(
            confirmText = "Are you sure you want to leave this group?",
            onDismissRequest = { isOpenLeaveGroupConfirmDialog = false }, onConfirm = {
                isOpenLeaveGroupConfirmDialog = false
                onLeaveGroup()
            })
    }
}