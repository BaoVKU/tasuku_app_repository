package com.example.tasuku.ui.components.task

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.tasuku.R
import com.example.tasuku.ui.viewmodels.TaskUiState

data class TaskMode(val iconID: Int, val name: String, val mode: Int)

@Composable
fun TaskModeSpinner(
    modifier: Modifier = Modifier,
    isHomeScreen: Boolean = false,
    selectedMode: Int = 2,
    onModeChange: (Int) -> Unit = {}
) {
    val taskModeList = listOf(
        TaskMode(R.drawable.ic_public, "Public", 0),
        TaskMode(R.drawable.ic_group, "Member", 1),
        TaskMode(R.drawable.ic_private, "Private", 2)
    )
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(taskModeList[selectedMode])
    }
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val trailingIconID = if (isExpanded) {
        R.drawable.ic_arrow_up
    } else {
        R.drawable.ic_arrow_down
    }
    if(selectedMode != selectedItem.mode){
        selectedItem = taskModeList[selectedMode]
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedItem.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Mode") },
            enabled = !isHomeScreen,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = selectedItem.iconID),
                    contentDescription = "current_mode_icon"
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = trailingIconID),
                    contentDescription = "dropdown_icon",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable {
                            if (!isHomeScreen) {
                                isExpanded = true
                            }
                        })
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                }
        )
        DropdownMenu(
            expanded = isExpanded, onDismissRequest = { isExpanded = false },
            offset = DpOffset(0.dp, 0.dp),
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            taskModeList.forEach { taskMode ->
                DropdownMenuItem(
                    text = { Text(text = taskMode.name) },
                    onClick = {
                        selectedItem = taskMode
                        isExpanded = false
                        onModeChange(taskMode.mode)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = taskMode.iconID),
                            contentDescription = "task_mode_icon"
                        )
                    })
            }
        }
    }
}
