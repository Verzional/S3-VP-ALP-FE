package com.example.vp_alp_test.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.navigation.NavController
import com.example.vp_alp_test.enum.PostEnum
import com.example.vp_alp_test.ui.theme.BackgroundBlue
import com.example.vp_alp_test.ui.theme.ButtonPurple
import com.example.vp_alp_test.uiState.PostUIState
import com.example.vp_alp_test.viewmodel.CommentViewModel
import com.example.vp_alp_test.viewmodel.LikeViewModel
import com.example.vp_alp_test.viewmodel.PostViewModel

@Composable
fun PostListView(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    likeViewModel: LikeViewModel,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val posts by postViewModel.posts.collectAsState()
    val uiState by postViewModel.postUIState.collectAsState()
    val userLikes by likeViewModel.userLikes.collectAsState()
    val postLikeCount by likeViewModel.postLikeCount.collectAsState()
    val postCommentCount by commentViewModel.postCommentCount.collectAsState()

    var selectedPostId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    LaunchedEffect(posts) {
        posts.forEach { post ->
            likeViewModel.loadPostLikes(post.id)
            commentViewModel.loadPostComments(post.id)
        }
    }

    LaunchedEffect(selectedPostId) {
        selectedPostId?.let { postId ->
            likeViewModel.loadPostLikes(postId)
            commentViewModel.loadPostComments(postId)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundBlue)
    ) {
        when (uiState) {
            is PostUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is PostUIState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(
                            items = posts, key = { _, post -> post.id }) { index, post ->
                            PostListCard(post = post,
                                isLiked = userLikes.contains(post.id),
                                likeCount = postLikeCount[post.id] ?: 0,
                                commentCount = postCommentCount[post.id] ?: 0,
                                onLikeClick = { likeViewModel.toggleLike(post.id) },
                                onCommentClick = { selectedPostId = post.id })

                            if (index != posts.lastIndex) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    Spacer(Modifier.padding(vertical = 4.dp))
                                    HorizontalDivider(thickness = 0.5.dp, color = Color.White)
                                    Spacer(Modifier.padding(top = 4.dp, bottom = 8.dp))
                                }
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate(PostEnum.POST_CREATION.route) },
                    containerColor = ButtonPurple,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, contentDescription = "Create Post"
                    )
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
            CommentListOverlay(postId = postId, viewModel = commentViewModel, onClose = {
                likeViewModel.loadPostLikes(postId)
                commentViewModel.loadPostComments(postId)
                selectedPostId = null
            })
        }
    }
}