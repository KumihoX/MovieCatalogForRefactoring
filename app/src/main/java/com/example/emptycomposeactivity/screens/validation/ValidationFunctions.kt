package com.example.emptycomposeactivity.screens.validation

fun checkEmail(email: String): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun checkFieldsOnEmptiness(
    login: String? = null,
    name: String? = null,
    email: String? = null,
    password: String? = null,
    confirmPassword: String? = null,
    dateOfBirth: String? = null
): Boolean {
    val isLoginEmpty = login?.let { login.isEmpty() } ?: false
    val isNameEmpty = name?.let { name.isEmpty() } ?: false
    val isEmailEmpty = email?.let { email.isEmpty() } ?: false
    val isPasswordEmpty = password?.let { password.isEmpty() } ?: false
    val isConfirmPasswordEmpty = confirmPassword?.let { confirmPassword.isEmpty() } ?: false
    val isDateOfBirthEmpty = dateOfBirth?.let { dateOfBirth.isEmpty() } ?: false

    return !isLoginEmpty && !isNameEmpty && !isEmailEmpty && !isPasswordEmpty
            && !isConfirmPasswordEmpty && !isDateOfBirthEmpty
}