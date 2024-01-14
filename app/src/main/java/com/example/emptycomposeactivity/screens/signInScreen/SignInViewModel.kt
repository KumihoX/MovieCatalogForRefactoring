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

    private val _uiState = mutableStateOf(SignInScreenState())
    var uiState: State<SignInScreenState> = _uiState

    private fun checkFields() {
        val login = _uiState.value.login
        val password = _uiState.value.password
        if (login != "" && password != "") {
            _uiState.value =
                _uiState.value.copy(allFieldsFilled = login.isNotEmpty() && password.isNotEmpty())
        }
    }

    fun onLoginChange(newLogin: String) {
        _uiState.value = _uiState.value.copy(
            login = newLogin,
            emptyLogin = newLogin == ""
        )
        checkFields()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
        if (_uiState.value.password == "") {
            _uiState.value = _uiState.value.copy(emptyPassword = true)
        } else {
            if (_uiState.value.password.length < 8) {
                _uiState.value = _uiState.value.copy(notValidPassword = true)
            } else {
                _uiState.value = _uiState.value.copy(
                    emptyPassword = false,
                    notValidPassword = false
                )
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
                    username = _uiState.value.login,
                    password = _uiState.value.password
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

    data class SignInScreenState(
        val login: String = "",
        val emptyLogin: Boolean = false,
        val password: String = "",
        val notValidPassword: Boolean = false,
        val emptyPassword: Boolean = false,
        val allFieldsFilled: Boolean = false,
    )
}