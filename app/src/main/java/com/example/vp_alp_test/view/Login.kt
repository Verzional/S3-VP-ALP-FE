package com.example.vp_alp_test.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.enums.PagesEnum
import com.example.vp_alp_test.R
import com.example.vp_alp_test.uiState.AuthenticationStatusUIState
import com.example.vp_alp_test.view.templates.AuthenticationButton
import com.example.vp_alp_test.view.templates.AuthenticationOutlinedTextField
import com.example.vp_alp_test.view.templates.AuthenticationQuestion
import com.example.vp_alp_test.view.templates.PasswordOutlinedTextField
import com.example.vp_alp_test.viewmodel.AuthenticationViewModel

@Composable
fun LoginView(
    authenticationViewModel: AuthenticationViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    val loginUIState by authenticationViewModel.authenticationUIState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(authenticationViewModel.dataStatus) {
        val dataStatus = authenticationViewModel.dataStatus
        if (dataStatus is AuthenticationStatusUIState.Failed) {
            Toast.makeText(context, dataStatus.errorMessage, Toast.LENGTH_SHORT).show()
            authenticationViewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C134F)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { rotationZ = 5f }
                .offset(y = 45.dp)
                .padding(24.dp)
                .height(350.dp)
                .width(370.dp)
                .align(Alignment.Center)
                .background(Color(0xFF1D267D), shape = RoundedCornerShape(15.dp))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "WELCOME BACK!",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 50.dp)
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF5C469C), shape = RoundedCornerShape(15.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Login",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    AuthenticationOutlinedTextField(
                        inputValue = authenticationViewModel.emailInput,
                        onInputValueChange = {
                            authenticationViewModel.changeEmailInput(it)
                            authenticationViewModel.checkLoginForm()
                        },
                        labelText = "Username",
                        placeholderText = "Username",
                        leadingIconSrc = painterResource(id = R.drawable.ic_email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        keyboardType = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        onKeyboardNext = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )
                    PasswordOutlinedTextField(
                        passwordInput = authenticationViewModel.passwordInput,
                        onPasswordInputValueChange = {
                            authenticationViewModel.changePasswordInput(it)
                            authenticationViewModel.checkLoginForm()
                        },
                        passwordVisibilityIcon = painterResource(id = loginUIState.passwordVisibilityIcon),
                        labelText = "Password",
                        placeholderText = "Password",
                        onTrailingIconClick = {
                            authenticationViewModel.changePasswordVisibility()
                        },
                        passwordVisibility = loginUIState.passwordVisibility,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        keyboardImeAction = ImeAction.None,
                        onKeyboardNext = KeyboardActions(
                            onDone = null
                        )
                    )
                    AuthenticationButton(
                        buttonText = "SIGN IN",
                        onButtonClick = {
                            authenticationViewModel.loginUser(navController)
                        },
                        buttonModifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textModifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 15.dp),
                        buttonEnabled = loginUIState.buttonEnabled,
                        buttonColor = Color(0xFFD4ADFC),
                        userDataStatusUIState = authenticationViewModel.dataStatus,
                        loadingBarModifier = Modifier.size(18.dp)
                    )
//                    if (loginUIState.errorMessage.isNotEmpty()) {
//                        Text(
//                            text = loginUIState.errorMessage,,
//                            fontSize = 14.sp,
//                            color = if (loginUIState.errorMessage.contains("success", true)) Color.Green else Color.Red,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                    }
                    AuthenticationQuestion(
                        questionText = stringResource(id = R.string.don_t_have_an_account_yet_text),
                        actionText = stringResource(id = R.string.registerText),
                        onActionTextClicked = {
                            authenticationViewModel.resetViewModel()
                            navController.navigate(PagesEnum.Register.name) {
                                popUpTo(PagesEnum.Register.name) {
                                    inclusive = true
                                }
                            }
                        },
                        rowModifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

