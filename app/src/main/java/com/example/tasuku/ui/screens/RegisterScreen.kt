package com.example.tasuku.ui.screens

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
import com.example.tasuku.ui.navigation.LoginDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.RegisterDestination
import com.example.tasuku.ui.theme.TasukuTheme
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.RegisterUiState
import com.example.tasuku.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigate: (NavigationDestination) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val registerUiState by viewModel.uiState.collectAsState()

    if (registerUiState is RegisterUiState.Success) {
        onNavigate(LoginDestination)
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
                    text = stringResource(id = RegisterDestination.titleRes),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = viewModel.registerFormState.name,
                    onValueChange = {
                        viewModel.registerFormState = viewModel.registerFormState.copy(name = it)
                    },
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.registerFormState.email,
                    onValueChange = {
                        viewModel.registerFormState = viewModel.registerFormState.copy(email = it)
                    },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.registerFormState.password,
                    onValueChange = {
                        viewModel.registerFormState =
                            viewModel.registerFormState.copy(password = it)
                    },
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.registerFormState.passwordConfirmation,
                    onValueChange = {
                        viewModel.registerFormState =
                            viewModel.registerFormState.copy(passwordConfirmation = it)
                    },
                    label = { Text(text = "Confirm password") },
                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    supportingText = {
                        if (registerUiState is RegisterUiState.Error) {
                            Text(
                                text = (registerUiState as RegisterUiState.Error).message,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onNavigateBack() }
                    )
                    Button(onClick = {
                        viewModel.register(
                            viewModel.registerFormState.name,
                            viewModel.registerFormState.email,
                            viewModel.registerFormState.password,
                            viewModel.registerFormState.passwordConfirmation
                        )
                    }) {
                        if (registerUiState is RegisterUiState.Loading) {
                            Text(text = "Registering...")
                        } else {
                            Text(text = "Register")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    TasukuTheme() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RegisterScreen()
        }
    }
}