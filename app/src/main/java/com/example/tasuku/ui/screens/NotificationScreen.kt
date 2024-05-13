package com.example.tasuku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.navigation.NotificationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            PageTopBar(
                title = stringResource(id = NotificationDestination.titleRes),
                onNavigateBack = onNavigateBack
            )
        }, modifier = modifier
    ) {
        LazyColumn(contentPadding = it) {
            items(5) {
                NotificationItem(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationItem(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissState = remember {
        DismissState(
            initialValue = DismissValue.Default,
            confirmValueChange = { state ->
                if (state == DismissValue.DismissedToEnd) {
                    coroutineScope.launch {
                        delay(500)
                    }
                    true
                } else {
                    false
                }
            }
        )
    }
    SwipeToDismiss(state = swipeToDismissState, background = {
        Surface(color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "delete_member_icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }, dismissContent = {
        Surface(color = Color.White) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "20/12/2023", style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Notification Title", style = MaterialTheme.typography.titleMedium)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_to_group),
                        contentDescription = "to_group_icon"
                    )
                    Text(text = "Group Name", style = MaterialTheme.typography.titleMedium)
                }
                Text(text = "Notification Description", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }, modifier = modifier)
}

@Preview
@Composable
fun NotificationItemPreview() {
    NotificationItem()
}