package com.example.emptycomposeactivity.screens.profileScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.screens.components.BirthdayPickerDialog
import com.example.emptycomposeactivity.screens.components.GenderButton
import com.example.emptycomposeactivity.screens.profileScreen.ProfileViewModel.ProfileScreenState
import com.example.emptycomposeactivity.ui.theme.*

@Composable
fun ProfileScreen(logout: () -> Unit) {
    val viewModel = ProfileViewModel()

    val state: ProfileScreenState by remember { viewModel.uiState }
    var showDataPickerDialogState: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        Avatar(state = state)

        ProfileEmailData(state = state, viewModel = viewModel)
        { viewModel.onEmailChange(it) }

        ProfileLinkToAvatar(state = state) { viewModel.onUrlChange(it) }
        ProfileName(state = state, viewModel = viewModel) { viewModel.onNameChange(it) }

        ProfileDateOfBirth(state = state)
        { showDataPickerDialogState = true }

        GenderButton(
            selectedMan = remember(state.manIsPressed) { state.manIsPressed },
            selectedWoman = remember(state.womanIsPressed) { state.womanIsPressed }
        ) { viewModel.buttonGenderIsPressed(it) }

        Column(
            modifier = Modifier.fillMaxWidth()
        )
        {
            ProfileSave(state = state) { viewModel.save() }
            LogOut(viewModel = viewModel) { logout() }
        }
    }

    if (showDataPickerDialogState) {
        BirthdayPickerDialog(onBirthdateChange = { day, month, year ->
            viewModel.onBirthdateChange(
                day,
                month,
                year
            )
        })
    }
}


@Composable
fun Avatar(state: ProfileScreenState) {

    val image = state.url
    val nick = state.nick

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    )
    {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(White)
        ) {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = stringResource(R.string.user_avatar),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }

        Text(
            nick,
            color = White,
            fontSize = 24.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            fontFamily = IBM,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun ProfileEmailData(state: ProfileScreenState, viewModel: ProfileViewModel, onEmailChange: (String) -> Unit) {

    val email = state.email
    val isValid = state.correct
    val emptyEmail = state.emptyEmail

    Text(
        stringResource(R.string.email),
        color = Gray,
        fontFamily = IBM,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp),
    )
    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 12.dp)
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
fun ProfileLinkToAvatar(state: ProfileScreenState, onUrlChange: (String) -> Unit) {

    val url = state.url

    Text(
        stringResource(R.string.avatar_link),
        color = Gray,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
        fontFamily = IBM,
        fontWeight = FontWeight.Medium
    )
    OutlinedTextField(
        value = url,
        onValueChange = onUrlChange,
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 12.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                stringResource(R.string.url),
                style = MaterialTheme.typography.body2
            )
        },
        enabled = true,
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
fun ProfileName(state: ProfileScreenState, viewModel: ProfileViewModel, onNameChange: (String) -> Unit) {

    val name = state.name
    val emptyName = state.emptyName

    Text(
        stringResource(R.string.name),
        color = Gray,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
        fontFamily = IBM,
        fontWeight = FontWeight.Medium
    )
    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 12.dp)
    )
    {
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

@SuppressLint("ResourceType")
@Composable
fun ProfileDateOfBirth(state: ProfileScreenState, showDatePickerDialog: (Context) -> Unit) {
    val context = LocalContext.current

    val data = state.dateOfBirth

    Text(
        stringResource(R.string.birthdate),
        color = Gray,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
        fontFamily = IBM,
        fontWeight = FontWeight.Medium
    )

    OutlinedTextField(
        value = data,
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
fun ProfileSave(
    state: ProfileScreenState,
    save: () -> Unit
) {
    val fieldsFilled = state.allFieldsFilled

    val colorBorder =
        if (fieldsFilled) DarkRed
        else GrayFaded

    Button(
        enabled = fieldsFilled,
        onClick = { save() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DarkRed,
            contentColor = White,
            disabledBackgroundColor = Black,
            disabledContentColor = DarkRed
        ),
        border = BorderStroke(1.dp, colorBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 26.dp, 16.dp, 4.dp)
            .height(44.dp)
    )
    {
        Text(
            stringResource(R.string.save),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

@Composable
fun LogOut(viewModel: ProfileViewModel, logout: () -> Unit) {
    Button(
        onClick = {
            viewModel.logout()
            logout()
        },
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
            stringResource(R.string.logout),
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

