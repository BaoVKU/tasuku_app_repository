package com.example.tasuku.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.ui.navigation.ForgotPasswordDestination
import com.example.tasuku.ui.navigation.HomeDestination
import com.example.tasuku.ui.navigation.LoginDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.RegisterDestination
import com.example.tasuku.ui.theme.TasukuTheme
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.LoginUiState
import com.example.tasuku.ui.viewmodels.LoginViewModel
import com.example.tasuku.ui.viewmodels.UserUiState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigate: (NavigationDestination) -> Unit = {}
) {
    val loginUiState by viewModel.uiState.collectAsState()
    if (loginUiState is LoginUiState.Success) {
        onNavigate(HomeDestination)
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .imePadding()
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(id = LoginDestination.titleRes),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = viewModel.loginFormState.email,
                    onValueChange = {
                        viewModel.loginFormState = viewModel.loginFormState.copy(email = it)
                    },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.loginFormState.password,
                    onValueChange = {
                        viewModel.loginFormState = viewModel.loginFormState.copy(password = it)
                    },
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    supportingText = {
                        if (loginUiState is LoginUiState.Error) {
                            Text(
                                text = (loginUiState as LoginUiState.Error).message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.loginFormState.remember,
                            onCheckedChange = {
                                viewModel.loginFormState =
                                    viewModel.loginFormState.copy(remember = it)
                            })
                        Text(text = "Remember me", style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onNavigate(ForgotPasswordDestination) }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Don't have an account? Sign up",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            onNavigate(RegisterDestination)
                        }
                    )
                    Button(onClick = {
                        viewModel.login(
                            viewModel.loginFormState.email,
                            viewModel.loginFormState.password,
                            viewModel.loginFormState.remember
                        )
                    }) {
                        if (loginUiState is LoginUiState.Loading) {
                            Text(
                                text = "Logging in...",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        } else {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    TasukuTheme() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }
    }
}