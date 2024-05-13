package com.example.tasuku.ui.components.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tasuku.R

@Composable
fun CreatorFilterDialog(
    creatorList: List<String>,
    selectedCreator: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier.fillMaxHeight(0.6f),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Choose members",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(creatorList) { creator ->
                        CreatorFilterItem(
                            memberName = creator,
                            selected = creator == selectedCreator,
                            onClick = { onClick(creator) }
                        )
                    }
                }
                Divider()
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@Composable
fun CreatorFilterItem(
    modifier: Modifier = Modifier,
    memberName: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(8.dp)) {
        RadioButton(selected = selected, onClick = onClick)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.woman_avatar),
                contentDescription = "member_avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = memberName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}