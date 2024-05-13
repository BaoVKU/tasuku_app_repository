package com.example.tasuku.ui.components.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.dateTimeToHhMmDdMmYyyy
import com.example.tasuku.model.Attachment
import com.example.tasuku.model.TaskResponse

@Composable
fun TaskCardContent(
    modifier: Modifier = Modifier,
    taskResponse: TaskResponse,
    isShowingDetail: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_expiration_start),
                contentDescription = "expiration_start_icon"
            )
            Text(
                text = dateTimeToHhMmDdMmYyyy(taskResponse.task.start),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_expiration_end),
                contentDescription = "expiration_end_icon"
            )
            Text(
                text = dateTimeToHhMmDdMmYyyy(taskResponse.task.end),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
        Text(
            text = taskResponse.task.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (isShowingDetail) {
            Text(
                text = taskResponse.task.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            val imageList = taskResponse.attachments.filter { it.type.contains("image") }
            val fileList = taskResponse.attachments.filter { !it.type.contains("image") }
            TaskCardFileList(
                list = fileList,
                modifier = Modifier.fillMaxWidth()
            )
            TaskCardImageList(
                list = imageList,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskCardFileList(list: List<Attachment>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        list.forEach { file ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file),
                    contentDescription = "file_icon"
                )
                Text(
                    text = file.name + file.extension,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }
        }

    }
}

@Composable
fun TaskCardImageList(list: List<Attachment>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        list.forEach { image ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(BaseUrl.URL + image.url).crossfade(true).build(),
                    contentDescription = "task_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)))
            }
        }
    }
}