package com.example.emptycomposeactivity.screens.signInScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.network.auth.AuthRepository
import com.example.emptycomposeactivity.network.auth.LoginRequestBody
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.user.UserRepository
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    val emptyMessage = R.string.empty
    val incorrectPasswordMessage = R.string.wrong_password


    private val _login = mutableStateOf("")
    var login: State<String> = _login

    private val _emptyLogin = mutableStateOf(false)
    var emptyLogin: State<Boolean> = _emptyLogin


    private val _password = mutableStateOf("")
    var password: State<String> = _password

    private val _notValidPassword = mutableStateOf(false)
    var notValidPassword: State<Boolean> = _notValidPassword

    private val _emptyPassword = mutableStateOf(false)
    var emptyPassword: State<Boolean> = _emptyPassword


    private val _allFieldsFilled = mutableStateOf(false)
    var allFieldsFilled: State<Boolean> = _allFieldsFilled

    private fun checkFields() {
        val login = _login.value
        val password = _password.value
        if (login != "" && password != "") {
            _allFieldsFilled.value = login.isNotEmpty() && password.isNotEmpty()
        }
    }

    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
        _emptyLogin.value = _login.value == ""
        checkFields()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        if (_password.value == "") {
            _emptyPassword.value = true
        } else {
            if (_password.value.length < 8) {
                _notValidPassword.value = true
            } else {
                _emptyPassword.value = false
                _notValidPassword.value = false
            }
        }
        checkFields()
    }

    fun comeIn(navController: NavController) {
        val repositoryAuth = AuthRepository()
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        val repositoryMovies = MoviesRepository()
        val repositoryUser = UserRepository()
        viewModelScope.launch {
            repositoryAuth.login(
                LoginRequestBody(
                    username = _login.value,
                    password = _password.value
                )
            ).collect {}
            repositoryFavoriteMovies.getFavoriteMovies().collect {}
            repositoryMovies.getMovies(1).collect {}
            repositoryUser.getUserData().collect {}

            navController.navigate(Screens.NavBarScreen.route) {
                popUpTo(Screens.SignInScreen.route) {
                    inclusive = true
                }
            }
        }
    }
}