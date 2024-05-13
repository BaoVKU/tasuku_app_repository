package com.example.tasuku.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tasuku.R
import com.example.tasuku.data.BaseUrl
import com.example.tasuku.ui.components.layout.ConfirmDialog
import com.example.tasuku.ui.components.layout.PageTopBar
import com.example.tasuku.ui.components.task.DockedDatePicker
import com.example.tasuku.ui.navigation.LoginDestination
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.ProfileViewModel
import com.example.tasuku.ui.viewmodels.UserProfileFormState
import com.example.tasuku.ui.viewmodels.UserUiState

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val userUiState by viewModel.userUiState.collectAsState()
    var isOpenLogoutConfirmDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            PageTopBar(title = "Profile",
                onNavigateBack = onNavigateBack,
                trailingActions = {
                    IconButton(onClick = {
                        isOpenLogoutConfirmDialog = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "logout_icon",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                })
        }, modifier = modifier
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Information", style = MaterialTheme.typography.titleLarge)

            when (userUiState) {
                is UserUiState.Loading -> {
                    Text(text = "Loading user information")
                }

                is UserUiState.Error -> {
                    Text(text = "Error loading user information")
                }

                is UserUiState.Success -> {
                    val user = (userUiState as UserUiState.Success).data

                    var pickedAvatar by remember{ mutableStateOf(Uri.EMPTY)}
                    val getContent =
                        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                            pickedAvatar = uri
                        }
                    Box(contentAlignment = Alignment.BottomEnd) {
                        val initialUri = BaseUrl.URL + user.avatar
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context).data(
                                if(pickedAvatar != Uri.EMPTY) pickedAvatar else initialUri
                            ).crossfade(true).build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = "user_avatar",contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0f, 0f, 0f, 0.7f),
                                contentColor = Color.White
                            ),
                            onClick = { getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "change_avatar_icon"
                            )
                        }
                    }
                    OutlinedTextField(
                        value = viewModel.userProfileFormState.name,
                        onValueChange = {
                            viewModel.userProfileFormState =
                                viewModel.userProfileFormState.copy(name = it)
                        },
                        label = { Text(text = "Name") }, modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.userProfileFormState.email,
                        onValueChange = {
                            viewModel.userProfileFormState =
                                viewModel.userProfileFormState.copy(email = it)
                        },
                        label = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.userProfileFormState.phoneNumber,
                        onValueChange = {
                            viewModel.userProfileFormState =
                                viewModel.userProfileFormState.copy(phoneNumber = it)
                        },
                        label = { Text(text = "Phone number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DockedDatePicker(
                            label = "Birthday",
                            dateValue = viewModel.userProfileFormState.birthday,
                            onDateChange = {
                                viewModel.userProfileFormState =
                                    viewModel.userProfileFormState.copy(birthday = it)

                            },
                            modifier = Modifier.weight(1f)
                        )
                        ProfileGenderSpinner(
                            initialGender = viewModel.userProfileFormState.gender,
                            onGenderChange = { gender ->
                                viewModel.userProfileFormState = viewModel.userProfileFormState.copy(
                                    gender = gender
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = viewModel.userProfileFormState.address,
                        onValueChange = {
                            viewModel.userProfileFormState =
                                viewModel.userProfileFormState.copy(address = it)
                        },
                        label = { Text(text = "Address") }, modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.userProfileFormState.description,
                        onValueChange = {
                            viewModel.userProfileFormState =
                                viewModel.userProfileFormState.copy(description = it)
                        },
                        label = { Text(text = "Description") }, modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                                  viewModel.updateProfile(
                                        context = context,
                                        name = viewModel.userProfileFormState.name,
                                        email = viewModel.userProfileFormState.email,
                                        phoneNumber = viewModel.userProfileFormState.phoneNumber,
                                        birthday = viewModel.userProfileFormState.birthday,
                                        gender = viewModel.userProfileFormState.gender,
                                        address = viewModel.userProfileFormState.address,
                                        description = viewModel.userProfileFormState.description,
                                        uri = pickedAvatar
                                  )
                                  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "Save", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Divider()
            Text(text = "Update password", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = "......",
                onValueChange = {},
                label = { Text(text = "Current password") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "......",
                onValueChange = {},
                label = { Text(text = "New password") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "......",
                onValueChange = {},
                label = { Text(text = "Confirm password") }, modifier = Modifier.fillMaxWidth()
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp), onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Update", style = MaterialTheme.typography.titleMedium)
            }
            Divider()
            Text(text = "Delete account", style = MaterialTheme.typography.titleLarge)
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(8.dp), onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(48.dp)
            ) {
                Text(text = "Delete", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    if(isOpenLogoutConfirmDialog){
        ConfirmDialog(
            confirmText = "Are you sure you to logout?",
            onDismissRequest = { isOpenLogoutConfirmDialog = false },
            onConfirm = {
                isOpenLogoutConfirmDialog = false
                viewModel.logout(context)
                onNavigate(LoginDestination)
            }
        )
    }
}

@Composable
fun ProfileGenderSpinner(
    modifier: Modifier = Modifier,
    initialGender: Int = 0,
    onGenderChange: (Int) -> Unit
) {
    val genderList = listOf("Male", "Female", "Other")
    var selectedGender by remember {
        mutableIntStateOf(initialGender)
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val trailingIconID = if (isExpanded) {
        R.drawable.ic_arrow_up
    } else {
        R.drawable.ic_arrow_down
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = genderList[selectedGender],
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Gender") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = trailingIconID),
                    contentDescription = "dropdown_icon",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable {
                            isExpanded = true
                        })
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                }
        )
        DropdownMenu(
            expanded = isExpanded, onDismissRequest = { isExpanded = false },
            offset = DpOffset(0.dp, 0.dp),
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            genderList.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender) },
                    onClick = {
                        selectedGender = genderList.indexOf(gender)
                        onGenderChange(selectedGender)
                        isExpanded = false
                    })
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onNavigate = {})
}