package com.example.tasuku.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.R
import com.example.tasuku.ui.components.filter.CreatorFilterDialog
import com.example.tasuku.ui.components.filter.FilterRadioGroup
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.components.task.DockedDatePicker
import com.example.tasuku.ui.components.task.TaskMemberDialog
import com.example.tasuku.ui.components.task.TaskModeSpinner

@Composable
fun FilterScreen() {
    var isOpenMemberDialog by remember { mutableStateOf(false) }
    val creatorList = listOf("Member 1", "Member 2", "Member 3", "Member 4", "Member 5")
    var selectedCreator by remember {
        mutableStateOf(creatorList[0])
    }
    var isOpenCreatorFilter by remember {
        mutableStateOf(false)
    }
    var selectedDateFilter by remember {
        mutableStateOf("")
    }
    val dateFilterList = listOf("During", "Coming", "Expired")
    var selectedStatusFilter by remember {
        mutableStateOf("")
    }
    val statusFilterList = listOf("New", "Working", "Done")
    Scaffold(
        topBar = {
            PageTopBar(title = "Filter", trailingActions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "reset_icon")
                }
            })
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier
                .padding(it)
                .padding(8.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            OutlinedTextField(
                value = selectedCreator,
                readOnly = true,
                onValueChange = {},
                label = {
                    Text(
                        text = "Creator"
                    )
                },
                trailingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_search),
                        contentDescription = "search_icon",
                        modifier = Modifier.clickable {
                            isOpenCreatorFilter = true
                        })
                },
                modifier = Modifier.fillMaxWidth()
            )
//            TaskModeSpinner()
            OutlinedButton(
                shape = RoundedCornerShape(4.dp),
                onClick = { isOpenMemberDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Choose members")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.width(16.dp))
                Text(text = "Date", modifier = Modifier.padding(horizontal = 8.dp))
                Divider()
            }
            DockedDatePicker(label = "At", dateValue = "dd/mm/yyyy" ,modifier = Modifier.fillMaxWidth())
            FilterRadioGroup(
                filterList = dateFilterList,
                selectedFilter = selectedDateFilter,
                onClick = { date ->
                    selectedDateFilter = date
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.width(16.dp))
                Text(text = "Status", modifier = Modifier.padding(horizontal = 8.dp))
                Divider()
            }
            FilterRadioGroup(
                filterList = statusFilterList,
                selectedFilter = selectedStatusFilter,
                onClick = { status ->
                    selectedStatusFilter = status
                },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                shape = RoundedCornerShape(4.dp),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Apply filter", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
//    if (isOpenMemberDialog) {
//        TaskMemberDialog(onDismissRequest = { isOpenMemberDialog = false })
//    }
    if (isOpenCreatorFilter) {
        CreatorFilterDialog(
            creatorList = creatorList,
            selectedCreator = selectedCreator,
            onClick = {
                selectedCreator = it
            },
            onDismissRequest = { isOpenCreatorFilter = false })
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    FilterScreen()
}