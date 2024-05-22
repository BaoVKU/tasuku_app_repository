package com.example.tasuku.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.R
import com.example.tasuku.setSystemBarColor
import com.example.tasuku.ui.navigation.ChatDestination
import com.example.tasuku.ui.components.chat.ChatItem
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.NavTopBar
import com.example.tasuku.ui.navigation.HomeDestination
import com.example.tasuku.ui.navigation.MessageScreenDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.header.ChannelListHeader
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.InitializationState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ChannelViewModelFactory = ChannelViewModelFactory(),
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateWithString: (NavigationDestination, String) -> Unit,
) {
    val client = ChatClient.instance()
    val clientInitialisationState by client.clientState.initializationState.collectAsState()

    ChatTheme {
        when (clientInitialisationState) {
            InitializationState.COMPLETE -> {
                val listViewModel: ChannelListViewModel = viewModel(
                    ChannelListViewModel::class.java,
                    factory = viewModelFactory,
                )
                val connectionState by listViewModel.connectionState.collectAsState()
                val user by listViewModel.user.collectAsState()

                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            ChannelListHeader(
                                currentUser = user,
                                title = stringResource(id = R.string.top_bar_chat_title),
                                connectionState = connectionState,
                                color = MaterialTheme.colorScheme.background,
                                elevation = 0.dp,
                                leadingContent = {
                                    Text(
                                        text = stringResource(id = R.string.top_bar_chat_title),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                centerContent = { Spacer(modifier = Modifier.weight(1f))},
                                trailingContent = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        })
                    },
                    bottomBar = {
                                BottomNavigationBar(
                                    currentDestination = ChatDestination,
                                    onNavClick = onNavigate
                                )
                    }
                    , modifier = modifier
                ) { contentPadding ->
                    Column(modifier = Modifier.padding(contentPadding)) {
                        ChannelList(modifier = Modifier.fillMaxSize(),
                            viewModel = listViewModel,
                            onChannelClick = { channel ->
                                onNavigateWithString(MessageScreenDestination, channel.cid)
                            },
                            onChannelLongClick = remember(listViewModel) {
                                {
                                    listViewModel.selectChannel(it)
                                }
                            },
                            divider = {})

                    }
                }
            }

            InitializationState.INITIALIZING -> {
                Text(text = "Initialising...")
            }

            InitializationState.NOT_INITIALIZED -> {
                Toast.makeText(
                    LocalContext.current,
                    "Chat initialisation failed",
                    Toast.LENGTH_SHORT
                ).show()
                onNavigate(HomeDestination)
            }
        }
    }
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.setSystemBarColor(color = Color.Transparent)
}


@Composable
fun ChatSearchBar(searchValue: String, onValueChange: (String) -> Unit, onClearClick: () -> Unit) {
    TextField(
        value = searchValue,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search_icon"
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = "clear_icon",
                modifier = Modifier.clickable { onClearClick() }
            )

        },
        placeholder = { Text("Search") },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp))
    )
}
