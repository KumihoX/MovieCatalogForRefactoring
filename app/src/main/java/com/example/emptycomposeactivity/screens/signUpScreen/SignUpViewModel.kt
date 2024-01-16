package com.example.emptycomposeactivity.screens.signUpScreen

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
import com.example.emptycomposeactivity.screens.enums.Gender
import com.example.emptycomposeactivity.screens.enums.toInt
import com.example.emptycomposeactivity.screens.ext.convertToRequiredExternalDateFormat
import com.example.emptycomposeactivity.screens.validation.checkEmail
import com.example.emptycomposeactivity.screens.validation.checkFieldsOnEmptiness
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val emptyMessage = R.string.empty
    val incorrectPasswordMessage = R.string.wrong_password
    val invalidEmailMessage = R.string.wrong_email
    val notEqualMessage = R.string.not_equal

    private val _uiState = mutableStateOf(SignUpScreenState())
    var uiState: State<SignUpScreenState> = _uiState

    private var _gender: Gender = Gender.NOT_SELECTED
    private var correctBirthday: String = ""

    private fun passwordComparison() {
        _uiState.value = _uiState.value.copy(
            equality = _uiState.value.password == _uiState.value.confirmPassword
        )
    }

    private fun checkGender() {
        _gender = if (_uiState.value.manIsPressed || _uiState.value.womanIsPressed) {
            if (_uiState.value.womanIsPressed) Gender.WOMAN else Gender.MAN
        } else Gender.NOT_SELECTED
    }

    private fun checkFields() {
        val isAllFieldsNotEmpty = with(_uiState.value) {
            checkFieldsOnEmptiness(
                login = login,
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                dateOfBirth = dateOfBirth
            )
        }
        checkGender()

        if (isAllFieldsNotEmpty && !_uiState.value.notValidPassword) {
            if (_gender != Gender.NOT_SELECTED) {
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
            login = newLogin,
            emptyLogin = newLogin == "",
        )
        checkFields()
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            emptyEmail = newEmail == "",
            correct = checkEmail(email = newEmail)
        )
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

    fun onBirthdateChange(day: Int, month: Int, year: Int) {
        _uiState.value =
            _uiState.value.copy(dateOfBirth = "$day.${month + 1}.$year")
        checkFields()
    }

    fun buttonGenderIsPressed(who: Gender) {
        when (who) {
            Gender.MAN -> {
                _uiState.value = _uiState.value.copy(
                    manIsPressed = true,
                    womanIsPressed = false
                )
            }

            Gender.WOMAN -> {
                _uiState.value = _uiState.value.copy(
                    manIsPressed = false,
                    womanIsPressed = true
                )
            }

            Gender.NOT_SELECTED -> {
                _uiState.value = _uiState.value.copy(
                    manIsPressed = false,
                    womanIsPressed = false
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

        correctBirthday = _uiState.value.dateOfBirth.convertToRequiredExternalDateFormat()

        viewModelScope.launch {
            repositoryAuth.register(
                RegisterRequestBody(
                    userName = _uiState.value.login,
                    name = _uiState.value.name,
                    password = _uiState.value.password,
                    email = _uiState.value.email,
                    birthDate = correctBirthday,
                    gender = _gender.toInt()
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