package com.example.vp_alp_test.view.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_test.R
import com.example.vp_alp_test.uiState.AuthenticationStatusUIState

@Composable
fun AuthenticationOutlinedTextField(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String,
    leadingIconSrc: Painter,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardOptions,
    onKeyboardNext: KeyboardActions
) {
    OutlinedTextField(
        value = inputValue,
        onValueChange = onInputValueChange,
        singleLine = true,
        label = {
            Text(
                text = labelText,
                color = Color.White
            )
        },
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.LightGray
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(size = 15.dp),
        leadingIcon = {
            Image(
                painter = leadingIconSrc,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),

            )
        },
        keyboardOptions = keyboardType,
        keyboardActions = onKeyboardNext
    )
}

@Composable
fun PasswordOutlinedTextField(
    passwordInput: String,
    onPasswordInputValueChange: (String) -> Unit,
    passwordVisibilityIcon: Painter,
    labelText: String,
    placeholderText: String,
    onTrailingIconClick: () -> Unit,
    passwordVisibility: VisualTransformation,
    modifier: Modifier = Modifier,
    onKeyboardNext: KeyboardActions,
    keyboardImeAction: ImeAction
) {
    OutlinedTextField(
        value = passwordInput,
        onValueChange = onPasswordInputValueChange,
        singleLine = true,
        label = {
            Text(
                text = labelText,
                color = Color.White
            )
        },
        trailingIcon = {
            Image(
                painter = passwordVisibilityIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.LightGray),
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    }
            )
        },
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.LightGray
            )
        },
        visualTransformation = passwordVisibility,
        modifier = modifier,
        shape = RoundedCornerShape(size = 15.dp),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardImeAction
        ),
        keyboardActions = onKeyboardNext
    )
}

@Composable
fun AuthenticationButton(
    buttonText: String,
    onButtonClick: () -> Unit,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonEnabled: Boolean,
    buttonColor: Color,
    userDataStatusUIState: AuthenticationStatusUIState,
    loadingBarModifier: Modifier = Modifier
) {
    when(userDataStatusUIState) {
        is AuthenticationStatusUIState.Loading -> CircleLoadingTemplate(
            modifier = loadingBarModifier,
            color = Color.Blue,
            trackColor = Color.Transparent
        )
        else -> Button(
            onClick = onButtonClick,
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(buttonColor),
            enabled = buttonEnabled
        ) {
            Text(
                text = buttonText,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = textModifier,
                color = Color.Gray
            )
        }
    }

}

@Composable
fun AuthenticationQuestion(
    questionText: String,
    actionText: String,
    rowModifier: Modifier = Modifier,
    onActionTextClicked: () -> Unit,
) {
    Row(
        modifier = rowModifier
    ) {
        Text(
            text = questionText,
            color = Color.White
        )

        Text(
            text = actionText,
            color = Color(0xFFD4ADFC),
            modifier = Modifier
                .clickable {
                    onActionTextClicked()
                }
        )
    }
}