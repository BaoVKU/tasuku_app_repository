package com.example.tasuku.ui.components.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tasuku.R

@Composable
fun GroupCreateDialog(
    onDismissRequest: () -> Unit,
    nameValue: String = "",
    descriptionValue: String = "",
    onNameValueChange: (String) -> Unit = {},
    onDescriptionValueChange: (String) -> Unit = {},
    onCreateClick: () -> Unit = {}
    ) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
            ) {
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = onNameValueChange,
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionValue,
                    onValueChange = onDescriptionValueChange,
                    label = { Text(text = "Description") },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(modifier = Modifier.padding(top = 8.dp))
                TextButton(onClick = onCreateClick) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupCreateDialogPreview() {
    GroupCreateDialog(onDismissRequest = {})
}