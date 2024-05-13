package com.example.tasuku.ui.components.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import com.example.tasuku.R

@Composable
fun GroupJoinDialog(
    onDismissRequest: () -> Unit,
    keyValue: String = "",
    onKeyValueChange: (String) -> Unit = {},
    onJoinClick: () -> Unit = {}
) {
    var inputValue by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
            ) {
                OutlinedTextField(
                    value = keyValue,
                    onValueChange =onKeyValueChange,
                    label = { Text("Join key") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "clear_icon",
                            modifier = Modifier.clickable { inputValue = "" }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(modifier = Modifier.padding(top = 8.dp))
                TextButton(onClick = onJoinClick) {
                    Text(text = "Join")
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupJoinDialogPreview() {
    GroupJoinDialog(onDismissRequest = {})
}