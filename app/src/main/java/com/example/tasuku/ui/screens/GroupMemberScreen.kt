package com.example.tasuku.ui.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.R
import com.example.tasuku.setSystemBarColor
import com.example.tasuku.ui.components.group.GroupMemberItem
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.navigation.MessageScreenDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.ChannelCreateUiState
import com.example.tasuku.ui.viewmodels.GroupInfoUiState
import com.example.tasuku.ui.viewmodels.GroupMemberViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun GroupMemberScreen(
    modifier: Modifier = Modifier,
    viewModel: GroupMemberViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateWithString: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val groupInfoUiState by viewModel.groupMemberResponseUiState.collectAsState()

    val channelCreateUiState by viewModel.channelCreateUiState.collectAsState()

    if (channelCreateUiState is ChannelCreateUiState.Success){
        val cid = (channelCreateUiState as ChannelCreateUiState.Success).channel?.cid ?: ""
        if(cid.isNotEmpty()){
            viewModel.setChannelCreateUiStateToLoading()
            onNavigateWithString(MessageScreenDestination, cid)
        }
    }

    when (groupInfoUiState) {
        is GroupInfoUiState.Loading -> {
            Text(text = "Loading...")
        }

        is GroupInfoUiState.Error -> {
            Text(text = (groupInfoUiState as GroupInfoUiState.Error).message)
        }

        is GroupInfoUiState.Success -> {
            val groupMemberResponse = (groupInfoUiState as GroupInfoUiState.Success).data

            Scaffold(
                topBar = {
                    PageTopBar(
                        title = groupMemberResponse.group.name,
                        subtitle = groupMemberResponse.members.size.toString() + " members",
                        onNavigateBack = onNavigateBack
                    )
                },
                modifier = modifier
            ) {
                LazyColumn(contentPadding = it) {
                    items(groupMemberResponse.members) { member ->
                        GroupMemberItem(
                            context = context,
                            authUserId = viewModel.authUserId,
                            member = member,
                            onMessageClick = {
                                viewModel.createChannelIfExist(member.id)
                            }
                        )
                    }
                }
            }
        }
    }
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.setSystemBarColor(color = Color.Transparent)
}