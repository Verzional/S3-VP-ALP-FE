package com.example.vp_alp_test.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.service.PostService
import com.example.vp_alp_test.util.AppClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class PostRepository {
    private val service = AppClient.retrofit.create(PostService::class.java)

    suspend fun fetchPosts(): List<PostModel> = withContext(Dispatchers.IO) {
        val response = service.getPosts()
        if (response.status == "success") {
            response.data
        } else {
            emptyList()
        }
    }

    suspend fun addPost(
        post: PostModel, imageUri: Uri? = null, context: Context? = null
    ): PostModel = withContext(Dispatchers.IO) {
        if (imageUri != null && context != null) {
            // Get file details
            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"

            val file = createTempFileFromUri(context, imageUri, extension)

            val titlePart = post.title.toRequestBody("text/plain".toMediaTypeOrNull())
            val contentPart = post.content.toRequestBody("text/plain".toMediaTypeOrNull())
            val userIdPart = post.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val communityIdPart =
                post.communityId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData("image", "image.$extension", requestFile)

            val response = service.createPostWithImage(
                title = titlePart,
                content = contentPart,
                userId = userIdPart,
                communityId = communityIdPart,
                image = imagePart
            )

            file.delete()

            if (response.status == "success") {
                response.data
            } else {
                throw Exception("Failed to add post with image")
            }
        } else {
            val response = service.createPost(post)
            if (response.status == "success") {
                response.data
            } else {
                throw Exception("Failed to add post")
            }
        }
    }

    private fun createTempFileFromUri(context: Context, uri: Uri, extension: String): File {
        val inputStream =
            context.contentResolver.openInputStream(uri) ?: throw Exception("Failed to read file")

        val tempFile = File.createTempFile("upload", ".$extension", context.cacheDir)
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.use { input ->
                input.copyTo(outputStream)
            }
        }
        return tempFile
    }
}