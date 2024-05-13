package com.example.tasuku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.model.MessageType
import com.example.tasuku.ui.components.chat.FileMessageItem
import com.example.tasuku.ui.components.chat.ImageMessageItem
import com.example.tasuku.ui.components.chat.MessageTopBar
import com.example.tasuku.ui.components.chat.MessageTypingBar
import com.example.tasuku.ui.components.chat.TextMessageItem

@Composable
fun MessageScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            MessageTopBar(onNavigateBack = onNavigateBack)
        },
        bottomBar = {
            MessageTypingBar()
        }, modifier = modifier
    ) {
        LazyColumn(
            contentPadding = it, modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TextMessageItem(messageType = MessageType.SENT)
            }
            item {
                FileMessageItem(messageType = MessageType.RECEIVED)
            }
            item {
                ImageMessageItem(messageType = MessageType.SENT)
            }
        }
    }
}

@Preview
@Composable
fun MessageScreenPreview() {
    MessageScreen()
}