package com.example.vp_alp_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_alp_test.ui.theme.VP_ALP_TestTheme
import com.example.vp_alp_test.view.PostView
import com.example.vp_alp_test.viewmodel.CommentViewModel
import com.example.vp_alp_test.viewmodel.LikeViewModel
import com.example.vp_alp_test.viewmodel.PostViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VP_ALP_TestTheme {
                val postViewModel = viewModel<PostViewModel>()
                val likeViewModel = viewModel<LikeViewModel>()
                val commentViewModel = viewModel<CommentViewModel>()

                PostView(
                    postViewModel = postViewModel,
                    likeViewModel = likeViewModel,
                    commentViewModel = commentViewModel
                )
            }
        }
    }
}