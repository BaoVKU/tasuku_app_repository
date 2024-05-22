package com.example.tasuku.ui.screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.isScrollingUp
import com.example.tasuku.ui.navigation.HomeDestination
import com.example.tasuku.setSystemBarColor
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.HomeTopBar
import com.example.tasuku.ui.components.layout.TaskAddFAB
import com.example.tasuku.ui.components.task.TaskCard
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.HomeUiState
import com.example.tasuku.ui.viewmodels.HomeViewModel
import com.example.tasuku.ui.viewmodels.TaskViewModel
import com.example.tasuku.ui.viewmodels.UserUiState
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    taskViewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit,
) {
    val context = LocalContext.current
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp(),
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                HomeTopBar(
                    userAvatar = BaseUrl.URL + homeViewModel.authUserAvatar,
                    onNavigate = onNavigate
                )
            }

        },
        bottomBar = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp(),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavigationBar(
                    currentDestination = HomeDestination,
                    onNavClick = onNavigate
                )
            }
        },
        floatingActionButton = {
            TaskAddFAB(onFABClick = onNavigate)
        }, modifier = modifier
    ) {
        LazyColumn(
            contentPadding = it,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            when (homeUiState) {
                is HomeUiState.Loading -> {
                    item { Text(text = "Loading tasks...") }
                }

                is HomeUiState.Error -> {
                    item { Text(text = "Error") }
                }

                is HomeUiState.Success -> {
                    val taskList = (homeUiState as HomeUiState.Success).data
                    items(taskList) { taskResponse ->
                        TaskCard(
                            taskResponse = taskResponse,
                            authUserId = homeViewModel.authUserId,
                            onNavigateWithArgs = onNavigateWithArgs,
                            onDeleteTask = { id ->
                                taskViewModel.deleteTask(context, id)
                                homeViewModel.getTasks()
                            },
                            onOperateTask = { taskId, operation ->
                                taskViewModel.operateTask(taskId, operation)
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