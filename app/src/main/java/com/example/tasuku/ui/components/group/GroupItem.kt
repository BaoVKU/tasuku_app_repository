package com.example.tasuku.ui.components.group


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.model.Group
import com.example.tasuku.ui.navigation.GroupDetailScreenDestination
import com.example.tasuku.ui.navigation.GroupMemberScreenDestination
import com.example.tasuku.ui.navigation.NavigationDestination

@Composable
fun GroupItem(
    modifier: Modifier = Modifier,
    group: Group,
    isGroupOwner: Boolean = false,
    onNavigateWithString: (NavigationDestination, String) -> Unit,
    onLeaveGroup: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onNavigateWithString(GroupDetailScreenDestination, group.joinKey) }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = group.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(id = R.string.top_bar_group_subtitle, group.memberCount),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Column {
            IconButton(
                onClick = { isMenuExpanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "more_icon"
                )
            }
            GroupDropDownMenu(
                isMenuExpanded = isMenuExpanded,
                isGroupOwner = isGroupOwner,
                onDismissRequest = { isMenuExpanded = false },
                onNavigateWithString = { onNavigateWithString(GroupMemberScreenDestination, group.joinKey) },
                onCopyJoinKey = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("join_key", group.joinKey)
                    clipboard.setPrimaryClip(clip)
                },
                onLeaveGroup = {
                    onLeaveGroup()
                    isMenuExpanded = false
                }
            )
        }
    }
}