package com.example.tasuku.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.ui.navigation.ChatDestination
import com.example.tasuku.ui.components.chat.ChatItem
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.NavTopBar
import com.example.tasuku.ui.navigation.NavigationDestination


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateWithArgs: (NavigationDestination, Int) -> Unit
) {
    var searchValue by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            NavTopBar(title = stringResource(id = ChatDestination.titleRes))
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = ChatDestination,
                onNavClick = onNavigate
            )

        }, modifier = modifier
    ) {
        LazyColumn(
            contentPadding = it,
            modifier = Modifier.padding(8.dp)
        ) {
            item {
                ChatSearchBar(
                    searchValue = searchValue,
                    onValueChange = { value -> searchValue = value },
                    onClearClick = { searchValue = "" })
            }
            items(3) {
                ChatItem(onShowMessage = onNavigateWithArgs)
            }
        }
    }
}


@Composable
fun ChatSearchBar(searchValue: String, onValueChange: (String) -> Unit, onClearClick: () -> Unit) {
    TextField(
        value = searchValue,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search_icon"
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = "clear_icon",
                modifier = Modifier.clickable { onClearClick() }
            )

        },
        placeholder = { Text("Search") },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp))
    )
}
