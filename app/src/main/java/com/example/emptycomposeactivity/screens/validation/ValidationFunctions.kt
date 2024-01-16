package com.example.emptycomposeactivity.screens.validation

fun checkEmail(email: String): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()