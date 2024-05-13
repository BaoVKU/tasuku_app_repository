package com.example.tasuku.ui.components.task

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.tasuku.R
import java.time.LocalTime
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    timeValue: String,
    onTimeChange: (String) -> Unit = {}
) {
    var isOpenDialog by remember { mutableStateOf(false) }
    val timePickerState =
        rememberTimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)
    var selectedTime by remember { mutableStateOf(timeValue) }
    OutlinedTextField(value = if(timeValue!="hh:mm") timeValue else selectedTime, readOnly = true, trailingIcon = {
        Icon(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = "clock_icon",
            modifier = Modifier
                .clip(shape = CircleShape)
                .clickable {
                    isOpenDialog = true
                }
        )
    },
        label = { Text(text = label) }, onValueChange = {}, modifier = modifier
    )
    if (isOpenDialog) {
        Dialog(onDismissRequest = { isOpenDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    TimeInput(state = timePickerState)
                    TextButton(onClick = {
                        isOpenDialog = false
                        selectedTime = formatTime(timePickerState.hour, timePickerState.minute)
                        onTimeChange(selectedTime)
                    }, modifier = Modifier.align(Alignment.End)) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

private fun formatTime(hour: Int, minute: Int): String {
    var formattedMinute = minute.toString()
    var formattedHour = hour.toString()
    if (hour < 10) {
        formattedHour = "0$hour"
    }
    if (minute < 10) {
        formattedMinute = "0$minute"
    }
    return "$formattedHour:$formattedMinute"
}