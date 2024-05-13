package com.example.tasuku.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.model.Attachment
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.components.task.DockedDatePicker
import com.example.tasuku.ui.components.task.DockedTimePicker
import com.example.tasuku.ui.components.task.TaskMemberDialog
import com.example.tasuku.ui.components.task.TaskModeSpinner
import com.example.tasuku.ui.viewmodels.AddTaskUiState
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.GroupMembersUiState
import com.example.tasuku.ui.viewmodels.TaskAddAndUpdateViewModel
import com.example.tasuku.ui.viewmodels.TaskUiState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskAddUpdateScreen(
    editingTaskId: Int? = null,
    isHomeScreen: Boolean = false,
    viewModel: TaskAddAndUpdateViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var isOpenMemberDialog by remember { mutableStateOf(false) }
    val groupMembersUiState by viewModel.groupMembersUiState.collectAsState()
    val addTaskUiState by viewModel.addTaskUiState.collectAsState()
    val taskUiState by viewModel.taskUiState.collectAsState()
    var pickedImageList by remember {
        mutableStateOf(listOf<Uri>())
    }
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            pickedImageList = uris
        }
    var receivedAttachmentList by remember {
        mutableStateOf(listOf<Attachment>())
    }

    if (editingTaskId != null && taskUiState is TaskUiState.Success) {
        val taskResponse = (taskUiState as TaskUiState.Success).data
        val fromStart = divideDateTimeString(taskResponse.task.start)
        val toEnd = divideDateTimeString(taskResponse.task.end)
        val checkedMemberList =
            viewModel.taskAddAndUpdateFormState.checkedMemberList.map { oldPair ->
                val newPair = taskResponse.members.find { member -> member.id == oldPair.first }
                if (newPair != null) {
                    Pair(newPair.id, true)
                } else {
                    Pair(oldPair.first, false)
                }
            }
        viewModel.taskAddAndUpdateFormState = viewModel.taskAddAndUpdateFormState.copy(
            mode = taskResponse.task.mode,
            from = fromStart[0],
            to = toEnd[0],
            start = fromStart[1],
            end = toEnd[1],
            title = taskResponse.task.title,
            description = taskResponse.task.description,
            checkedMemberList = checkedMemberList
        )
        receivedAttachmentList = taskResponse.attachments
        viewModel.setTaskUiStateToLoading()
    }
    Scaffold(
        topBar = {
            PageTopBar(title = if (editingTaskId != null) "Edit task" else "Add task",
                onNavigateBack = onNavigateBack,
                trailingActions = {
                    if (editingTaskId == null) {
                        IconButton(onClick = {
                            viewModel.createTask(
                                context = context,
                                mode = viewModel.taskAddAndUpdateFormState.mode,
                                from = viewModel.taskAddAndUpdateFormState.from,
                                to = viewModel.taskAddAndUpdateFormState.to,
                                start = viewModel.taskAddAndUpdateFormState.start,
                                end = viewModel.taskAddAndUpdateFormState.end,
                                title = viewModel.taskAddAndUpdateFormState.title,
                                description = viewModel.taskAddAndUpdateFormState.description,
                                checkedMemberList = viewModel.taskAddAndUpdateFormState.checkedMemberList,
                                uris = pickedImageList
                            )
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "add_task_icon"
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            viewModel.updateTask(
                                context = context,
                                taskId = editingTaskId,
                                mode = viewModel.taskAddAndUpdateFormState.mode,
                                from = viewModel.taskAddAndUpdateFormState.from,
                                to = viewModel.taskAddAndUpdateFormState.to,
                                start = viewModel.taskAddAndUpdateFormState.start,
                                end = viewModel.taskAddAndUpdateFormState.end,
                                title = viewModel.taskAddAndUpdateFormState.title,
                                description = viewModel.taskAddAndUpdateFormState.description,
                                checkedMemberList = viewModel.taskAddAndUpdateFormState.checkedMemberList,
                                uris = pickedImageList
                            )
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_done_filled),
                                contentDescription = "save_task_icon"
                            )
                        }
                    }

                })
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TaskModeSpinner(
                    isHomeScreen = isHomeScreen,
                    selectedMode = viewModel.taskAddAndUpdateFormState.mode,
                    onModeChange = { mode ->
                        viewModel.taskAddAndUpdateFormState =
                            viewModel.taskAddAndUpdateFormState.copy(mode = mode)
                    },
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    shape = RoundedCornerShape(4.dp),
                    onClick = { isOpenMemberDialog = true },
                    enabled = !isHomeScreen,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(text = "Choose members")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DockedDatePicker(
                    label = "From",
                    dateValue = viewModel.taskAddAndUpdateFormState.from,
                    onDateChange = { date ->
                        viewModel.taskAddAndUpdateFormState =
                            viewModel.taskAddAndUpdateFormState.copy(from = date)
                    },
                    modifier = Modifier.weight(1f)
                )
                DockedTimePicker(
                    label = "Start",
                    timeValue = viewModel.taskAddAndUpdateFormState.start,
                    onTimeChange = { time ->
                        viewModel.taskAddAndUpdateFormState =
                            viewModel.taskAddAndUpdateFormState.copy(start = time)
                    }, modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DockedDatePicker(
                    label = "To",
                    dateValue = viewModel.taskAddAndUpdateFormState.to,
                    onDateChange = { date ->
                        viewModel.taskAddAndUpdateFormState =
                            viewModel.taskAddAndUpdateFormState.copy(to = date)
                    },
                    modifier = Modifier.weight(1f)
                )
                DockedTimePicker(
                    label = "End",
                    timeValue = viewModel.taskAddAndUpdateFormState.end,
                    onTimeChange = { time ->
                        viewModel.taskAddAndUpdateFormState =
                            viewModel.taskAddAndUpdateFormState.copy(end = time)
                    }, modifier = Modifier.weight(1f)
                )
            }
            OutlinedButton(
                shape = RoundedCornerShape(4.dp),
                onClick = { getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = "upload_icon"
                )
                Text(text = "Upload file", modifier = Modifier.padding(start = 8.dp))
            }
            if (receivedAttachmentList.isNotEmpty()) {
                val images =
                    receivedAttachmentList.filter { attachment -> attachment.type.contains("image") }
                val files =
                    receivedAttachmentList.filter { attachment -> !attachment.type.contains("image") }
                files.forEach { file ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        IconButton(onClick = {
                            receivedAttachmentList =
                                receivedAttachmentList.filter { attachment -> attachment != file }
                            viewModel.deleteAttachment(file.attachmentId)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = "file_delete_icon"
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_file),
                            contentDescription = "file_icon"
                        )
                        Text(
                            text = file.name + file.extension,
                            style = MaterialTheme.typography.bodyMedium,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
                images.forEach { image ->
                    Box(
                        contentAlignment = Alignment.TopEnd, modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(BaseUrl.URL + image.url)
                                .crossfade(true).build(),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(1f, 1f, 1f, 0.6f),
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            onClick = {
                                receivedAttachmentList =
                                    receivedAttachmentList.filter { attachment -> attachment != image }
                                viewModel.deleteAttachment(image.attachmentId)
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = "delete_image_icon",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
            if (pickedImageList.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pickedImageList.forEach { uri ->
                        Box(
                            contentAlignment = Alignment.TopEnd, modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(8.dp))
                        ) {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context).data(uri).build()
                            )
                            Image(
                                painter = painter,
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color(1f, 1f, 1f, 0.6f),
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = {
                                    pickedImageList =
                                        pickedImageList.filter { pickedUri -> pickedUri != uri }
                                }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clear),
                                    contentDescription = "delete_image_icon",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
            OutlinedTextField(
                value = viewModel.taskAddAndUpdateFormState.title,
                onValueChange = { value ->
                    viewModel.taskAddAndUpdateFormState =
                        viewModel.taskAddAndUpdateFormState.copy(title = value)
                },
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.taskAddAndUpdateFormState.description,
                onValueChange = { value ->
                    viewModel.taskAddAndUpdateFormState =
                        viewModel.taskAddAndUpdateFormState.copy(description = value)
                },
                label = { Text(text = "Description") },
                minLines = 7,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
    if (isOpenMemberDialog) {
        TaskMemberDialog(
            onDismissRequest = { isOpenMemberDialog = false },
            memberList = (groupMembersUiState as GroupMembersUiState.Success).data,
            checkedMemberList = viewModel.taskAddAndUpdateFormState.checkedMemberList,
            onCheckedChanged = { id, isChecked ->
                viewModel.updateCheckedMemberList(id, isChecked)
                Log.e(
                    "TaskAddUpdateScreen",
                    viewModel.taskAddAndUpdateFormState.checkedMemberList.toString()
                )
            }
        )
    }
    if (addTaskUiState is AddTaskUiState.Success) {
        onNavigateBack()
    }
}

fun divideDateTimeString(dateTime: String): Array<String> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = inputFormat.parse(dateTime)
    val formattedDateTime = outputFormat.format(date!!)
    return formattedDateTime.split(" ").toTypedArray()
}