package com.example.vp_alp_test.view

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CreateCommunityDone(onNavigateToHome: () -> Unit) {
    var animationState by remember { mutableStateOf(0f) }

    // Animate scale of the check icon
    val scale by animateFloatAsState(
        targetValue = if (animationState == 1f) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1500)
    )

    // Trigger navigation after delay
    LaunchedEffect(Unit) {
        animationState = 1f
        delay(3000)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C134F)), // Dark blue background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success Icon",
                tint = Color(0xFF00FF00), // Green check icon
                modifier = Modifier
                    .size(100.dp * scale)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Successful!",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "New Community Has Been Created",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
