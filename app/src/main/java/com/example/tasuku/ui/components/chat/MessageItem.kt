package com.example.tasuku.ui.components.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.model.MessageType

@Composable
fun MessageItem(
    messageType: MessageType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = if (messageType == MessageType.SENT) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun TextMessageItem(messageType: MessageType, modifier: Modifier = Modifier) {
    MessageItem(messageType = messageType, modifier = modifier) {
        Surface(
            color =
            if (messageType == MessageType.SENT)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.background,
            contentColor =
            if (messageType == MessageType.SENT)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onBackground,
            border =
            if (messageType == MessageType.RECEIVED)
                BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            else
                null,
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = "Hello",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun FileMessageItem(messageType: MessageType, modifier: Modifier = Modifier) {
    MessageItem(messageType = messageType, modifier = modifier) {
        Surface(
            color =
            if (messageType == MessageType.SENT)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.background,
            contentColor =
            if (messageType == MessageType.SENT)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onBackground,
            border =
            if (messageType == MessageType.RECEIVED)
                BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            else
                null,
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = "Hello",
                textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ImageMessageItem(messageType: MessageType, modifier: Modifier = Modifier) {
    MessageItem(messageType = messageType, modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth(0.6f)) {
            Image(
                painter = painterResource(R.drawable.file_image),
                contentDescription = "image_message",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(24.dp))
            )
        }
    }
}