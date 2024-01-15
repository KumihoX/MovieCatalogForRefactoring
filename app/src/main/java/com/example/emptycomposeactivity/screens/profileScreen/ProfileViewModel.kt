package com.example.emptycomposeactivity.screens.profileScreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.auth.AuthRepository
import com.example.emptycomposeactivity.network.user.UserData
import com.example.emptycomposeactivity.network.user.UserRepository
import com.example.emptycomposeactivity.screens.enums.Gender
import com.example.emptycomposeactivity.screens.enums.toGender
import com.example.emptycomposeactivity.screens.enums.toInt
import com.example.emptycomposeactivity.screens.ext.toRequiredDateFormat
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel : ViewModel() {
    var userData = Network.userData

    val emptyMessage = R.string.empty
    val invalidEmailMessage = R.string.wrong_email

    private val _uiState = mutableStateOf(ProfileScreenState())
    var uiState: State<ProfileScreenState> = _uiState

    private var _gender = Gender.NOT_SELECTED

    private var correctBirthday: String = ""

    private var hasChanges = false

    private var startBirthday = ""

    private var dataChange = false

    init {
        updateUiState()
    }

    private fun updateUiState() {
        if (userData != null) {
            birthdayForOutput()
            _uiState.value = _uiState.value.copy(
                nick = userData!!.nickName,
                email = userData!!.email,
                name = userData!!.name,
                url = userData!!.avatarLink,
            )
            _gender = userData!!.gender.toGender()
            checkGender()
        }
    }

    private fun birthdayForOutput() {
        val dataInBirthday = userData!!.birthDate.substringBefore("T").split('-')
        _uiState.value =
            _uiState.value.copy(dateOfBirth = dataInBirthday[2] + "." + dataInBirthday[1] + "." + dataInBirthday[0])

        startBirthday = _uiState.value.dateOfBirth
    }


    private fun correctEmail() {
        _uiState.value = _uiState.value.copy(correct = _uiState.value.email.let {
            android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        })
    }

    private fun checkGender() {
        if (_gender == Gender.WOMAN) {
            _uiState.value = _uiState.value.copy(womanIsPressed = true)
        } else
            _uiState.value = _uiState.value.copy(manIsPressed = true)
    }

    private fun checkData() {
        val start = startBirthday.split('.')
        val current = _uiState.value.dateOfBirth.split(".")

        dataChange = !(start[0].toInt() == current[0].toInt() &&
                start[1].toInt() == current[1].toInt() &&
                start[2].toInt() == current[2].toInt())

    }

    private fun changes() {
        checkData()
        with(_uiState.value) {
            hasChanges = email != userData!!.email || url != userData!!.avatarLink ||
                    name != userData!!.name || dataChange ||
                    _gender != userData!!.gender.toGender()
        }

    }

    private fun checkFields() {
        changes()

        val email = _uiState.value.email
        val name = _uiState.value.name
        val dateOfBirth = _uiState.value.dateOfBirth

        if (email.isNotEmpty() && name.isNotEmpty() && dateOfBirth.isNotEmpty()
        ) {
            _uiState.value =
                _uiState.value.copy(allFieldsFilled = _uiState.value.correct == hasChanges)
        } else
            _uiState.value = _uiState.value.copy(allFieldsFilled = false)
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            emptyEmail = newEmail == ""
        )
        correctEmail()
        checkFields()
    }

    fun onUrlChange(newUrl: String) {
        _uiState.value = _uiState.value.copy(
            url = newUrl
        )
        checkFields()
    }

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            name = newName,
            emptyName = newName == ""
        )
        checkFields()
    }

    @SuppressLint("SimpleDateFormat")
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
                _uiState.value =
                    _uiState.value.copy(dateOfBirth = "$mDayOfMonth.${mMonth + 1}.$mYear")
                checkFields()
            },
            year,
            month,
            day
        )

        mDatePickerDialog.show()
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

    fun save() {
        val repositoryUser = UserRepository()
        correctBirthday = _uiState.value.dateOfBirth.toRequiredDateFormat()

        viewModelScope.launch {
            repositoryUser.putUserData(
                UserData(
                    id = userData!!.id,
                    nickName = userData!!.nickName,
                    email = _uiState.value.email,
                    avatarLink = _uiState.value.url,
                    name = _uiState.value.name,
                    birthDate = correctBirthday,
                    gender = _gender.toInt()
                )
            )
            repositoryUser.getUserData().collect {
                userData = it
                updateUiState()
            }
            _uiState.value = _uiState.value.copy(
                allFieldsFilled = false
            )
            hasChanges = false
        }
    }

    fun logout() {
        val repositoryAuth = AuthRepository()

        viewModelScope.launch {
            repositoryAuth.logout().collect {}

            Network.token = null
            Network.favoriteMovies = null
            Network.movies = null
            Network.movieDetail = null
            Network.userData = null
        }
    }

    data class ProfileScreenState(
        val email: String = "",
        val emptyEmail: Boolean = false,
        val name: String = "",
        val emptyName: Boolean = false,
        val dateOfBirth: String = "",
        val url: String = "",
        val nick: String = "",
        val manIsPressed: Boolean = false,
        val womanIsPressed: Boolean = false,
        val correct: Boolean = true,
        val allFieldsFilled: Boolean = false,
    )
}