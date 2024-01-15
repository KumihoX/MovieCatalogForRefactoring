package com.example.emptycomposeactivity.screens.signUpScreen

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.network.auth.AuthRepository
import com.example.emptycomposeactivity.network.auth.LoginRequestBody
import com.example.emptycomposeactivity.network.auth.RegisterRequestBody
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.user.UserRepository
import com.example.emptycomposeactivity.screens.ext.toRequiredDateFormat
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class SignUpViewModel : ViewModel() {

    val emptyMessage = R.string.empty
    val incorrectPasswordMessage = R.string.wrong_password
    val invalidEmailMessage = R.string.wrong_email
    val notEqualMessage = R.string.not_equal

    private val _uiState = mutableStateOf(SignUpScreenState())
    var uiState: State<SignUpScreenState> = _uiState

    private var _gender: Int = -1
    private var correctBirthday: String = ""

    private fun passwordComparison() {
        _uiState.value = _uiState.value.copy(
            equality = _uiState.value.password == _uiState.value.confirmPassword
        )
    }

    private fun correctEmail() {
        _uiState.value = _uiState.value.copy(correct = _uiState.value.email.let {
            android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        })
    }

    private fun checkGender() {
        _gender = if (_uiState.value.manIsPressed || _uiState.value.womanIsPressed) {
            if (_uiState.value.womanIsPressed) {
                1
            } else 0
        } else -1
    }

    private fun checkFields() {
        val login = _uiState.value.login
        val email = _uiState.value.email
        val name = _uiState.value.name
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        val dateOfBirth = _uiState.value.dateOfBirth
        val notValidPassword = _uiState.value.notValidPassword
        checkGender()

        if (login.isNotEmpty() && email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && dateOfBirth.isNotEmpty() && !notValidPassword) {
            if (_gender != -1) {
                _uiState.value = _uiState.value.copy(
                    allFieldsFilled = _uiState.value.equality == _uiState.value.correct
                )

            }
        } else _uiState.value = _uiState.value.copy(
            allFieldsFilled = false
        )
    }

    fun onLoginChange(newLogin: String) {
        _uiState.value = _uiState.value.copy(
            login = newLogin, emptyLogin = newLogin == ""
        )
        checkFields()
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail, emptyEmail = newEmail == ""
        )
        correctEmail()
        checkFields()
    }

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            name = newName, emptyName = newName == ""
        )
        checkFields()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            emptyPassword = newPassword == "",
            notValidPassword = _uiState.value.password.length < 8
        )
        passwordComparison()
        checkFields()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = newConfirmPassword, emptyConfirmPassword = newConfirmPassword == ""
        )
        passwordComparison()
        checkFields()
    }

    fun showDatePickerDialog(context: Context) {
        val year: Int
        val month: Int
        val day: Int

        val calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time = Date()

        val mDatePickerDialog = DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                _uiState.value = _uiState.value.copy(
                    dateOfBirth = "$mDayOfMonth.${mMonth + 1}.$mYear"
                )
                checkFields()
            },
            year,
            month,
            day
        )

        mDatePickerDialog.show()
    }

    fun buttonGenderIsPressed(who: Int) {
        when (who) {
            1 -> {
                _uiState.value = _uiState.value.copy(
                    manIsPressed = true,
                    womanIsPressed = false
                )
            }

            2 -> {
                _uiState.value = _uiState.value.copy(
                    manIsPressed = false,
                    womanIsPressed = true
                )
            }
        }
        checkFields()
    }

    fun register(navController: NavController) {
        val repositoryAuth = AuthRepository()
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        val repositoryMovies = MoviesRepository()
        val repositoryUser = UserRepository()

        correctBirthday = _uiState.value.dateOfBirth.toRequiredDateFormat()

        viewModelScope.launch {
            repositoryAuth.register(
                RegisterRequestBody(
                    userName = _uiState.value.login,
                    name = _uiState.value.name,
                    password = _uiState.value.password,
                    email = _uiState.value.email,
                    birthDate = correctBirthday,
                    gender = _gender
                )
            ).collect {}

            repositoryAuth.login(
                LoginRequestBody(
                    username = _uiState.value.login, password = _uiState.value.password
                )
            ).collect {}

            repositoryFavoriteMovies.getFavoriteMovies().collect {}
            repositoryMovies.getMovies(1).collect {}
            repositoryUser.getUserData().collect {}

            navController.navigate(Screens.NavBarScreen.route) {
                popUpTo(Screens.SignUpScreen.route) {
                    inclusive = true
                }
                popUpTo(Screens.SignInScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    data class SignUpScreenState(
        val login: String = "",
        val emptyLogin: Boolean = false,
        val email: String = "",
        val emptyEmail: Boolean = false,
        val name: String = "",
        val emptyName: Boolean = false,
        val password: String = "",
        val notValidPassword: Boolean = false,
        val emptyPassword: Boolean = false,
        val confirmPassword: String = "",
        val emptyConfirmPassword: Boolean = false,
        val dateOfBirth: String = "",
        val manIsPressed: Boolean = false,
        val womanIsPressed: Boolean = false,
        val equality: Boolean = true,
        val correct: Boolean = true,
        val allFieldsFilled: Boolean = false,
    )
}