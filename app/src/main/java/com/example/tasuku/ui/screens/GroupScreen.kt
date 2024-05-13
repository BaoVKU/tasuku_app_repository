package com.example.tasuku.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.model.Group
import com.example.tasuku.ui.components.group.GroupCreateDialog
import com.example.tasuku.ui.navigation.GroupDestination
import com.example.tasuku.ui.components.group.GroupItem
import com.example.tasuku.ui.components.group.GroupJoinDialog
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.GroupAddFAB
import com.example.tasuku.ui.components.layout.GroupJoinFAB
import com.example.tasuku.ui.components.layout.NavTopBar
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.GroupListUiState
import com.example.tasuku.ui.viewmodels.GroupViewModel

@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateWithString: (NavigationDestination, String) -> Unit,
    viewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val groupListUiState by viewModel.groupListUiState.collectAsState()
    var isOpenGroupCreateDialog by remember {
        mutableStateOf(false)
    }
    var isOpenGroupJoinDialog by remember {
        mutableStateOf(false)
    }
    var groupList by remember {
        mutableStateOf(listOf<Group>())
    }
    if(groupListUiState is GroupListUiState.Success){
        groupList = (groupListUiState as GroupListUiState.Success).data
    }
    Scaffold(
        topBar = {
            NavTopBar(title = stringResource(id = GroupDestination.titleRes))
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = GroupDestination,
                onNavClick = onNavigate
            )

        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                GroupJoinFAB(onFABClick = {
                    isOpenGroupJoinDialog = true
                })
                Spacer(modifier = Modifier.height(16.dp))
                GroupAddFAB(onFABClick = {
                    isOpenGroupCreateDialog = true
                })
            }
        }, modifier = modifier
    ) {
        when (groupListUiState) {
            is GroupListUiState.Loading -> {
                Text(text = "Loading...")
            }

            is GroupListUiState.Error -> {
                Text(text = (groupListUiState as GroupListUiState.Error).message)
            }

            is GroupListUiState.Success -> {
                LazyColumn(contentPadding = it) {
                    items(groupList) { group ->
                        GroupItem(
                            group = group,
                            isGroupOwner = group.creatorId == viewModel.authUserId,
                            onNavigateWithString = onNavigateWithString,
                            onLeaveGroup = {
                                viewModel.leaveGroup(context, group.id)
                                groupList = groupList.filter { groupItem -> groupItem.id != group.id}
                            }
                        )
                    }
                }
            }
        }
    }

    if (isOpenGroupCreateDialog) {
        GroupCreateDialog(
            onDismissRequest = { isOpenGroupCreateDialog = false },
            nameValue = viewModel.groupCreateFormState.name,
            descriptionValue = viewModel.groupCreateFormState.description,
            onNameValueChange = {
                viewModel.groupCreateFormState = viewModel.groupCreateFormState.copy(name = it)
            },
            onDescriptionValueChange = {
                viewModel.groupCreateFormState =
                    viewModel.groupCreateFormState.copy(description = it)
            },
            onCreateClick = {
                viewModel.createGroup(
                    context,
                    viewModel.groupCreateFormState.name,
                    viewModel.groupCreateFormState.description
                )
                isOpenGroupCreateDialog = false
            }
        )
    }

    if (isOpenGroupJoinDialog) {
        GroupJoinDialog(onDismissRequest = { isOpenGroupJoinDialog = false },
            keyValue = viewModel.groupJoinFormState.key,
            onKeyValueChange = {
                viewModel.groupJoinFormState = viewModel.groupJoinFormState.copy(key = it)
            },
            onJoinClick = {
                viewModel.joinGroup(context,viewModel.groupJoinFormState.key)
                isOpenGroupJoinDialog = false
            })
    }
}