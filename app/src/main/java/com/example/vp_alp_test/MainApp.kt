package com.example.vp_alp_test

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vp_alp_test.enum.PostEnum
import com.example.vp_alp_test.view.PostCreationView
import com.example.vp_alp_test.view.PostListView
import com.example.vp_alp_test.viewmodel.CommentViewModel
import com.example.vp_alp_test.viewmodel.LikeViewModel
import com.example.vp_alp_test.viewmodel.PostViewModel

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val postViewModel = PostViewModel()
    val likeViewModel = LikeViewModel()
    val commentViewModel = CommentViewModel()

    NavHost(
        navController = navController, startDestination = PostEnum.POST_LIST.route
    ) {
        composable(PostEnum.POST_LIST.route) {
            PostListView(
                postViewModel = postViewModel,
                likeViewModel = likeViewModel,
                commentViewModel = commentViewModel,
                navController = navController
            )
        }
        composable(PostEnum.POST_CREATION.route) {
            PostCreationView(
                onNavigationBack = {
                    navController.navigate(PostEnum.POST_LIST.route) {
                        popUpTo(PostEnum.POST_LIST.route) { inclusive = true }
                    }
                },
                onPostCreated = {
                    // First navigate back
                    navController.navigate(PostEnum.POST_LIST.route) {
                        popUpTo(PostEnum.POST_LIST.route) { inclusive = true }
                    }
                    // Then ensure we reload posts after a slight delay to allow navigation to complete
                    postViewModel.loadPosts()
                },
                viewModel = postViewModel  // Pass the shared viewModel instance
            )
        }
    }
}