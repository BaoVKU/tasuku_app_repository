package com.example.tasuku.ui.components.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.model.Task
import com.example.tasuku.model.TaskMember

@Composable
fun TaskCardMemberList(list: List<TaskMember>, modifier: Modifier = Modifier) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier.padding(8.dp)) {
        items(list) {member->
            TaskCardMemberItem(member= member, isCompleted = (member.isCompleted == 1))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCardMemberItem(member:TaskMember, isCompleted:Boolean ,modifier: Modifier = Modifier) {
    val tooltipState:PlainTooltipState = remember {
        PlainTooltipState()
    }
    val ringColor = if (isCompleted) Color(74, 222, 128) else Color(248, 113, 113)
    PlainTooltipBox(
        tooltip = { Text(text = member.name, style = MaterialTheme.typography.bodyLarge) },
        tooltipState = tooltipState,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BaseUrl.URL+member.avatar)
                .crossfade(true)
                .build(),
            contentDescription = "member_avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(
                    CircleShape
                )
                .border(width = 2.dp, color = ringColor, shape = CircleShape)
                .tooltipAnchor())

    }

}

@Preview
@Composable
fun TaskCardMemberListPreview() {
    TaskCardMemberList(listOf())
}