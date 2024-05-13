package com.example.tasuku.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.tasuku.ui.screens.ChatScreen
import com.example.tasuku.ui.screens.GroupDetailScreen
import com.example.tasuku.ui.screens.GroupScreen
import com.example.tasuku.ui.screens.HomeScreen
import com.example.tasuku.ui.screens.MessageScreen
import com.example.tasuku.ui.screens.NotificationScreen
import com.example.tasuku.ui.screens.ScheduleScreen
import com.example.tasuku.ui.screens.TaskDetailScreen
import com.example.tasuku.ui.screens.ForgotPasswordScreen
import com.example.tasuku.ui.screens.GroupMemberScreen
import com.example.tasuku.ui.screens.LoginScreen
import com.example.tasuku.ui.screens.ProfileScreen
import com.example.tasuku.ui.screens.RegisterScreen
import com.example.tasuku.ui.screens.TaskAddUpdateScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(LoginDestination.route) {
            LoginScreen(
                onNavigate = { navController.navigate(it.route) }
            )
        }

        composable(RegisterDestination.route) {
            RegisterScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(ForgotPasswordDestination.route) {
            ForgotPasswordScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(HomeDestination.route) {
            HomeScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateWithArgs = { destination, id ->
                    navController.navigate("${destination.route}/$id")
                })
        }

        composable(GroupDestination.route) {
            GroupScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateWithString = { destination, string ->
                    navController.navigate("${destination.route}/$string")
                })
        }

        composable(ChatDestination.route) {
            ChatScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateWithArgs = { destination, id ->
                    navController.navigate("${destination.route}/$id")
                })
        }

        composable(ScheduleDestination.route) {
            ScheduleScreen(onNavigate = { navController.navigate(it.route) })
        }

        composable(NotificationDestination.route) {
            NotificationScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable(ProfileScreenDestination.route) {
            ProfileScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateBack = { navController.navigateUp() })
        }

        composable(TaskAddScreenDestination.route) {
            TaskAddUpdateScreen(
                isHomeScreen = navController.previousBackStackEntry?.destination?.route == HomeDestination.route,
                onNavigateBack = { navController.navigateUp() })
        }

        composable(
            route = TaskDetailScreenDestination.routeWithArgs,
            arguments = listOf(navArgument("taskId") {
                type = NavType.IntType
            })
        ) {
            TaskDetailScreen(
                onNavigateWithArgs = { destination, id ->
                    navController.navigate("${destination.route}/$id")
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = GroupDetailScreenDestination.routeWithArgs,
            arguments = listOf(navArgument("joinKey") {
                type = NavType.StringType
            })
        ) {
            GroupDetailScreen(
                onNavigate = { navController.navigate(it.route) },
                onNavigateWithArgs = { destination, id ->
                    navController.navigate("${destination.route}/$id")
                },
                onNavigateWithString = { destination, string ->
                    navController.navigate("${destination.route}/$string")
                },
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable(
            route = MessageScreenDestination.routeWithArgs,
            arguments = listOf(navArgument("messageId") {
                type = NavType.IntType
            })
        ) {
            MessageScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable(
            route = TaskUpdateScreenDestination.routeWithArgs,
            arguments = listOf(navArgument("taskId") {
                type = NavType.IntType
            })
        ) {
            TaskAddUpdateScreen(
                isHomeScreen = navController.previousBackStackEntry?.destination?.route == HomeDestination.route,
                editingTaskId = backStackEntry.value?.arguments?.getInt("taskId"),
                onNavigateBack = { navController.navigateUp() })
        }

        composable(
            route = GroupMemberScreenDestination.routeWithArgs,
            arguments = listOf(navArgument("joinKey") {
                type = NavType.StringType
            })
        ) {
            GroupMemberScreen(onNavigateBack = { navController.navigateUp() })
        }

    }
}