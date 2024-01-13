package com.example.emptycomposeactivity.screens.signUpScreen

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emptycomposeactivity.network.auth.AuthRepository
import com.example.emptycomposeactivity.network.auth.LoginRequestBody
import com.example.emptycomposeactivity.network.auth.RegisterRequestBody
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.user.UserRepository
import kotlinx.coroutines.launch
import java.util.*

class SignUpViewModel : ViewModel() {

    val emptyMessage = R.string.empty
    val incorrectPasswordMessage = R.string.wrong_password
    val invalidEmailMessage = R.string.wrong_email
    val notEqualMessage = R.string.not_equal

    private val _login = mutableStateOf("")
    var login: State<String> = _login

    private val _emptyLogin = mutableStateOf(false)
    var emptyLogin: State<Boolean> = _emptyLogin

    private val _email = mutableStateOf("")
    var email: State<String> = _email

    private val _emptyEmail = mutableStateOf(false)
    var emptyEmail: State<Boolean> = _emptyEmail

    private val _name = mutableStateOf("")
    var name: State<String> = _name

    private val _emptyName = mutableStateOf(false)
    var emptyName: State<Boolean> = _emptyName

    private val _password = mutableStateOf("")
    var password: State<String> = _password

    private val _notValidPassword = mutableStateOf(false)
    var notValidPassword: State<Boolean> = _notValidPassword

    private val _emptyPassword = mutableStateOf(false)
    var emptyPassword: State<Boolean> = _emptyPassword

    private val _confirmPassword = mutableStateOf("")
    var confirmPassword: State<String> = _confirmPassword

    private val _emptyConfirmPassword = mutableStateOf(false)
    var emptyConfirmPassword: State<Boolean> = _emptyConfirmPassword

    private val _dateOfBirth = mutableStateOf("")
    var dateOfBirth: State<String> = _dateOfBirth

    private val _manIsPressed = mutableStateOf(false)
    var manIsPressed: State<Boolean> = _manIsPressed

    private val _womanIsPressed = mutableStateOf(false)
    var womanIsPressed: State<Boolean> = _womanIsPressed

    private var _gender: Int = -1

    private val _equality = mutableStateOf(true)
    var equality: State<Boolean> = _equality

    private val _correct = mutableStateOf(true)
    var correct: State<Boolean> = _correct

    private val _allFieldsFilled = mutableStateOf(false)
    var allFieldsFilled: State<Boolean> = _allFieldsFilled


    private var correctBirthday: String = ""

    private fun passwordComparison() {
        _equality.value = _password.value == _confirmPassword.value
    }

    private fun correctEmail() {
        _correct.value =
            _email.value.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true
    }

    private fun checkGender() {
        _gender = if (_manIsPressed.value || _womanIsPressed.value) {
            if (_womanIsPressed.value) {
                1
            } else
                0
        } else
            -1
    }

    private fun correctBirth() {
        val list = _dateOfBirth.value.split(".").toMutableList()
        if (list[0].toInt() < 10) {
            list[0] = "0" + list[0]
        }
        if (list[1].toInt() < 10) {
            list[1] = "0" + list[1]
        }
        correctBirthday = list[2] + "-" + list[1] + "-" + list[0]
    }

    private fun checkFields() {
        val login = _login.value
        val email = _email.value
        val name = _name.value
        val password = _password.value
        val confirmPassword = _confirmPassword.value
        val dateOfBirth = _dateOfBirth.value
        val notValidPassword = _notValidPassword.value
        checkGender()

        if (login.isNotEmpty() && email.isNotEmpty()
            && name.isNotEmpty() && password.isNotEmpty()
            && confirmPassword.isNotEmpty() && dateOfBirth.isNotEmpty()
            && !notValidPassword
        ) {
            if (_gender != -1) {
                _allFieldsFilled.value = _equality.value == _correct.value == true
            }
        } else
            _allFieldsFilled.value = false
    }

    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
        _emptyLogin.value = _login.value == ""
        checkFields()
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emptyEmail.value = _email.value == ""
        correctEmail()
        checkFields()
    }

    fun onNameChange(newName: String) {
        _name.value = newName
        _emptyName.value = _name.value == ""
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
        passwordComparison()
        checkFields()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _emptyConfirmPassword.value = _confirmPassword.value == ""
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
                _dateOfBirth.value = "$mDayOfMonth.${mMonth + 1}.$mYear"
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
                _manIsPressed.value = !_manIsPressed.value
                if (_womanIsPressed.value) {
                    _womanIsPressed.value = !_womanIsPressed.value
                }

                checkFields()
            }

            2 -> {
                _womanIsPressed.value = !_womanIsPressed.value
                if (_manIsPressed.value) {
                    _manIsPressed.value = !_manIsPressed.value
                }

                checkFields()
            }
        }
    }

    fun register(navController: NavController) {
        val repositoryAuth = AuthRepository()
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        val repositoryMovies = MoviesRepository()
        val repositoryUser = UserRepository()
        correctBirth()
        viewModelScope.launch {
            repositoryAuth.register(
                RegisterRequestBody(
                    userName = _login.value,
                    name = _name.value,
                    password = _password.value,
                    email = _email.value,
                    birthDate = correctBirthday,
                    gender = _gender
                )
            ).collect {}

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
                popUpTo(Screens.SignUpScreen.route) {
                    inclusive = true
                }
                popUpTo(Screens.SignInScreen.route) {
                    inclusive = true
                }
            }
        }
    }
}