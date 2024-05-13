package com.example.tasuku.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasuku.ui.navigation.ForgotPasswordDestination
import com.example.tasuku.ui.navigation.LoginDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.theme.TasukuTheme

@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier, onNavigate: (NavigationDestination) -> Unit = {}, onNavigateBack: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = stringResource(id = ForgotPasswordDestination.titleRes), style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = "Forgot Password? No problem. Just let us know your email address and we will email you a password reset link that will allow you to choose a new one.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()){
                    TextButton(onClick = onNavigateBack) {
                        Text(text = "Cancel", style = MaterialTheme.typography.bodyMedium)
                    }
                    Button(onClick = { onNavigate(LoginDestination) }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(text = "Send Reset Link")
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    TasukuTheme() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ForgotPasswordScreen()
        }
    }
}