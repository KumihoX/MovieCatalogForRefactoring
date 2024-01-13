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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.Navigation
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.ui.theme.*

@Composable
fun ProfileScreen(logout: () -> Unit) {
    val viewModel = ProfileViewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        Avatar(viewModel = viewModel)

        ProfileEmailData(viewModel = viewModel)
        { viewModel.onEmailChange(it) }

        ProfileLinkToAvatar(viewModel = viewModel) { viewModel.onUrlChange(it) }
        ProfileName(viewModel = viewModel) { viewModel.onNameChange(it) }

        ProfileDateOfBirth(viewModel = viewModel)
        { viewModel.showDatePickerDialog(it) }

        Gender(viewModel = viewModel) { viewModel.buttonGenderIsPressed(it) }

        Column(
            modifier = Modifier.fillMaxWidth()
        )
        {
            ProfileSave(viewModel = viewModel) { viewModel.save() }
            LogOut(viewModel = viewModel) { logout() }
        }
    }
}


@Composable
fun Avatar(viewModel: ProfileViewModel) {

    val image: String by remember { viewModel.url }
    val nick: String by remember { viewModel.nick }

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
fun ProfileEmailData(viewModel: ProfileViewModel, onEmailChange: (String) -> Unit) {

    val email: String by remember { viewModel.email }
    val isValid: Boolean by remember { viewModel.correct }
    val emptyEmail: Boolean by remember { viewModel.emptyEmail }

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
fun ProfileLinkToAvatar(viewModel: ProfileViewModel, onUrlChange: (String) -> Unit) {

    val url: String by remember { viewModel.url }

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
fun ProfileName(viewModel: ProfileViewModel, onNameChange: (String) -> Unit) {

    val name: String by remember { viewModel.name }
    val emptyName: Boolean by remember { viewModel.emptyName }

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
fun ProfileDateOfBirth(viewModel: ProfileViewModel, showDatePickerDialog: (Context) -> Unit) {
    val context = LocalContext.current

    val data: String by remember { viewModel.dateOfBirth }

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
fun Gender(viewModel: ProfileViewModel, buttonGenderIsPressed: (Int) -> Unit) {

    val selectedWoman: Boolean by remember { viewModel.womanIsPressed }
    val selectedMan: Boolean by remember { viewModel.manIsPressed }

    val womanBack =
        if (selectedWoman) DarkRed else Black
    val manBack = if (selectedMan) DarkRed else Black

    Text(
        stringResource(R.string.gender),
        color = Gray,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
        fontFamily = IBM,
        fontWeight = FontWeight.Medium
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp, 8.dp, 16.dp, 16.dp)
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
                stringResource(R.string.male), style = MaterialTheme.typography.body2,
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
                stringResource(R.string.female), style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileSave(
    viewModel: ProfileViewModel,
    save: () -> Unit
) {
    val fieldsFilled: Boolean by remember { viewModel.allFieldsFilled }

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
