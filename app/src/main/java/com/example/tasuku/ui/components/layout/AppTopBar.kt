package com.example.tasuku.ui.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.NotificationDestination
import com.example.tasuku.ui.navigation.ProfileScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    userAvatar: String = "",
    onNavigate: (NavigationDestination) -> Unit = {},
) {
    val context = LocalContext.current
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onNavigate(NotificationDestination) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "top_bar_notification_icon",
                    modifier = Modifier.rotate(45f)
                )
            }
            IconButton(onClick = { onNavigate(ProfileScreenDestination) }) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(userAvatar).crossfade(true).build(),
                    contentDescription = "user_avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(0.7f).clip(shape = CircleShape)
                )
            }
        }
    }, modifier = modifier)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

        }, modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    onNavigateBack: () -> Unit = {},
    trailingActions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotEmpty()) {
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "arrow_back_icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = trailingActions, modifier = modifier
    )
}

@Preview
@Composable
fun HomeTopBarPreview() {
    HomeTopBar()
}

@Preview
@Composable
fun PageTopBarPreview() {
    PageTopBar(title = "Page Title") {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "more_icon"
            )
        }
    }
}