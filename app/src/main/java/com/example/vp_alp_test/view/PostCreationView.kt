package com.example.vp_alp_test.view

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.ui.theme.BackgroundBlue
import com.example.vp_alp_test.util.PermissionHandler
import com.example.vp_alp_test.viewmodel.PostViewModel

@Composable
fun PostCreationView(
    onNavigationBack: () -> Unit,
    onPostCreated: () -> Unit,
    viewModel: PostViewModel = PostViewModel()
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val selectedImage by viewModel.selectedImage.collectAsState()
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Photo Picker launcher for Android 14+
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.setSelectedImage(uri)
    }

    // Legacy image picker launcher for older versions
    val legacyImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setSelectedImage(uri)
    }

    // Permission launcher for Android 13 and below
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            legacyImagePicker.launch("image/*")
        } else {
            showPermissionDialog = true
        }
    }

    BackHandler {
        onNavigationBack()
    }

    if (showPermissionDialog) {
        AlertDialog(onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Storage permission is required to select images. Please enable it in app settings.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("OK")
                }
            })
    }

    Scaffold(topBar = {
        Column {
            PostCreationTopAppBar(
                onNavigationIconClick = onNavigationBack,
                onAttachmentClick = {
                    when {
                        // Use Photo Picker for Android 14+
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        // Use legacy picker with permission check for older versions
                        else -> {
                            if (PermissionHandler.hasRequiredPermissions(context)) {
                                legacyImagePicker.launch("image/*")
                            } else {
                                permissionLauncher.launch(PermissionHandler.getRequiredPermissions())
                            }
                        }
                    }
                },
                onSubmitClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        val post = PostModel(
                            id = 0,
                            title = title,
                            content = content,
                            userId = 1,
                            communityId = 1
                        )
                        viewModel.createPost(post, context)
                        onPostCreated()
                    }
                })
            HorizontalDivider(thickness = 0.5.dp, color = Color.White)
        }
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            PostCreationForm(title = title,
                content = content,
                onTitleChange = { title = it },
                onContentChange = { content = it })

            // Show selected image preview
            selectedImage?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }, containerColor = BackgroundBlue
    )
}