package com.example.emptycomposeactivity.screens.signInScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.ui.theme.*

@Composable
fun SignInScreen(navController: NavHostController) {

    val signInViewModel: SignInViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        SignInLogo()
        LoginField(viewModel = signInViewModel) { signInViewModel.onLoginChange(it) }
        PasswordField(viewModel = signInViewModel) { signInViewModel.onPasswordChange(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    )
    {
        SignInComeIn(viewModel = signInViewModel, navController = navController)
        SignInRegister(navController = navController)
    }
}

@Composable
fun SignInLogo() {
    Image(
        painter = painterResource(R.drawable.logo_movie_catalog),
        contentDescription = stringResource(R.string.sign_in_logo),
        modifier = Modifier.padding(0.dp, 56.dp, 0.dp, 40.795.dp)
    )
}

@Composable
fun LoginField(viewModel: SignInViewModel, onLoginChange: (String) -> Unit) {

    val login: String by remember { viewModel.login }
    val emptyLogin: Boolean by remember { viewModel.emptyLogin }

    Column(
        modifier = Modifier
            .padding(16.dp, 7.205.dp)
    )
    {
        OutlinedTextField(
            value = login,
            onValueChange = onLoginChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.login),
                    style = MaterialTheme.typography.body2,
                    color = GrayFaded
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = {
                if (emptyLogin) Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.icon_error), tint = White
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Gray,
                placeholderColor = GrayFaded,
                cursorColor = DarkRed,
                textColor = DarkRed,
                focusedBorderColor = DarkRed
            )
        )
        if (emptyLogin) {
            Text(
                text = stringResource(viewModel.emptyMessage),
                color = DarkRed,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            )
        }
    }
}

@Composable
fun PasswordField(viewModel: SignInViewModel, onPasswordChange: (String) -> Unit) {

    val password: String by remember { viewModel.password }
    val emptyPassword: Boolean by remember { viewModel.emptyPassword }
    val notValidPassword: Boolean by remember { viewModel.notValidPassword }

    Column(
        modifier = Modifier
            .padding(16.dp, 7.205.dp)
    )
    {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.password),
                    style = MaterialTheme.typography.body2,
                    color = GrayFaded
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                if (emptyPassword || notValidPassword) Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.icon_error), tint = White
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Gray,
                placeholderColor = GrayFaded,
                cursorColor = DarkRed,
                textColor = DarkRed,
                focusedBorderColor = DarkRed
            )

        )

        if (emptyPassword || notValidPassword) {
            Text(
                text = if (emptyPassword) stringResource(viewModel.emptyMessage)
                else stringResource(viewModel.incorrectPasswordMessage),
                color = DarkRed,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            )
        }
    }
}

@Composable
fun SignInComeIn(
    viewModel: SignInViewModel,
    navController: NavController
) {
    val fieldsFilled: Boolean by remember { viewModel.allFieldsFilled }

    val colorBorder =
        if (fieldsFilled) DarkRed
        else GrayFaded

    Button(
        onClick = {
            viewModel.comeIn(navController)
        },
        enabled = fieldsFilled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DarkRed,
            contentColor = White,
            disabledBackgroundColor = Black,
            disabledContentColor = DarkRed
        ),
        border = BorderStroke(1.dp, colorBorder),
        modifier = Modifier
            .height(44.dp)
            .padding(16.dp, 0.dp)
            .fillMaxWidth()
    )
    {
        Text(
            stringResource(R.string.sign_in),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SignInRegister(navController: NavController) {
    Button(
        onClick = { navController.navigate(Screens.SignUpScreen.route) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Black,
            contentColor = DarkRed
        ),
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 16.dp)
            .fillMaxWidth()
    )
    {
        Text(
            stringResource(R.string.registration),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}
