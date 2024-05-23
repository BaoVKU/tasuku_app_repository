package com.example.tasuku.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.TaskAddScreenDestination

@Composable
fun TaskAddFAB(onFABClick: (NavigationDestination) -> Unit, onFilterClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.End) {
        Surface(
            shape = CircleShape,
            shadowElevation = 4.dp,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable {
                onFilterClick()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "filter_icon",
                modifier = Modifier.padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(3.dp),
            onClick = { onFABClick(TaskAddScreenDestination) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "fab_task_add_icon"
            )
        }
    }
}

@Composable
fun GroupAddFAB(onFABClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(text = { Text(text = "Create") }, icon = {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "fab_group_add_icon"
        )
    }, onClick = onFABClick, modifier = modifier)
}

@Composable
fun GroupJoinFAB(onFABClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        elevation = FloatingActionButtonDefaults.elevation(3.dp),
        onClick = onFABClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_login),
            contentDescription = "fab_group_join_icon"
        )
    }
}

@Preview
@Composable
fun TaskAddFABPreview() {
    Surface() { TaskAddFAB(onFABClick = {}) }
}

@Preview
@Composable
fun GroupAddFABPreview() {
    GroupAddFAB(onFABClick = {})
}