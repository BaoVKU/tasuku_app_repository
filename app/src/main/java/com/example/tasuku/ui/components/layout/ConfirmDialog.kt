package com.example.tasuku.ui.components.layout

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    confirmText: String = "Are you sure?",
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "No")
            }
        },
        title = { Text(text = "Confirmation") },
        text = { Text(text = confirmText) }, modifier = modifier
    )
}