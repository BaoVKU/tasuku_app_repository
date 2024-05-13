package com.example.tasuku.ui.components.task

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.model.CommentResponse
import com.example.tasuku.ui.components.layout.ConfirmDialog


@Composable
fun CommentBar(
    modifier: Modifier = Modifier,
    commentValue: String,
    onCommentValueChange: (String) -> Unit,
    onPickImageChange: (List<Uri>) -> Unit,
    onSendClick: () -> Unit
) {
    BottomAppBar(modifier = modifier.imePadding()) {
        val context = LocalContext.current
        val getContent =
            rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                onPickImageChange(uris)
            }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                getContent.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_image_search),
                    contentDescription = "upload_image_icon"
                )
            }
            TextField(
                value = commentValue,
                onValueChange = onCommentValueChange,
                placeholder = {
                    Text(
                        text = "Add a comment...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(shape = RoundedCornerShape(48.dp))
            )
            IconButton(onClick = onSendClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "send_icon"
                )
            }
        }
    }
}


@Composable
fun CommentList(
    list: List<CommentResponse>,
    authUserId: Int,
    modifier: Modifier = Modifier,
    onDeleteComment: (Int) -> Unit
) {
    var comments by remember {
        mutableStateOf(list)
    }
    Column(modifier = modifier.padding(8.dp)) {
        comments.forEach { comment ->
            CommentItem(
                commentResponse = comment,
                authUserId = authUserId,
                onDeleteComment = {
                    val id = comment.comment.id
                    onDeleteComment(id)
                    comments = comments.filter { it.comment.id != id }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CommentItem(
    commentResponse: CommentResponse,
    authUserId: Int,
    modifier: Modifier = Modifier,
    onDeleteComment: () -> Unit
) {
    var isOpenCommentDeleteConfirmDialog by remember { mutableStateOf(false) }
    Row(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(BaseUrl.URL + commentResponse.comment.avatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = "comment_owner_avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(shape = CircleShape)
                )
            }
            if (authUserId == commentResponse.comment.userId) {
                IconButton(
                    onClick = { isOpenCommentDeleteConfirmDialog = true }, modifier = Modifier
                        .padding(top = 16.dp)
                        .clip(shape = CircleShape)
                        .size(40.dp)
                        .background(color = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "delete_icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = commentResponse.comment.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = commentResponse.comment.createdAt,
                style = MaterialTheme.typography.bodySmall
            )
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(8.dp)
            ) {
                Text(
                    text = commentResponse.comment.comment,
                    style = MaterialTheme.typography.bodyLarge
                )
                val imageList = commentResponse.attachments.filter { it.type.contains("image") }
                val fileList = commentResponse.attachments.filter { !it.type.contains("image") }
                TaskCardFileList(
                    list = fileList
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
    if (isOpenCommentDeleteConfirmDialog) {
        ConfirmDialog(
            confirmText = "Are you sure you want to delete this comment?",
            onDismissRequest = { isOpenCommentDeleteConfirmDialog = false }, onConfirm = {
                onDeleteComment()
                isOpenCommentDeleteConfirmDialog = false
            })
    }
}
