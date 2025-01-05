package com.example.vp_alp_test.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vp_alp_test.uiState.PostUIState
import com.example.vp_alp_test.viewmodel.CommentViewModel
import com.example.vp_alp_test.viewmodel.LikeViewModel
import com.example.vp_alp_test.viewmodel.PostViewModel

@Composable
fun PostView(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    likeViewModel: LikeViewModel,
    commentViewModel: CommentViewModel
) {
    val posts by postViewModel.posts.collectAsState()
    val uiState by postViewModel.postUIState.collectAsState()
    val userLikes by likeViewModel.userLikes.collectAsState()
    val postLikeCount by likeViewModel.postLikeCount.collectAsState()
    val postCommentCount by commentViewModel.postCommentCount.collectAsState()

    var selectedPostId by remember { mutableStateOf<Int?>(null) }

    // Initial load of posts
    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    // Load likes and comments whenever posts change
    LaunchedEffect(posts) {
        posts.forEach { post ->
            likeViewModel.loadPostLikes(post.id)
            commentViewModel.loadPostComments(post.id)
        }
    }

    // Maintain likes and comments state
    LaunchedEffect(selectedPostId) {
        selectedPostId?.let { postId ->
            // Refresh counts when comment overlay is opened
            likeViewModel.loadPostLikes(postId)
            commentViewModel.loadPostComments(postId)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is PostUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is PostUIState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = (uiState as PostUIState.Success).posts,
                        key = { post -> post.id }
                    ) { post ->
                        PostCard(
                            post = post,
                            isLiked = userLikes.contains(post.id),
                            likeCount = postLikeCount[post.id] ?: 0,
                            commentCount = postCommentCount[post.id] ?: 0,
                            onLikeClick = { likeViewModel.toggleLike(post.id) },
                            onCommentClick = { selectedPostId = post.id }
                        )
                    }
                }
            }

            is PostUIState.Failed -> {
                Text(
                    text = (uiState as PostUIState.Failed).errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .wrapContentSize(Alignment.Center)
                )
            }

            PostUIState.Start -> {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

        selectedPostId?.let { postId ->
            CommentOverlay(
                postId = postId,
                viewModel = commentViewModel,
                onClose = {
                    // Refresh counts when comment overlay is closed
                    likeViewModel.loadPostLikes(postId)
                    commentViewModel.loadPostComments(postId)
                    selectedPostId = null
                }
            )
        }
    }
}