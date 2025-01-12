package com.example.vp_alp_test.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_alp_test.viewmodel.CommunityViewModel

@Composable
fun CommunityCreation(
    onNavigationBack: () -> Unit,
    onCommunityCreated: () -> Unit,
    viewModel: CommunityViewModel = viewModel()
) {
    val context = LocalContext.current
    var communityName by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var tags by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("") }
    val categories = listOf(
        "Anime", "Car", "Movie", "Food", "Games", "Fashion",
        "Horror", "Animal", "Travel", "K-Pop", "Sport", "Comics", "Music", "Beauty"
    )
    var expanded by remember { mutableStateOf(false) }
    val selectedFile by viewModel.selectedImage.collectAsState()

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setSelectedImage(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C134F)) // Dark blue background
            .padding(horizontal = 16.dp)
    ) {
        // Title "Create Community"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigationBack,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back Arrow Icon",
                    tint = Color.White
                )
            }

            Text(
                text = "Create Community",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Community Name Input
        Text("Community Name", color = Color.White)
        BasicTextField(
            value = communityName,
            onValueChange = { communityName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White.copy(alpha = 0.1f))
                .border(1.dp, Color.White)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (communityName.text.isEmpty()) {
                    Text("Enter community name", color = Color.Gray)
                }
                innerTextField()
            }
        )

        // Description Input
        Text("Description", color = Color.White)
        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(100.dp)
                .border(1.dp, Color.White)
                .background(Color.White.copy(alpha = 0.1f))
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (description.text.isEmpty()) {
                    Text("Enter description", color = Color.Gray)
                }
                innerTextField()
            }
        )

        // Tags Input
        Text("Tags", color = Color.White)
        BasicTextField(
            value = tags,
            onValueChange = { tags = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.White)
                .background(Color.White.copy(alpha = 0.1f))
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (tags.text.isEmpty()) {
                    Text("Enter tags (comma-separated)", color = Color.Gray)
                }
                innerTextField()
            }
        )

        // Category Dropdown
        Text("Category", color = Color.White)
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, Color.White)
            ) {
                Text(
                    text = selectedCategory.ifEmpty { "Select Category" },
                    color = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category, color = Color.Black) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Label
        Text("Profile", color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        // Attachment File Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = { filePicker.launch("*/*") }, // Allow selection of files
                modifier = Modifier
                    .background(Color(0xFFD4ADFC)) // Button color
                    .padding(horizontal = 16.dp)
            ) {
                Text("Attachment File", color = Color.White)
            }

            // Display Selected File Name (if any)
            selectedFile?.let { uri ->
                Text(
                    text = uri.lastPathSegment ?: "Selected file",
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        TextButton(
            onClick = {
                if (communityName.text.isNotEmpty() && description.text.isNotEmpty() && selectedCategory.isNotEmpty()) {
                    viewModel.createCommunity(
                        communityName.text,
                        description.text,
                        selectedCategory,
                        bio = "", // Replace with actual bio
                        context = context
                    )
                    onCommunityCreated() // Callback after creation
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD4ADFC)) // Button color
                .padding(vertical = 8.dp)
        ) {
            Text("Create Community", color = Color.White)
        }
    }
}

