package com.example.tasuku.ui.components.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FilterRadioGroup(
    filterList: List<String>,
    selectedFilter: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier) {
        filterList.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedFilter == item,
                    onClick = { onClick(item) })
                Text(text = item)
            }
        }
    }
}