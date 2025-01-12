package com.example.vp_alp_test.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_alp_test.R
import com.example.vp_alp_test.uiState.FriendUIState
import com.example.vp_alp_test.viewmodel.FriendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendView(viewModel: FriendViewModel = viewModel()) {
    // Observasi state dari ViewModel
    val friendState by viewModel.friendState.collectAsState()

    // Tampilan utama
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121B4C)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Friend Recommendation",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tampilan berdasarkan FriendUIState
        when (friendState) {
            is FriendUIState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            is FriendUIState.Success -> {
                val friends = (friendState as FriendUIState.Success).friends
                // Menampilkan daftar teman
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    friends.take(3).forEach { friend ->
                        FriendItem(
                            friend = FriendUI(
                                id = friend.id,
                                name = friend.friend.username,
                                isFollowing = false, // Sesuaikan status follow berdasarkan data
                                avatar = R.drawable.baseline_account_circle_24   // Ganti dengan data avatar yang relevan
                            ),
                            onFollowClicked = { isFollowing ->
                                if (isFollowing) {
                                    viewModel.removeFriend("token", friend.userId, friend.friendId)
                                } else {
                                    viewModel.addFriend("token", friend.userId, friend.friendId)
                                }
                            }
                        )
                    }
                }
            }
            is FriendUIState.Failed -> {
                val errorMessage = (friendState as FriendUIState.Failed).errorMessage
                Text(text = "Error: $errorMessage", color = Color.Red)
            }
            FriendUIState.Start -> {
                Text(text = "Start fetching data...", color = Color.White)
            }
        }
    }

    // Memicu data pertama kali
    LaunchedEffect(Unit) {
        viewModel.getFriends("token", userId = 1)
    }
}

@Composable
fun FriendItem(friend: FriendUI, onFollowClicked: (Boolean) -> Unit) {
    var isFollowing by remember { mutableStateOf(friend.isFollowing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Image(
            painter = painterResource(id = friend.avatar),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray, CircleShape)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name
        Text(
            text = friend.name,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        // Follow/Unfollow Button
        Button(
            onClick = {
                isFollowing = !isFollowing
                onFollowClicked(isFollowing)
            },
            colors = if (isFollowing) {
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            },
            modifier = if (isFollowing) {
                Modifier
                    .height(36.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
            } else {
                Modifier.height(36.dp)
            }
        ) {
            Text(if (isFollowing) "Unfollow" else "Follow")
        }
    }
}

// Dummy data model
data class FriendUI(val id: Int, val name: String, val isFollowing: Boolean, val avatar: Int)

// Dummy Data
val dummyFriends = listOf(
    FriendUI(id = 1, name = "John", isFollowing = false, avatar = R.drawable.baseline_account_circle_24),
    FriendUI(id = 2, name = "Bob", isFollowing = false, avatar = R.drawable.baseline_account_circle_24),
    FriendUI(id = 3, name = "Sasa", isFollowing = false, avatar = R.drawable.baseline_account_circle_24),
    FriendUI(id = 4, name = "Alice", isFollowing = false, avatar = R.drawable.baseline_account_circle_24)
)
