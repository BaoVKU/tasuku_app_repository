package com.example.tasuku.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.model.GroupMember
import com.example.tasuku.model.TaskResponse
import com.example.tasuku.ui.components.filter.FilterRadioGroup
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.components.task.DockedDatePicker
import com.example.tasuku.ui.components.task.TaskMemberDialog
import com.example.tasuku.ui.components.task.TaskModeSpinner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FilterScreen(
    isHomeScreen: Boolean = false,
    taskList: List<TaskResponse>,
    groupMemberList: List<GroupMember> = listOf(),
    onDismissRequest: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onSubmitFilterClick: (List<TaskResponse>) -> Unit,
) {
    var isOpenMemberDialog by remember { mutableStateOf(false) }

    var creatorFilter by remember {
        mutableStateOf("")
    }

    var modeFilter by remember {
        mutableIntStateOf(2)
    }

    var checkedMemberList by remember {
        mutableStateOf(
            groupMemberList.map {
                it.id to false
            }
        )
    }

    var selectedDateFilter by remember {
        mutableStateOf("dd/mm/yyyy")
    }

    var selectedDateOptionFilter by remember {
        mutableStateOf("")
    }
    val dateOptionFilterList = listOf("During", "Coming", "Expired")

    var selectedStatusFilter by remember {
        mutableStateOf("")
    }
    val statusFilterList = listOf("Working", "Done")

    Scaffold(
        topBar = {
            PageTopBar(title = "Filter",
                onNavigateBack = onDismissRequest,
                trailingActions = {
                    IconButton(onClick = onResetClick) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "reset_icon")
                    }
                })
        }, modifier = Modifier.fillMaxSize()
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
                value = creatorFilter,
                onValueChange = { creatorFilterValue ->
                    creatorFilter = creatorFilterValue
                },
                enabled = !isHomeScreen,
                label = {
                    Text(
                        text = "Creator"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            TaskModeSpinner(
                isHomeScreen = isHomeScreen,
                selectedMode = modeFilter,
                onModeChange = { mode ->
                    modeFilter = mode
                },
            )
            OutlinedButton(
                shape = RoundedCornerShape(4.dp),
                enabled = !isHomeScreen,
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
            DockedDatePicker(
                label = "At",
                dateValue = selectedDateFilter,
                onDateChange = { date ->
                    selectedDateFilter = date
                },
                modifier = Modifier.fillMaxWidth()
            )
            FilterRadioGroup(
                filterList = dateOptionFilterList,
                selectedFilter = selectedDateOptionFilter,
                onClick = { date ->
                    selectedDateOptionFilter = date
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
                onClick = {
                    val membersFilter = checkedMemberList.filter { filterPair ->
                        filterPair.second
                    }.map { mapPair ->
                        mapPair.first
                    }
                    onSubmitFilterClick(
                        filterTasks(
                            taskList = taskList,
                            creatorFilter = creatorFilter,
                            modeFilter = modeFilter,
                            membersFilter = membersFilter,
                            dateFilter = selectedDateFilter,
                            dateOptionFilter = selectedDateOptionFilter,
                            statusFilter = selectedStatusFilter
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Apply filter", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    if (isOpenMemberDialog) {
        TaskMemberDialog(
            checkedMemberList = checkedMemberList,
            memberList = groupMemberList,
            onCheckedChanged = { id, isChecked ->
                val newCheckList = checkedMemberList.toMutableList()
                val index = newCheckList.indexOfFirst { it.first == id }
                newCheckList[index] = Pair(id, isChecked)
                checkedMemberList = newCheckList
                Log.e("checkedMemberList", checkedMemberList.toString())
            },
            onDismissRequest = { isOpenMemberDialog = false }
        )
    }
}

fun filterTasks(
    taskList: List<TaskResponse>,
    creatorFilter: String = "",
    modeFilter: Int = 2,
    membersFilter: List<Int> = listOf(),
    dateFilter: String = "dd/mm/yyyy",
    dateOptionFilter: String = "",
    statusFilter: String = ""
): List<TaskResponse> {
    var list = taskList

    if (creatorFilter.isNotEmpty()) {
        list = list.filter { taskResponse ->
            taskResponse.task.creatorName.contains(creatorFilter)
        }
    }

    list = list.filter { taskResponse ->
        taskResponse.task.mode == modeFilter
    }

    if(membersFilter.isNotEmpty()) {
        list = list.filter { taskResponse ->
            membersFilter.all {
                it in taskResponse.members.map { member -> member.id }
            }
        }
    }

    if (dateFilter != "dd/mm/yyyy") {
        list = list.filter { taskResponse ->
            checkDateRangeWithGivenDay(
                dateFilter,
                taskResponse.task.start,
                taskResponse.task.end
            )
        }
    }

    if (dateOptionFilter.isNotEmpty()) {
        list = list.filter { taskResponse ->
            checkDateRangeWithToDay(
                dateOptionFilter,
                taskResponse.task.start,
                taskResponse.task.end
            )
        }
    }

    if(statusFilter.isNotEmpty()) {
        if(statusFilter == "Working") {
            list = list.filter { taskResponse ->
                if(taskResponse.members.size == 1) return@filter false
                taskResponse.members.any { member ->
                    member.isCompleted == 1
                }
            }
        }else if(statusFilter == "Done") {
            list = list.filter { taskResponse ->
                taskResponse.members.all { member ->
                    member.isCompleted == 1
                }
            }
        }
    }

    return list
}

fun checkDateRangeWithGivenDay(
    givenDateStr: String,
    startDateStr: String,
    endDateStr: String
): Boolean {
    val givenFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val rangeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val givenDate = givenFormat.parse(givenDateStr)
    val startDate = rangeFormat.parse(startDateStr)
    val endDate = rangeFormat.parse(endDateStr)
    return givenDate in startDate..endDate
}

fun checkDateRangeWithToDay(option: String, startDateStr: String, endDateStr: String): Boolean {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val startDate = format.parse(startDateStr)
    val endDate = format.parse(endDateStr)
    val currentDate = Date()
    when (option) {
        "During" -> {
            return (currentDate in startDate..endDate)
        }

        "Coming" -> {
            return (currentDate.before(startDate))
        }

        "Expired" -> {
            return (currentDate.after(endDate))
        }
    }
    return false
}

@Preview
@Composable
fun FilterScreenPreview() {
    FilterScreen(taskList = listOf()) {}
}