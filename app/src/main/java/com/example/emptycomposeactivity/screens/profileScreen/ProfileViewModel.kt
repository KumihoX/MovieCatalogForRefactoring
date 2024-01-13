package com.example.emptycomposeactivity.screens.profileScreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.auth.AuthRepository
import com.example.emptycomposeactivity.network.user.UserData
import com.example.emptycomposeactivity.network.user.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel : ViewModel() {
    var userData = Network.userData

    val emptyMessage = R.string.empty
    val invalidEmailMessage = R.string.wrong_email

    private val _email = mutableStateOf("")
    var email: State<String> = _email

    private val _emptyEmail = mutableStateOf(false)
    var emptyEmail: State<Boolean> = _emptyEmail

    private val _name = mutableStateOf("")
    var name: State<String> = _name

    private val _emptyName = mutableStateOf(false)
    var emptyName: State<Boolean> = _emptyName

    private val _dateOfBirth = mutableStateOf("")
    var dateOfBirth: State<String> = _dateOfBirth

    private val _url = mutableStateOf("")
    var url: State<String> = _url

    private val _nick = mutableStateOf("")
    var nick: State<String> = _nick

    private val _manIsPressed = mutableStateOf(false)
    var manIsPressed: State<Boolean> = _manIsPressed

    private val _womanIsPressed = mutableStateOf(false)
    var womanIsPressed: State<Boolean> = _womanIsPressed

    private val _correct = mutableStateOf(true)
    var correct: State<Boolean> = _correct

    private val _allFieldsFilled = mutableStateOf(false)
    var allFieldsFilled: State<Boolean> = _allFieldsFilled

    private var _gender = 0

    private var correctBirthday: String = ""

    private var hasChanges = false

    private var startBirthday = ""

    private var dataChange = false

    init {
        if (userData != null) {
            _nick.value = userData!!.nickName
            _email.value = userData!!.email
            _name.value = userData!!.name
            birthdayForOutput()
            _url.value = userData!!.avatarLink
            _gender = userData!!.gender
            checkGender()
        }
    }

    private fun birthdayForOutput() {
        val dataInBirthday = userData!!.birthDate.substringBefore("T").split('-')
        _dateOfBirth.value = dataInBirthday[2] + "." + dataInBirthday[1] + "." + dataInBirthday[0]
        startBirthday = _dateOfBirth.value
    }


    private fun correctEmail() {
        _correct.value =
            _email.value?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true
    }

    private fun checkGender() {
        if (_gender == 1) {
            _womanIsPressed.value = true
        } else
            _manIsPressed.value = true
    }

    private fun checkData() {
        val start = startBirthday.split('.')
        val current = _dateOfBirth.value.split(".")

        dataChange = !(start[0].toInt() == current[0].toInt() &&
                start[1].toInt() == current[1].toInt() &&
                start[2].toInt() == current[2].toInt())

    }

    private fun changes() {
        checkData()
        hasChanges = _email.value != userData!!.email || _url.value != userData!!.avatarLink ||
                _name.value != userData!!.name || dataChange ||
                _gender != userData!!.gender
    }

    private fun checkFields() {

        changes()

        val email = _email.value
        val name = _name.value
        val dateOfBirth = _dateOfBirth.value

        if (email.isNotEmpty() && name.isNotEmpty() && dateOfBirth.isNotEmpty()
        ) {
            _allFieldsFilled.value = _correct.value == hasChanges
        } else
            _allFieldsFilled.value = false
    }

    private fun correctBirth() {
        val list = _dateOfBirth.value.split(".").toMutableList()
        if (list[0].toInt() < 10) {
            list[0] = "0" + list[0].toInt().toString()
        }
        if (list[1].toInt() < 10) {
            list[1] = "0" + list[1].toInt().toString()
        }
        correctBirthday = list[2] + "-" + list[1] + "-" + list[0]
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emptyEmail.value = _email.value == ""
        correctEmail()
        checkFields()
    }

    fun onUrlChange(newUrl: String) {
        _url.value = newUrl
        checkFields()
    }

    fun onNameChange(newName: String) {
        _name.value = newName
        _emptyName.value = _name.value == ""
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
                _manIsPressed.value = true
                _womanIsPressed.value = false
                _gender = 0
                checkFields()
            }

            2 -> {
                _manIsPressed.value = false
                _womanIsPressed.value = true
                _gender = 1
                checkFields()
            }
        }
    }

    fun save() {
        val repositoryUser = UserRepository()
        correctBirth()

        viewModelScope.launch {
            repositoryUser.putUserData(
                UserData(
                    id = userData!!.id,
                    nickName = userData!!.nickName,
                    email = _email.value,
                    avatarLink = _url.value,
                    name = _name.value,
                    birthDate = correctBirthday,
                    gender = _gender
                )
            )
            repositoryUser.getUserData().collect {
                userData = it
                if (userData != null) {
                    _nick.value = userData!!.nickName
                    _email.value = userData!!.email
                    _name.value = userData!!.name
                    birthdayForOutput()
                    _url.value = userData!!.avatarLink
                    _gender = userData!!.gender
                    checkGender()
                }
            }
            _allFieldsFilled.value = false
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
}