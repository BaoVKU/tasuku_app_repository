package com.example.tasuku.ui.components.task


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.tasuku.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedDatePicker(
    modifier: Modifier = Modifier,
    label: String,
    dateValue: String,
    onDateChange: (String) -> Unit = {}
) {
    var isOpenDialog by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = Date().time)
    var formatSelectedDate by remember { mutableStateOf(dateValue) }
    OutlinedTextField(value = dateValue, readOnly = true, trailingIcon = {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = "calendar_icon",
            modifier = Modifier
                .clip(shape = CircleShape)
                .clickable { isOpenDialog = true }
        )
    },
        label = { Text(text = label) }, onValueChange = {}, modifier = modifier
    )
    if (isOpenDialog) {
        DatePickerDialog(onDismissRequest = { isOpenDialog = false }, confirmButton = {
            TextButton(
                onClick = {
                    isOpenDialog = false
                    formatSelectedDate =
                        datePickerState.selectedDateMillis?.let { formatDate(it) }.toString()
                    onDateChange(formatSelectedDate)
                }) {
                Text(text = "OK")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}


fun formatDate(selectedDateMillis: Long): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Date(selectedDateMillis)
    return format.format(date)
}