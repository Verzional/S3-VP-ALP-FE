package com.example.vp_alp_test.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vp_alp_test.R
import com.example.vp_alp_test.model.UserModel

@Composable
fun HomeScreen(user: UserModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E))
            .padding(16.dp)
    ) {
        CustomSearchBar(user = user)

        Text(text ="HI"+ user.username)
    }

}

@Composable
fun CustomSearchBar(user: UserModel) {
    var searchQuery by remember { mutableStateOf("") }
    val avatar = user.avatar.orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text("Search", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (avatar.isNotEmpty()) {
                AsyncImage(
                    model = avatar,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = "Default Avatar",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun PopularCommunities(){

}

@Composable
@Preview
fun AppPreview() {
    val sampleUser = UserModel(
        id = 1,
        username = "JohnDoe",
        email = "johndoe@example.com",
        avatar = "", // Empty avatar for preview
        bio = "This is a sample bio.",
        createdAt = "2024-01-01",
        updatedAt = "2025-01-01",
        token = null
    )
    HomeScreen(user = sampleUser)
}
