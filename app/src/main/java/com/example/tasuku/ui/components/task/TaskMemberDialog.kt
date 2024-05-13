package com.example.tasuku.ui.components.task


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.TasukuApp
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.model.GroupMember
import com.example.tasuku.ui.theme.TasukuTheme
import com.example.tasuku.ui.viewmodels.TaskAddAndUpdateFormState


@Composable
fun TaskMemberDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    memberList: List<GroupMember>,
    checkedMemberList: List<Pair<Int, Boolean>>,
    onCheckedChanged: (Int, Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier.fillMaxHeight(0.6f),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Choose members",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(items = memberList, key = { item -> item.id }) { member ->
                        TaskMemberItem(
                            member = member,
                            checked = checkedMemberList.find { checkedPair -> checkedPair.first == member.id }?.second
                                ?: false,
                            onCheckedChanged =
                                onCheckedChanged
                        )
                    }
                }
                Divider()
                TextButton(onClick = onDismissRequest) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@Composable
fun TaskMemberItem(
    modifier: Modifier = Modifier,
    member: GroupMember,
    checked: Boolean = false,
    onCheckedChanged: (Int,Boolean) -> Unit
) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(8.dp)) {
        Checkbox(checked = checked, onCheckedChange = {
            onCheckedChanged(member.id, it) })
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(BaseUrl.URL + member.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = "member_avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = member.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
