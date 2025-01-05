package com.example.vp_alp_test.repository

import com.example.vp_alp_test.AppClient
import com.example.vp_alp_test.model.UserResponse
import com.example.vp_alp_test.service.AuthenticationAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

//
//class AuthenticationRepository {
//    private val authenticationAPIService = AppClient.retrofit.create(AuthenticationAPIService::class.java)
//
//    suspend fun register(username: String, email: String, password: String): UserResponse = withContext(Dispatchers.IO) {
//        val requestMap = mapOf(
//            "username" to username,
//            "email" to email,
//            "password" to password
//        )
//        val response = authenticationAPIService.register(requestMap).execute()
//        if (response.isSuccessful && response.body()?.status == "success") {
//            response.body()!!
//        } else {
//            throw Exception("Registration failed: ${response.errorBody()?.string()}")
//        }
//    }
//
//    suspend fun login(email: String, password: String): UserResponse = withContext(Dispatchers.IO) {
//        val requestMap = mapOf(
//            "email" to email,
//            "password" to password
//        )
//        val response = authenticationAPIService.login(requestMap).execute()
//        if (response.isSuccessful && response.body()?.status == "success") {
//            response.body()!!
//        } else {
//            throw Exception("Login failed: ${response.errorBody()?.string()}")
//        }
//    }
//}

interface AuthenticationRepository {
    fun register(username: String, email: String, password: String): Call<UserResponse>

    fun login(email: String, password: String): Call<UserResponse>
}

class NetworkAuthenticationRepository(
    private val authenticationAPIService: AuthenticationAPIService
): AuthenticationRepository {
    override fun register(username: String, email: String, password: String): Call<UserResponse> {
        var registerMap = HashMap<String, String>()

        registerMap["username"] = username
        registerMap["email"] = email
        registerMap["password"] = password

        return authenticationAPIService.register(registerMap)
    }

    override fun login(email: String, password: String): Call<UserResponse> {
        var loginMap = HashMap<String, String>()

        loginMap["email"] = email
        loginMap["password"] = password

        return authenticationAPIService.login(loginMap)
    }
}