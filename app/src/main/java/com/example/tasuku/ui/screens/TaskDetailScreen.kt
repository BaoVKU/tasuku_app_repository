package com.example.tasuku.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.calculateTaskCompletion
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.components.task.CommentBar
import com.example.tasuku.ui.components.task.CommentList
import com.example.tasuku.ui.components.task.TaskCardContent
import com.example.tasuku.ui.components.task.TaskCardFooter
import com.example.tasuku.ui.components.task.TaskCardHeader
import com.example.tasuku.ui.components.task.TaskCardMemberList
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.CommentUiState
import com.example.tasuku.ui.viewmodels.TaskUiState
import com.example.tasuku.ui.viewmodels.TaskDetailViewModel
import com.example.tasuku.ui.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit,
    onNavigateBack: () -> Unit = {},
    taskDetailViewModel: TaskDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    taskViewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    var isMenuExpanded by remember { mutableStateOf(false) }
    val taskUiState by taskDetailViewModel.taskUiState.collectAsState()
    val commentUiState by taskDetailViewModel.commentUiState.collectAsState()

    var pickedImageList by remember {
        mutableStateOf(listOf<Uri>())
    }

    when (taskUiState) {
        is TaskUiState.Loading -> {
            Text(text = "Loading...")
        }

        is TaskUiState.Error -> {
            Text(text = "Error: ${(taskUiState as TaskUiState.Error).message}")
        }

        is TaskUiState.Success -> {
            val taskResponse = (taskUiState as TaskUiState.Success).data
            Scaffold(
                topBar = {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        TopAppBar(title = {
                            TaskCardHeader(
                                isMenuExpanded = isMenuExpanded,
                                authUserId = taskDetailViewModel.authUserId,
                                onMoreExpand = { isMenuExpanded = true },
                                onDismissRequest = { isMenuExpanded = false },
                                onNavigateWithArgs = onNavigateWithArgs,
                                task = taskResponse.task,
                                onDeleteTask = { id ->
                                    taskViewModel.deleteTask(context, id)
                                    onNavigateBack()
                                }
                            )
                        },
                            navigationIcon = {
                                IconButton(onClick = onNavigateBack) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowLeft,
                                        contentDescription = "back_icon"
                                    )
                                }
                            })
                    }
                },
                bottomBar = {
                    Column {
                        if (pickedImageList.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .height(120.dp)
                                    .padding(4.dp)
                                    .fillMaxWidth()
                            ) {
                                items(pickedImageList) { uri ->
                                    Box(
                                        contentAlignment = Alignment.TopEnd, modifier = Modifier
                                            .height(100.dp)
                                            .fillMaxWidth()
                                    ) {
                                        val painter = rememberAsyncImagePainter(
                                            ImageRequest.Builder(context).data(uri).build()
                                        )
                                        Image(
                                            painter = painter,
                                            contentDescription = "image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(
                                                    RoundedCornerShape(8.dp)
                                                )
                                        )
                                        IconButton(
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = Color(1f, 1f, 1f, 0.8f),
                                                contentColor = MaterialTheme.colorScheme.onBackground
                                            ),
                                            onClick = {
                                                pickedImageList =
                                                    pickedImageList.filter { it != uri }
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_clear),
                                                contentDescription = "delete_image_icon",
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        CommentBar(
                            commentValue = taskDetailViewModel.commentFormState.comment,
                            onCommentValueChange = { value ->
                                taskDetailViewModel.commentFormState =
                                    taskDetailViewModel.commentFormState.copy(
                                        comment = value
                                    )
                            },
                            onPickImageChange = { uris ->
                                pickedImageList = uris
                            },
                            onSendClick = {
                                taskDetailViewModel.createComment(
                                    context,
                                    taskResponse.task.id,
                                    taskDetailViewModel.commentFormState.comment,
                                    pickedImageList
                                )
                                taskDetailViewModel.commentFormState =
                                    taskDetailViewModel.commentFormState.copy(
                                        comment = ""
                                    )
                                pickedImageList = listOf()
                            }
                        )
                    }
                }, modifier = modifier
            ) { contentPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(contentPadding)
                ) {
                    item {
                        LinearProgressIndicator(
                            progress = calculateTaskCompletion(taskResponse.members),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        TaskCardContent(taskResponse = taskResponse, isShowingDetail = true)
                        Divider(thickness = 1.dp)
                        val userWhoIsMember =
                            taskResponse.members.find { member -> member.id == taskDetailViewModel.authUserId }
                        TaskCardFooter(
                            userWhoIsMember = userWhoIsMember,
                            onOperateTask = { operation ->
                                taskViewModel.operateTask(taskResponse.task.id, operation)
                            })
                        Divider(thickness = 1.dp)
                        if (taskResponse.members.isNotEmpty()) {
                            TaskCardMemberList(list = taskResponse.members)
                            Divider(thickness = 1.dp)
                        }
                        when (commentUiState) {
                            is CommentUiState.Loading -> {
                                Text(text = "Loading comments...")
                            }

                            is CommentUiState.Error -> {
                                Text(text = "Error: ${(commentUiState as CommentUiState.Error).message}")
                            }

                            is CommentUiState.Success -> {
                                CommentList(
                                    list = (commentUiState as CommentUiState.Success).data,
                                    authUserId = taskDetailViewModel.authUserId,
                                    onDeleteComment = { id ->
                                        taskDetailViewModel.deleteComment(context, id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}