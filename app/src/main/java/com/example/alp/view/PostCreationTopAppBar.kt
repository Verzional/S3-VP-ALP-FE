package com.example.alp.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.alp.R
import com.example.alp.ui.theme.BackgroundBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationTopAppBar(
    onNavigationIconClick: () -> Unit, onAttachmentClick: () -> Unit, onSubmitClick: () -> Unit
) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = BackgroundBlue,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White,
        actionIconContentColor = Color.White
    ), navigationIcon = {
        IconButton(onClick = onNavigationIconClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back Arrow Icon"
            )
        }
    }, title = { Text("New Post") }, actions = {
        IconButton(onClick = onAttachmentClick) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_attachment_24),
                contentDescription = "Attachment Icon"
            )
        }
        IconButton(onClick = onSubmitClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Submit Icon"
            )
        }
    })
}