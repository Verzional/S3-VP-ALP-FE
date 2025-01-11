package com.example.vp_alp_test.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.service.UserAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Call

interface UserRepository {
    val currentUserToken: Flow<String>
    val currentUserId: Flow<Int> // This will emit user ID asynchronously

    suspend fun saveUserToken(token: String)

    suspend fun saveUserId(id: Int)

    fun logout(token: String): Call<GeneralResponseModel>
}

class NetworkUserRepository(
    private val userDataStore: DataStore<Preferences>,
    private val userAPIService: UserAPIService
) : UserRepository {
    private companion object {
        val TAG = "NetworkUserRepository"
        val USER_TOKEN = stringPreferencesKey("token")
        val USER_ID = intPreferencesKey("id")
    }

    override val currentUserToken: Flow<String> = userDataStore.data.map { preferences ->
        val token = preferences[USER_TOKEN] ?: "Unknown"
        Log.d(TAG, "Retrieved user token: $token")
        token
    }

    override val currentUserId: Flow<Int> = userDataStore.data.map { preferences ->
        val id = preferences[USER_ID] ?: -1 // Default value for ID
        Log.d(TAG, "Retrieved user ID: $id")
        id
    }

    override suspend fun saveUserToken(token: String) {
        try {
            Log.d(TAG, "Saving user token: $token")
            userDataStore.edit { preferences ->
                preferences[USER_TOKEN] = token
            }
            Log.d(TAG, "User token saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user token: ${e.message}", e)
        }
    }

    override suspend fun saveUserId(id: Int) {
        try {
            Log.d(TAG, "Saving user ID: $id")
            userDataStore.edit { preferences ->
                preferences[USER_ID] = id
            }
            Log.d(TAG, "User ID saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user ID: ${e.message}", e)
        }
    }

    override fun logout(token: String): Call<GeneralResponseModel> {
        Log.d(TAG, "Logging out user with token: $token")
        if (token.isBlank()) {
            Log.e(TAG, "Logout failed: Token is blank")
            throw IllegalArgumentException("Invalid token: Token cannot be blank")
        }
        return userAPIService.logout(token)
    }


    // Helper function to retrieve the saved user ID synchronously if needed
    suspend fun getUserId(): Int {
        return try {
            val id = userDataStore.data.first()[USER_ID] ?: 0 // Default to 0 if no ID saved
            Log.d(TAG, "Retrieved user ID synchronously: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to retrieve user ID: ${e.message}", e)
            0
        }
    }
}
