package com.example.vp_alp_test.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vp_alp_test.R
import com.example.vp_alp_test.model.UserModel
import com.example.vp_alp_test.uiState.ProfileUIState
import com.example.vp_alp_test.viewmodel.ProfileViewModel

@Composable
fun ProfileContent(
    profileViewModel: ProfileViewModel,
    token: String,
    id: Int,
    userModel: UserModel
) {
    val profileState by profileViewModel.profileState.collectAsState()
    val actionState by profileViewModel.actionState.collectAsState()
    val logoutStatus by profileViewModel.logoutStatus.observeAsState(initial = null)

    // Local states for profile data
    var username by remember { mutableStateOf(userModel.username) }
    var email by remember { mutableStateOf(userModel.email) }
    var avatar by remember { mutableStateOf(userModel.avatar ?: "") }
    var bio by remember { mutableStateOf(userModel.bio ?: "") }

    // Fetch profile data when component mounts
    LaunchedEffect(Unit) {
        profileViewModel.getUserProfile(token, id)
    }

    // Update local state when profile data is successfully fetched
    LaunchedEffect(profileState) {
        if (profileState is ProfileUIState.Success) {
            val userData = (profileState as ProfileUIState.Success).data
            username = userData.username
            email = userData.email
            avatar = userData.avatar ?: ""
            bio = userData.bio ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (profileState) {
            is ProfileUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    color = Color.White
                )
            }

            is ProfileUIState.Failed -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (profileState as ProfileUIState.Failed).errorMessage,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = { profileViewModel.getUserProfile(token, id) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Retry")
                    }
                }
            }

            is ProfileUIState.Success -> {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = avatar.ifEmpty { R.drawable.baseline_account_circle_24 },
                        contentDescription = "Avatar",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.baseline_account_circle_24),
                        error = painterResource(R.drawable.baseline_account_circle_24)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Fields
                ProfileField("Username", username, onValueChange = { username = it })
                Spacer(modifier = Modifier.height(8.dp))
                ProfileField("Email", email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(8.dp))
                ProfileField("Bio", bio, onValueChange = { bio = it }, isMultiline = true)
                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        profileViewModel.updateUserProfile(
                            token = token,
                            id = id,
                            username = username,
                            email = email,
                            avatar = avatar,
                            bio = bio
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Button(
                    onClick = { profileViewModel.logoutUser() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action state feedback
                when (actionState) {
                    is ProfileUIState.Loading -> {
                        CircularProgressIndicator(color = Color.White)
                    }

                    is ProfileUIState.Success -> {
                        Text(
                            text = "Profile updated successfully!",
                            color = Color.Green,
                            fontSize = 14.sp
                        )
                    }

                    is ProfileUIState.Failed -> {
                        Text(
                            text = (actionState as ProfileUIState.Failed).errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }

                    else -> {}
                }

                // Logout feedback
                logoutStatus?.let { result ->
                    Text(
                        text = if (result.isSuccess) {
                            "Logged out successfully!"
                        } else {
                            "Logout failed: ${result.exceptionOrNull()?.message}"
                        },
                        color = if (result.isSuccess) Color.Green else Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            else -> {
                // Initial state, showing loading
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isMultiline: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.small)
                .padding(8.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = !isMultiline
        )
    }
}
