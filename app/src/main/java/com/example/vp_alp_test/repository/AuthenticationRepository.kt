package com.example.vp_alp_test.repository

import com.example.vp_alp_test.model.UserResponse
import com.example.vp_alp_test.service.AuthenticationAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

interface AuthenticationRepository {
    fun register(username: String, email: String, password: String): Call<UserResponse>

    fun login(email: String, password: String): Call<UserResponse>
}

class NetworkAuthenticationRepository(
    private val authenticationAPIService: AuthenticationAPIService
) : AuthenticationRepository {
    override fun register(username: String, email: String, password: String): Call<UserResponse> {
        val registerMap = HashMap<String, String>()

        registerMap["username"] = username
        registerMap["email"] = email
        registerMap["password"] = password

        return authenticationAPIService.register(registerMap)
    }

    override fun login(email: String, password: String): Call<UserResponse> {
        val loginMap = HashMap<String, String>()

        loginMap["email"] = email
        loginMap["password"] = password

        return authenticationAPIService.login(loginMap)
    }
}
