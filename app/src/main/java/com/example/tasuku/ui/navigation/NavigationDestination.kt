package com.example.tasuku.ui.navigation

import com.example.tasuku.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.top_bar_login_title
}

object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.top_bar_register_title
}

object ForgotPasswordDestination : NavigationDestination {
    override val route = "forgot_password"
    override val titleRes = R.string.top_bar_forgot_password_title
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.top_bar_home_title
}

object GroupDestination : NavigationDestination {
    override val route = "group"
    override val titleRes = R.string.top_bar_group_title
}

object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.top_bar_chat_title
}

object ScheduleDestination : NavigationDestination {
    override val route = "schedule"
    override val titleRes = R.string.top_bar_schedule_title
}

object NotificationDestination : NavigationDestination {
    override val route = "notification"
    override val titleRes = R.string.top_bar_notification_title
}

object ProfileScreenDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.top_bar_profile_title
}

object TaskAddScreenDestination : NavigationDestination {
    override val route = "task_add"
    override val titleRes = R.string.top_bar_task_add_title
}

object TaskDetailScreenDestination : NavigationDestination {
    override val route = "task_detail"
    override val titleRes = 0
    val routeWithArgs = "$route/{taskId}"
}

object GroupDetailScreenDestination : NavigationDestination {
    override val route = "group_detail"
    override val titleRes = 0
    val routeWithArgs = "$route/{joinKey}"
}

object MessageScreenDestination : NavigationDestination {
    override val route = "message"
    override val titleRes = 0
    val routeWithArgs = "$route/{channelId}"
}

object TaskUpdateScreenDestination : NavigationDestination {
    override val route = "task_update"
    override val titleRes = R.string.top_bar_task_edit_title
    val routeWithArgs = "$route/{taskId}"
}

object GroupMemberScreenDestination : NavigationDestination {
    override val route = "group_member"
    override val titleRes = R.string.top_bar_group_member_title
    val routeWithArgs = "$route/{joinKey}"
}