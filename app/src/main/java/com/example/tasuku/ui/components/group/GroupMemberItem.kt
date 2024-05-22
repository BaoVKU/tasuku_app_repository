package com.example.tasuku.ui.components.group

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.model.GroupMember

@Composable
fun GroupMemberItem(
    modifier: Modifier = Modifier,
    context: Context,
    authUserId: Int,
    member: GroupMember,
    onMessageClick: () -> Unit
) {
    Row(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(BaseUrl.URL + member.avatar)
                    .crossfade(true).build(),
                contentDescription = "group_member_avatar", contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


        }
        Text(
            text = member.name + if (member.id == authUserId) " (You)" else "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        )
        if(member.id != authUserId) {
            IconButton(onClick = onMessageClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = "message_icon"
                )
            }
        }
    }
}