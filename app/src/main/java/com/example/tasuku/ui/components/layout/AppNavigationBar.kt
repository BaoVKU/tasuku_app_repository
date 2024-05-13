package com.example.tasuku.ui.components.layout

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasuku.R
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.ChatDestination
import com.example.tasuku.ui.navigation.GroupDestination
import com.example.tasuku.ui.navigation.HomeDestination
import com.example.tasuku.ui.navigation.ScheduleDestination

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavigationDestination = HomeDestination,
    onNavClick: (NavigationDestination) -> Unit={}
) {
    NavigationBar(
        modifier = modifier
    ) {
        val homeIconID =
            if (currentDestination == HomeDestination) R.drawable.ic_home_filled else R.drawable.ic_home
        val groupIconID = if (currentDestination == GroupDestination) R.drawable.ic_group_filled else R.drawable.ic_group
        val chatIconID = if (currentDestination == ChatDestination) R.drawable.ic_message_filled else R.drawable.ic_message
        val scheduleIconID =
            if (currentDestination == ScheduleDestination) R.drawable.ic_calendar_filled else R.drawable.ic_calendar
        NavigationBarItem(
            selected = currentDestination == HomeDestination,
            onClick = { onNavClick(HomeDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = homeIconID),
                    contentDescription = "nav_home_icon"
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nav_home_label),
                    style = MaterialTheme.typography.labelLarge
                )
            })
        NavigationBarItem(
            selected = currentDestination == GroupDestination,
            onClick = { onNavClick(GroupDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = groupIconID),
                    contentDescription = "nav_group_icon"
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nav_group_label),
                    style = MaterialTheme.typography.labelLarge
                )
            })
        NavigationBarItem(
            selected = currentDestination == ChatDestination,
            onClick = { onNavClick(ChatDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = chatIconID),
                    contentDescription = "nav_message_icon"
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nav_message_label),
                    style = MaterialTheme.typography.labelLarge
                )
            })
        NavigationBarItem(
            selected = currentDestination == ScheduleDestination,
            onClick = { onNavClick(ScheduleDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = scheduleIconID),
                    contentDescription = "nav_calendar_icon"
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nav_calendar_label),
                    style = MaterialTheme.typography.labelLarge
                )
            })
    }
}

@Preview
@Composable
fun AppNavigationBarPreview() {
    BottomNavigationBar() {}
}