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
import androidx.compose.ui.text.font.FontFamily
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
import com.example.emptycomposeactivity.ui.theme.*
import java.util.*


@Composable
fun SignUpScreen(navController: NavController) {

    val signUpViewModel: SignUpViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        SignUpLogo()
        SignUpRegistrationText()
        SignUpLoginField(viewModel = signUpViewModel) { signUpViewModel.onLoginChange(it) }
        SignUpEMailField(viewModel = signUpViewModel) { signUpViewModel.onEmailChange(it) }
        SignUpNameField(viewModel = signUpViewModel) { signUpViewModel.onNameChange(it) }
        SignUpPasswordField(viewModel = signUpViewModel) { signUpViewModel.onPasswordChange(it) }
        SignUpConfirmPasswordField(viewModel = signUpViewModel)
        { signUpViewModel.onConfirmPasswordChange(it) }

        SignUpDateOfBirthField(viewModel = signUpViewModel)
        { signUpViewModel.showDatePickerDialog(it) }

        Gender(viewModel = signUpViewModel)
        { signUpViewModel.buttonGenderIsPressed(it) }

        Column(
            modifier = Modifier.fillMaxWidth(),
        )
        {
            SignUpRegister(viewModel = signUpViewModel)
            { signUpViewModel.register(navController = navController) }

            SignUpIHaveAcc(navController)
        }
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
fun SignUpLoginField(viewModel: SignUpViewModel, onLoginChange: (String) -> Unit) {

    val login: String by remember { viewModel.login }
    val emptyLogin: Boolean by remember { viewModel.emptyLogin }

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
fun SignUpEMailField(viewModel: SignUpViewModel, onEmailChange: (String) -> Unit) {

    val email: String by remember { viewModel.email }
    val emptyEmail: Boolean by remember { viewModel.emptyEmail }
    val isValid: Boolean by remember { viewModel.correct }

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
fun SignUpNameField(viewModel: SignUpViewModel, onNameChange: (String) -> Unit) {

    val name: String by remember { viewModel.name }
    val emptyName: Boolean by remember { viewModel.emptyName }

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
fun SignUpPasswordField(viewModel: SignUpViewModel, onPasswordChange: (String) -> Unit) {

    val password: String by remember { viewModel.password }
    val emptyPassword: Boolean by remember { viewModel.emptyPassword }
    val notValidPassword: Boolean by remember { viewModel.notValidPassword }

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
    viewModel: SignUpViewModel,
    onConfirmPasswordChange: (String) -> Unit
) {

    val confirmPassword: String by remember { viewModel.confirmPassword }
    val emptyConfirmPassword: Boolean by remember { viewModel.emptyConfirmPassword }
    val equal: Boolean by remember { viewModel.equality }

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
fun SignUpDateOfBirthField(viewModel: SignUpViewModel, showDatePickerDialog: (Context) -> Unit) {
    val context = LocalContext.current

    val dateOfBirth: String by remember { viewModel.dateOfBirth }

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
fun Gender(viewModel: SignUpViewModel, buttonGenderIsPressed: (Int) -> Unit) {

    val selectedWoman: Boolean by remember { viewModel.womanIsPressed }
    val selectedMan: Boolean by remember { viewModel.manIsPressed }

    val womanBack =
        if (selectedWoman) DarkRed else Black
    val manBack = if (selectedMan) DarkRed else Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp, 0.dp)
            .border(1.dp, White, RoundedCornerShape(8.dp))
    ) {
        Button(
            onClick = { buttonGenderIsPressed(1) },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = manBack,
                contentColor = White
            ),
            shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp),
        )
        {
            Text(
                stringResource(R.string.male),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }

        Divider(
            color = White,
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
        )

        Button(
            onClick = { buttonGenderIsPressed(2) },
            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = womanBack,
                contentColor = White
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        {
            Text(
                stringResource(R.string.female),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SignUpRegister(
    viewModel: SignUpViewModel,
    register: () -> Unit
) {

    val fieldsFilled: Boolean by remember { viewModel.allFieldsFilled }

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


