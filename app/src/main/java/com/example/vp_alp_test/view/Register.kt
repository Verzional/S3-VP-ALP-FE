package com.example.vp_alp_test.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todolistapp.enums.PagesEnum
import com.example.vp_alp_test.R
import com.example.vp_alp_test.ui.theme.VP_ALP_TestTheme
import com.example.vp_alp_test.uiState.AuthenticationStatusUIState
import com.example.vp_alp_test.view.templates.AuthenticationButton
import com.example.vp_alp_test.view.templates.AuthenticationOutlinedTextField
import com.example.vp_alp_test.view.templates.AuthenticationQuestion
import com.example.vp_alp_test.view.templates.PasswordOutlinedTextField
import com.example.vp_alp_test.viewmodel.AuthenticationViewModel

@Composable
fun RegisterView(
    authenticationViewModel: AuthenticationViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    val registerUIState by authenticationViewModel.authenticationUIState.collectAsState()
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
                .height(475.dp)
                .width(380.dp)
                .align(Alignment.Center)
                .background(Color(0xFF1D267D), shape = RoundedCornerShape(15.dp))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "HELLO!",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 50.dp),
                textAlign = TextAlign.Start
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF5C469C), shape = RoundedCornerShape(15.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Register",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    AuthenticationOutlinedTextField(
                        inputValue = authenticationViewModel.emailInput,
                        onInputValueChange = {
                            authenticationViewModel.changeEmailInput(it)
                            authenticationViewModel.checkRegisterForm()
                        },
                        labelText = "Email",
                        placeholderText = "Email",
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
                        ),

                    )
                    AuthenticationOutlinedTextField(
                        inputValue = authenticationViewModel.usernameInput,
                        onInputValueChange = {
                            authenticationViewModel.changeUsernameInput(it)
                            authenticationViewModel.checkRegisterForm()
                        },
                        labelText = "Username",
                        placeholderText = "Username",
                        leadingIconSrc = painterResource(id = R.drawable.ic_username),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        keyboardType = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                            authenticationViewModel.checkRegisterForm()
                        },
                        passwordVisibilityIcon = painterResource(id = registerUIState.passwordVisibilityIcon),
                        labelText = "Password",
                        placeholderText = "Password",
                        onTrailingIconClick = {
                            authenticationViewModel.changePasswordVisibility()
                        },
                        passwordVisibility = registerUIState.passwordVisibility,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        keyboardImeAction = ImeAction.Next,
                        onKeyboardNext = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )
                    PasswordOutlinedTextField(
                        passwordInput = authenticationViewModel.confirmPasswordInput,
                        onPasswordInputValueChange = {
                            authenticationViewModel.changeConfirmPasswordInput(it)
                            authenticationViewModel.checkRegisterForm()
                        },
                        passwordVisibilityIcon = painterResource(id = registerUIState.confirmPasswordVisibilityIcon),
                        labelText = "Confirm Password",
                        placeholderText = "Confirm Password",
                        onTrailingIconClick = {
                            authenticationViewModel.changeConfirmPasswordVisibility()
                        },
                        passwordVisibility = registerUIState.confirmPasswordVisibility,
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
                        buttonText = "SIGN UP",
                        onButtonClick = {
                            authenticationViewModel.registerUser(navController)
                        },
                        buttonModifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textModifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 15.dp),
                        buttonEnabled = registerUIState.buttonEnabled,
                        buttonColor = Color(0xFFD4ADFC),
                        userDataStatusUIState = authenticationViewModel.dataStatus,
                        loadingBarModifier = Modifier.size(18.dp)
                    )
                    AuthenticationQuestion(
                        questionText = stringResource(id = R.string.already_have_an_account_text),
                        actionText = stringResource(id = R.string.sign_in_text),
                        onActionTextClicked = {
                            authenticationViewModel.resetViewModel()
                            navController.navigate(PagesEnum.Login.name) {
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
