package com.example.alp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alp.enum.PostEnum
import com.example.alp.view.PostCreationView
import com.example.alp.view.PostListView
import com.example.alp.viewmodel.CommentViewModel
import com.example.alp.viewmodel.LikeViewModel
import com.example.alp.viewmodel.PostViewModel

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
            PostCreationView(onNavigationBack = {
                navController.navigate(PostEnum.POST_LIST.route) {
                    popUpTo(PostEnum.POST_LIST.route) { inclusive = true }
                }
            }, onPostCreated = {
                navController.navigate(PostEnum.POST_LIST.route) {
                    popUpTo(PostEnum.POST_LIST.route) { inclusive = true }
                }
                postViewModel.loadPosts()
            }, viewModel = postViewModel  // Pass the shared viewModel instance
            )
        }
    }
}