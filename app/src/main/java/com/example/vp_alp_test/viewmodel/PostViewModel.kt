package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel(){
    private val postRepository = PostRepository()

    private val _posts = MutableStateFlow<List<PostModel>>(emptyList())
    val posts = _posts

    fun loadPosts() {
        viewModelScope.launch {
            _posts.value = postRepository.fetchPosts()
        }
    }

    fun createPost(post: PostModel){
        viewModelScope.launch {
            postRepository.addPost(post)
            loadPosts()
        }
    }
}