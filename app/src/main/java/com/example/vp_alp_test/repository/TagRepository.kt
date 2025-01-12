package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.util.AppClient
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.TagModel
import com.example.vp_alp_test.service.TagService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagRepository {
    private val tagService = AppClient.retrofit.create(TagService::class.java)

    // Fungsi untuk membuat tag baru
    suspend fun createTag(tag: TagModel): GeneralResponseModel? = withContext(Dispatchers.IO) {
        try {
            val response = tagService.createTag(tag)
            Log.d("TagRepository", "Response: $response")
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TagRepository", "Error creating tag", e)
            null
        }
    }

    // Fungsi untuk menghapus tag jika tidak ada komunitas yang menggunakannya
    suspend fun deleteTagIfNoCommunityUsage(tagId: Int): GeneralResponseModel? =
        withContext(Dispatchers.IO) {
            try {
                // Ambil tag berdasarkan ID
                val tagResponse = tagService.getTagById(tagId)

                // Cek apakah permintaan berhasil
                if (tagResponse.isSuccessful) {
                    val tag = tagResponse.body()

                    // Periksa apakah tag ada dan tidak ada komunitas yang menggunakannya
                    if (tag != null && tag.communityTags.isEmpty()) {
                        // Jika tag tidak digunakan oleh komunitas, maka hapus tag
                        val deleteResponse = tagService.deleteTag(tagId)
                        if (deleteResponse.isSuccessful) {
                            Log.d("TagRepository", "Tag deleted successfully")
                            return@withContext deleteResponse.body()
                        } else {
                            Log.e("TagRepository", "Error deleting tag")
                        }
                    } else {
                        // Jika ada komunitas yang menggunakan tag, tidak dapat dihapus
                        Log.d("TagRepository", "Tag is in use by communities, cannot delete")
                    }
                } else {
                    Log.e("TagRepository", "Error fetching tag by ID")
                }

                return@withContext null
            } catch (e: Exception) {
                Log.e("TagRepository", "Error checking tag usage", e)
                return@withContext null
            }
        }
    suspend fun getTagById(tagId: Int): TagModel? = withContext(Dispatchers.IO) {
        try {
            val response = tagService.getTagById(tagId)
            Log.d("TagRepository", "Response: $response")
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TagRepository", "Error fetching tag by ID", e)
            null
        }
    }
}