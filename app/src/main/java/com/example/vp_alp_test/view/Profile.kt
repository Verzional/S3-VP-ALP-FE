package com.example.vp_alp_test.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.vp_alp_test.R
import com.example.vp_alp_test.model.UserModel
import com.example.vp_alp_test.uiState.ProfileUIState
import com.example.vp_alp_test.viewmodel.ProfileViewModel

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    context: Context,
    token: String,
    userId: Int
) {
    // Fetch the user profile when this screen is launched
    LaunchedEffect(Unit) {
        if (token.isNotEmpty() && userId > 0) {
            profileViewModel.fetchUserProfile(token, userId)
        }
    }


    // Collect profile state from the ViewModel
    val profileState by profileViewModel.profileState.collectAsState()

    // Render the UI based on the current profile state
    when (profileState) {
        is ProfileUIState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        is ProfileUIState.Success -> {
            val user = (profileState as ProfileUIState.Success).data
            ProfileContent(
                user = user,
                onUpdate = { username, email, avatar, bio ->
                    profileViewModel.updateUserProfile(token, userId, username, email, avatar, bio)
                }
            )
        }
        is ProfileUIState.Failed -> {
            val errorMessage = (profileState as ProfileUIState.Failed).errorMessage
            Text(
                text = "Error: $errorMessage",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        else -> {
            Text(
                text = "No profile data available.",
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun ProfileContent(
    user: UserModel,
    onUpdate: (String, String, String?, String?) -> Unit
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var avatar by remember { mutableStateOf(user.avatar ?: "") }
    var bio by remember { mutableStateOf(user.bio ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E)) // Dark blue background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Display profile photo
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatar.ifEmpty { R.drawable.baseline_account_circle_24 }, // Placeholder if avatar is null
                contentDescription = "Avatar",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display followers and following info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Followers", color = Color.White, fontSize = 14.sp)
                Text("10", color = Color.White, fontSize = 16.sp) // Placeholder data for followers
            }
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Following", color = Color.White, fontSize = 14.sp)
                Text("5", color = Color.White, fontSize = 16.sp) // Placeholder data for following
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields for username, email, and bio
        ProfileField(
            label = "Username",
            value = username,
            onValueChange = { username = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileField(
            label = "Email",
            value = email,
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileField(
            label = "Avatar URL",
            value = avatar,
            onValueChange = { avatar = it },
            isMultiline = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileField(
            label = "Bio",
            value = bio,
            onValueChange = { bio = it },
            isMultiline = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Save button to trigger the update action
        Button(
            onClick = { onUpdate(username, email, avatar, bio) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB)) // Custom button color
        ) {
            Text(text = "Save", color = Color.White)
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
        Text(text = label, color = Color.White, fontSize = 14.sp)
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
