package com.example.vp_alp_test.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistapp.enums.PagesEnum
import com.example.vp_alp_test.viewmodel.AuthenticationViewModel
import com.example.vp_alp_test.viewmodel.ProfileViewModel

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    authenticationViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory),
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    val localContext = LocalContext.current

    // Observe login or registration success
    val authState = authenticationViewModel.authenticationUIState.collectAsState()

    // Fetch the token and userId after successful login/register
    val token = authenticationViewModel.token // token saved during login/register
    val id = authenticationViewModel.id // userId saved during login/register

    if (token.isNotEmpty() && id != 0) {
        // Fetch user profile if token and userId are available
        profileViewModel.getUserProfile(token, id)
    }

    NavHost(
        navController = navController,
        startDestination = PagesEnum.Register.name // Start with Register page
    ) {
        composable(route = PagesEnum.Login.name) {
            LoginView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                authenticationViewModel = authenticationViewModel,
                navController = navController,
                context = localContext
            )
        }

        composable(route = PagesEnum.Register.name) {
            RegisterView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                authenticationViewModel = authenticationViewModel,
                navController = navController,
                context = localContext
            )
        }

        composable(route = PagesEnum.Profile.name) {
            ProfileView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                profileViewModel = profileViewModel,
                navController = navController,
                token = token,
                id = id,
                context = localContext
            )
        }
    }
}
