package com.example.vp_alp_test.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.todolistapp.enums.PagesEnum
import com.example.vp_alp_test.R
import com.example.vp_alp_test.CommunityApplication
import com.example.vp_alp_test.model.ErrorModel
import com.example.vp_alp_test.model.UserResponse
import com.example.vp_alp_test.repository.AuthenticationRepository
import com.example.vp_alp_test.repository.UserRepository
import com.example.vp_alp_test.uiState.AuthenticationStatusUIState
import com.example.vp_alp_test.uiState.AuthenticationUIState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthenticationViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userIdStateFlow = MutableStateFlow<Int>(0)  // Menyimpan ID
    val userIdStateFlow: StateFlow<Int> = _userIdStateFlow.asStateFlow()
    private val _authenticationUIState = MutableStateFlow(AuthenticationUIState())
    val authenticationUIState: StateFlow<AuthenticationUIState>
        get() = _authenticationUIState.asStateFlow()

    var dataStatus: AuthenticationStatusUIState by mutableStateOf(AuthenticationStatusUIState.Start)
        private set

    var token: String by mutableStateOf("")
        private set

    var id: Int by mutableStateOf(0)
        private set

    var usernameInput by mutableStateOf("")
        private set

    var passwordInput by mutableStateOf("")
        private set

    var confirmPasswordInput by mutableStateOf("")
        private set

    var emailInput by mutableStateOf("")
        private set

    fun changeEmailInput(emailInput: String) {
        this.emailInput = emailInput
    }

    fun changeConfirmPasswordInput(confirmPasswordInput: String) {
        this.confirmPasswordInput = confirmPasswordInput
    }

    fun changeUsernameInput(usernameInput: String) {
        this.usernameInput = usernameInput
    }

    fun changePasswordInput(passwordInput: String) {
        this.passwordInput = passwordInput
    }

    fun changePasswordVisibility() {
        _authenticationUIState.update { currentState ->
            currentState.copy(
                showPassword = !currentState.showPassword,
                passwordVisibility = if (currentState.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                passwordVisibilityIcon = if (currentState.showPassword) R.drawable.ic_password_visible else R.drawable.ic_password_invisible
            )
        }
    }

    fun changeConfirmPasswordVisibility() {
        _authenticationUIState.update { currentState ->
            currentState.copy(
                showConfirmPassword = !currentState.showConfirmPassword,
                confirmPasswordVisibility = if (currentState.showConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
                confirmPasswordVisibilityIcon = if (currentState.showConfirmPassword) R.drawable.ic_password_visible else R.drawable.ic_password_invisible
            )
        }
    }

    fun checkLoginForm() {
        _authenticationUIState.update { currentState ->
            currentState.copy(buttonEnabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty())
        }
    }

    fun checkRegisterForm() {
        _authenticationUIState.update { currentState ->
            currentState.copy(
                buttonEnabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty() &&
                        usernameInput.isNotEmpty() && confirmPasswordInput.isNotEmpty() &&
                        passwordInput == confirmPasswordInput
            )
        }
    }

    fun checkButtonEnabled(isEnabled: Boolean): Color {
        return if (isEnabled) Color.Blue else Color.LightGray
    }

    fun registerUser(navController: NavHostController) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    authenticationRepository.register(usernameInput, emailInput, passwordInput).execute()
                }
                if (response.isSuccessful) {
                    response.body()?.data?.id?.let { id ->
                        userRepository.saveUserId(id)
                        Log.d("RegisterSuccess", "User ID saved: $id")
                        onRegisterSuccess(response.body()?.data?.token.orEmpty(), id, navController)
                    } ?: run {
                        Log.e("RegisterError", "User ID is null")
                    }
                } else {
                    Log.e("RegisterError", "Register failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RegisterError", "Exception: ${e.message}")
            }
        }
    }

    fun loginUser(navController: NavHostController) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    authenticationRepository.login(emailInput, passwordInput).execute()
                }
                if (response.isSuccessful) {
                    response.body()?.data?.id?.let { id ->
                        userRepository.saveUserId(id)
                        onLoginSuccess(response.body()?.data?.token.orEmpty(), id, navController)
                        Log.d("LoginSuccess", "User ID saved: $id")
                    } ?: run {
                        Log.e("LoginError", "User ID is null")
                    }
                } else {
                    Log.e("LoginError", "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginError", "Exception: ${e.message}")
            }
        }
    }

    fun onLoginSuccess(token: String, id: Int, navController: NavHostController) {
        this.token = token
        this.id = id
        _userIdStateFlow.value = id  // Update state ID di AuthenticationViewModel
        saveUsernameToken(token)
        saveUsernameId(id)
        navController.navigate(PagesEnum.Profile.name)
    }

    fun onRegisterSuccess(token: String, id: Int, navController: NavHostController) {
        this.token = token
        this.id = id
        _userIdStateFlow.value = id  // Update state ID di AuthenticationViewModel
        saveUsernameToken(token)
        saveUsernameId(id)
        navController.navigate(PagesEnum.Profile.name)
    }

    fun saveUsernameToken(token: String) {
        viewModelScope.launch {
            userRepository.saveUserToken(token)
        }
    }

    fun saveUsernameId(id: Int) {
        viewModelScope.launch {
            userRepository.saveUserId(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CommunityApplication)
                AuthenticationViewModel(
                    application.container.authenticationRepository,
                    application.container.userRepository
                )
            }
        }
    }

    fun resetViewModel() {
        changeEmailInput("")
        changePasswordInput("")
        changeUsernameInput("")
        changeConfirmPasswordInput("")
        _authenticationUIState.update { currentState ->
            currentState.copy(
                showConfirmPassword = false,
                showPassword = false,
                passwordVisibility = PasswordVisualTransformation(),
                confirmPasswordVisibility = PasswordVisualTransformation(),
                passwordVisibilityIcon = R.drawable.ic_password_visible,
                confirmPasswordVisibilityIcon = R.drawable.ic_password_visible,
                buttonEnabled = false
            )
        }
        dataStatus = AuthenticationStatusUIState.Start
    }

    fun clearErrorMessage() {
        dataStatus = AuthenticationStatusUIState.Start
    }
}
