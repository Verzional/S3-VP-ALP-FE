package com.example.vp_alp_test.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.repository.PostRepository
import com.example.vp_alp_test.uiState.PostUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableStateFlow<List<PostModel>>(emptyList())
    val posts = _posts

    private val _selectedImage = MutableStateFlow<Uri?>(null)
    val selectedImage = _selectedImage

    private val _postUIState = MutableStateFlow<PostUIState>(PostUIState.Start)
    val postUIState = _postUIState

    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    fun loadPosts() {
        viewModelScope.launch {
            _postUIState.value = PostUIState.Loading
            try {
                val fetchedPosts = repository.fetchPosts()
                _posts.value = fetchedPosts  // Update the posts flow
                _postUIState.value = PostUIState.Success(fetchedPosts)
            } catch (e: Exception) {
                _postUIState.value = PostUIState.Failed("Failed to load posts: ${e.message}")
            }
        }
    }

    fun createPost(post: PostModel, context: Context) {
        viewModelScope.launch {
            try {
                _postUIState.value = PostUIState.Loading
                repository.addPost(post, _selectedImage.value, context)
                _selectedImage.value = null
                loadPosts()
            } catch (e: Exception) {
                _postUIState.value = PostUIState.Failed("Failed to create post: ${e.message}")
            }
        }
    }
}