package com.example.emptycomposeactivity.screens.signUpScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.screens.components.BirthdayPickerDialog
import com.example.emptycomposeactivity.screens.components.GenderButton
import com.example.emptycomposeactivity.screens.signUpScreen.SignUpViewModel.SignUpScreenState
import com.example.emptycomposeactivity.ui.theme.*
import java.util.*


@Composable
fun SignUpScreen(navController: NavController) {

    val signUpViewModel: SignUpViewModel = viewModel()
    val state: SignUpScreenState by remember { signUpViewModel.uiState }
    var showDataPickerDialogState: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        SignUpLogo()
        SignUpRegistrationText()
        SignUpLoginField(
            state = state,
            viewModel = signUpViewModel
        ) { signUpViewModel.onLoginChange(it) }
        SignUpEMailField(
            state = state,
            viewModel = signUpViewModel
        ) { signUpViewModel.onEmailChange(it) }
        SignUpNameField(state = state, viewModel = signUpViewModel) {
            signUpViewModel.onNameChange(
                it
            )
        }
        SignUpPasswordField(
            state = state,
            viewModel = signUpViewModel
        ) { signUpViewModel.onPasswordChange(it) }
        SignUpConfirmPasswordField(state = state, viewModel = signUpViewModel)
        { signUpViewModel.onConfirmPasswordChange(it) }

        SignUpDateOfBirthField(state = state)
        { showDataPickerDialogState = true }

        GenderButton(
            selectedMan = remember(state.manIsPressed) { state.manIsPressed },
            selectedWoman = remember(state.womanIsPressed) { state.womanIsPressed }
        )
        { signUpViewModel.buttonGenderIsPressed(it) }

        Column(
            modifier = Modifier.fillMaxWidth(),
        )
        {
            SignUpRegister(state = state)
            { signUpViewModel.register(navController = navController) }

            SignUpIHaveAcc(navController)
        }
    }

    if (showDataPickerDialogState) {
        BirthdayPickerDialog(onBirthdateChange = { day, month, year ->
            signUpViewModel.onBirthdateChange(
                day,
                month,
                year
            )
        })
    }
}


@Composable
fun SignUpLogo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var animationAlreadyStarted by remember { mutableStateOf(false) }

        val animatedHeightDp: Dp by animateDpAsState(
            targetValue = if (animationAlreadyStarted) 100.dp else 170.dp,
            animationSpec = tween(durationMillis = 500)
        )
        val animatedWidthDp: Dp by animateDpAsState(
            targetValue = if (animationAlreadyStarted) 170.dp else 250.dp,
            animationSpec = tween(durationMillis = 500)
        )

        LaunchedEffect(animationAlreadyStarted) {
            animationAlreadyStarted = true
        }

        Image(
            painter = painterResource(R.drawable.mini_logo_movie_catalog),
            contentDescription = stringResource(R.string.sign_up_logo),
            modifier = Modifier
                .padding(0.dp, 56.dp, 0.dp, 8.dp)
                .height(animatedHeightDp)
                .width((animatedWidthDp))
        )
    }
}

@Composable
fun SignUpRegistrationText() {
    Text(
        stringResource(R.string.registration),
        fontFamily = IBM,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = DarkRed,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
    )
}

@Composable
fun SignUpLoginField(
    state: SignUpScreenState,
    viewModel: SignUpViewModel,
    onLoginChange: (String) -> Unit
) {

    val login = state.login
    val emptyLogin = state.emptyLogin

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 8.dp)
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
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
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
fun SignUpEMailField(
    state: SignUpScreenState,
    viewModel: SignUpViewModel,
    onEmailChange: (String) -> Unit
) {

    val email = state.email
    val emptyEmail = state.emptyEmail
    val isValid = state.correct

    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp)
    )
    {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.email),
                    style = MaterialTheme.typography.body2
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                if (!isValid) Icon(
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
        if (emptyEmail || !isValid) {
            Text(
                text = if (emptyEmail) stringResource(viewModel.emptyMessage)
                else stringResource(viewModel.invalidEmailMessage),
                color = DarkRed,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            )
        }
    }
}

@Composable
fun SignUpNameField(
    state: SignUpScreenState,
    viewModel: SignUpViewModel,
    onNameChange: (String) -> Unit
) {

    val name = state.name
    val emptyName = state.emptyName

    Column(
        modifier = Modifier.padding(16.dp, 8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.name),
                    style = MaterialTheme.typography.body2
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                if (emptyName) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.icon_error), tint = White
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Gray,
                placeholderColor = GrayFaded,
                cursorColor = DarkRed,
                textColor = DarkRed,
                focusedBorderColor = DarkRed
            )
        )
        if (emptyName) {
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
fun SignUpPasswordField(
    state: SignUpScreenState,
    viewModel: SignUpViewModel,
    onPasswordChange: (String) -> Unit
) {

    val password = state.password
    val emptyPassword = state.emptyPassword
    val notValidPassword = state.notValidPassword

    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp)
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
                    style = MaterialTheme.typography.body2
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                if (emptyPassword || notValidPassword) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.icon_error), tint = White
                    )
                }
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
fun SignUpConfirmPasswordField(
    state: SignUpScreenState,
    viewModel: SignUpViewModel,
    onConfirmPasswordChange: (String) -> Unit
) {

    val confirmPassword = state.confirmPassword
    val emptyConfirmPassword = state.emptyConfirmPassword
    val equal = state.equality

    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp)
    )
    {
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.confirm_password),
                    style = MaterialTheme.typography.body2
                )
            },
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                if (!equal || emptyConfirmPassword)
                    Icon(
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
        if (emptyConfirmPassword || !equal) {
            Text(
                text = if (emptyConfirmPassword) stringResource(viewModel.emptyMessage)
                else stringResource(viewModel.notEqualMessage),
                color = DarkRed,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            )
        }
    }

}

@SuppressLint("ResourceType")
@Composable
fun SignUpDateOfBirthField(state: SignUpScreenState, showDatePickerDialog: (Context) -> Unit) {
    val context = LocalContext.current

    val dateOfBirth = state.dateOfBirth

    OutlinedTextField(
        value = dateOfBirth,
        onValueChange = {},
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 16.dp)
            .fillMaxWidth(),
        //вынести в отдельную функцию
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showDatePickerDialog(context)
                        }
                    }
                }
            },
        readOnly = true,
        placeholder = {
            Text(
                stringResource(R.string.birthdate),
                style = MaterialTheme.typography.body2
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = stringResource(R.string.icon_calendar),
                tint = White
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gray,
            placeholderColor = GrayFaded,
            cursorColor = DarkRed,
            textColor = DarkRed,
            focusedBorderColor = DarkRed
        )
    )
}

@Composable
fun SignUpRegister(
    state: SignUpScreenState,
    register: () -> Unit
) {
    val fieldsFilled = state.allFieldsFilled

    val colorBorder =
        if (fieldsFilled) DarkRed
        else GrayFaded

    Button(
        enabled = fieldsFilled,
        onClick = {
            register()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DarkRed,
            contentColor = White,
            disabledBackgroundColor = Black,
            disabledContentColor = DarkRed
        ),
        border = BorderStroke(1.dp, colorBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 32.dp, 16.dp, 4.dp)
            .height(44.dp)
    )
    {
        Text(
            stringResource(R.string.register),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SignUpIHaveAcc(navController: NavController) {
    Button(
        onClick = { navController.navigate(Screens.SignInScreen.route) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Black,
            contentColor = DarkRed
        ),
        modifier = Modifier
            .padding(16.dp, 4.dp, 16.dp, 16.dp)
            .fillMaxWidth(),
    )
    {
        Text(
            stringResource(R.string.acc_already_exist),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}



