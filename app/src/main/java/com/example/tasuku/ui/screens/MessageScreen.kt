package com.example.tasuku.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.setSystemBarColor
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.MessageViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    messageViewModel: MessageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.setSystemBarColor(color = Color.White)
    val context = LocalContext.current

    val viewModelFactory =
        MessagesViewModelFactory(context = context, channelId = messageViewModel.channelId)

    ChatTheme {
        Box(modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()) {
            MessagesScreen(
                viewModelFactory = viewModelFactory,
                onBackPressed = onNavigateBack
            )
        }
    }
}
