package com.example.tasuku

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.tasuku.model.TaskMember
import com.google.accompanist.systemuicontroller.SystemUiController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun SystemUiController.setSystemBarColor(color: Color) {
    setStatusBarColor(color = color,
        darkIcons = !isSystemInDarkTheme()
    )
    setNavigationBarColor(color = color)
}

fun calculateTaskCompletion(taskMembers: List<TaskMember>): Float {
    val completedMembers = taskMembers.filter { it.isCompleted == 1 }.size
    val totalMembers = taskMembers.size

    return if (totalMembers > 0) {
        (completedMembers.toFloat() / totalMembers) * 100
    } else {
        0.0f
    }
}

fun dateTimeToHhMmDdMmYyyy(date: String): String {
    if(date.isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

    val inputDate = inputFormat.parse(date)
    val outputDate = outputFormat.format(inputDate!!)
    return  outputDate
}

fun dateToDdMmYyyy(date: String): String {
    if(date.isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val inputDate = inputFormat.parse(date)
    val outputDate = outputFormat.format(inputDate!!)
    return  outputDate
}

fun getHumanReadableDate(dateString: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    val currentTime = System.currentTimeMillis()
    val gapInMillis = currentTime - date.time
    val gapInSeconds = gapInMillis / 1000

    return when {
        gapInSeconds < 60 -> "just now"
        gapInSeconds < 60 * 60 -> "${gapInSeconds / 60} minutes ago"
        gapInSeconds < 60 * 60 * 24 -> "${gapInSeconds / (60 * 60)} hours ago"
        else -> "${gapInSeconds / (60 * 60 * 24)} days ago"
    }
}

fun getPathFromUri(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
    }
    return null
}