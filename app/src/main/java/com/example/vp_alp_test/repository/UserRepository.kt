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
        val USER_TOKEN = stringPreferencesKey("token")
        val USER_ID = intPreferencesKey("id")
    }

    override val currentUserToken: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_TOKEN] ?: "Unknown"
    }

    override val currentUserId: Flow<Int> = userDataStore.data.map { preferences ->
        preferences[USER_ID] ?: -1 // Default value for ID
    }

    override suspend fun saveUserToken(token: String) {
        // Log token saving
        Log.d("NetworkUserRepository", "Saving user token: $token")
        userDataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override suspend fun saveUserId(id: Int) {
        // Log user ID saving
        Log.d("NetworkUserRepository", "Saving user ID: $id")

        userDataStore.edit { preferences ->
            preferences[USER_ID] = id
        }

    }

    override fun logout(token: String): Call<GeneralResponseModel> {
        return userAPIService.logout(token)
    }

    // Helper function to retrieve the saved user ID synchronously if needed
    suspend fun getUserId(): Int {
        return userDataStore.data.first()[USER_ID] ?: 0 // Default to 0 if no ID saved
    }
}
