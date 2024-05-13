package com.example.tasuku.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.ui.navigation.MessageScreenDestination
import com.example.tasuku.ui.navigation.NavigationDestination

@Composable
fun ChatItem(modifier: Modifier = Modifier, onShowMessage:(NavigationDestination, Int) -> Unit,) {
    Row(modifier = modifier.padding(8.dp).clickable { onShowMessage(MessageScreenDestination,0) }) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.woman_avatar),
                contentDescription = "chatter_avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxHeight()
            .weight(1f)) {
            Text("Chatter Name", style = MaterialTheme.typography.titleMedium)
            Text("Last message", style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text = "2 minutes ago",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
