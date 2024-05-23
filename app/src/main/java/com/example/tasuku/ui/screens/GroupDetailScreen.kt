package com.example.tasuku.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.R
import com.example.tasuku.model.Group
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.components.group.GroupDropDownMenu
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.components.layout.TaskAddFAB
import com.example.tasuku.ui.components.task.TaskCard
import com.example.tasuku.ui.navigation.GroupDetailScreenDestination
import com.example.tasuku.ui.navigation.GroupMemberScreenDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.GroupDetailUiState
import com.example.tasuku.ui.viewmodels.GroupDetailViewModel
import com.example.tasuku.ui.viewmodels.GroupMembersUiState
import com.example.tasuku.ui.viewmodels.TaskViewModel

@Composable
fun GroupDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit,
    onNavigateWithString: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit = {},
    groupDetailViewModel: GroupDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    taskViewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    var isMenuExpanded by remember { mutableStateOf(false) }
    val groupDetailUiState by groupDetailViewModel.groupDetailUiState.collectAsState()
    val groupMembersUiState by groupDetailViewModel.groupMembersUiState.collectAsState()
    var isOpenFilterDialog by remember { mutableStateOf(false) }

    when (groupDetailUiState) {
        is GroupDetailUiState.Loading -> {
            Text(text = "Loading...")
        }

        is GroupDetailUiState.Error -> {
            Text(text = (groupDetailUiState as GroupDetailUiState.Error).message)
        }

        is GroupDetailUiState.Success -> {
            val group = (groupDetailUiState as GroupDetailUiState.Success).data.group
            Scaffold(
                topBar = {
                    PageTopBar(
                        title = group.name,
                        subtitle = group.memberCount.toString() + " members",
                        onNavigateBack = onNavigateBack
                    ) {
                        GroupDetailMoreIconButton(
                            isMenuExpanded = isMenuExpanded,
                            isGroupOwner = group.creatorId == groupDetailViewModel.authUserId,
                            context = context,
                            group = group,
                            onClick = { isMenuExpanded = true },
                            onDismissRequest = { isMenuExpanded = false },
                            onNavigateWithString = onNavigateWithString,
                            onLeaveGroup = {
                                groupDetailViewModel.leaveGroup(context, group.id)
                                onNavigateBack()
                            }
                        )
                    }
                }, floatingActionButton = {
                    TaskAddFAB(onFABClick = onNavigate,
                        onFilterClick = { isOpenFilterDialog = true })
                },
                modifier = modifier
            ) {

                LazyColumn(
                    contentPadding = it,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    items(groupDetailViewModel.taskList) { taskResponse ->
                        TaskCard(
                            taskResponse = taskResponse,
                            authUserId = groupDetailViewModel.authUserId,
                            onNavigateWithArgs = onNavigateWithArgs,
                            onDeleteTask = { id ->
                                taskViewModel.deleteTask(context, id)
                                groupDetailViewModel.refreshGroup()
                            },
                            onOperateTask = { taskId, operation ->
                                taskViewModel.operateTask(taskId, operation)
                            })
                    }
                }
            }
            if (isOpenFilterDialog) {
                FilterScreen(
                    taskList = groupDetailViewModel.taskList,
                    groupMemberList = if(groupMembersUiState is GroupMembersUiState.Success) {
                        (groupMembersUiState as GroupMembersUiState.Success).data
                    } else {
                        listOf()
                    },
                    onDismissRequest = {
                        isOpenFilterDialog = false
                    },
                    onResetClick = {
                        groupDetailViewModel.resetTaskList()
                        isOpenFilterDialog = false
                    },
                    onSubmitFilterClick = {
                        groupDetailViewModel.taskList = it
                        isOpenFilterDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun GroupDetailMoreIconButton(
    modifier: Modifier = Modifier,
    context: Context,
    group: Group,
    isMenuExpanded: Boolean = false,
    isGroupOwner: Boolean = false,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onNavigateWithString: (NavigationDestination, String) -> Unit,
    onLeaveGroup: () -> Unit
) {
    Column(modifier = modifier) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "more_icon"
            )
        }
        GroupDropDownMenu(
            isMenuExpanded = isMenuExpanded,
            isGroupOwner = isGroupOwner,
            onDismissRequest = onDismissRequest,
            onNavigateWithString = {
                onNavigateWithString(
                    GroupMemberScreenDestination,
                    group.joinKey
                )
            },
            onCopyJoinKey = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("join_key", group.joinKey)
                clipboard.setPrimaryClip(clip)
            },
            onLeaveGroup = onLeaveGroup
        )
    }
}