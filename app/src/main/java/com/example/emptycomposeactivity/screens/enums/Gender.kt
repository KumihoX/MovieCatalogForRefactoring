package com.example.emptycomposeactivity.screens.enums

enum class Gender {
    WOMAN,
    MAN,
    NOT_SELECTED
}

fun Gender.toInt(): Int = when (this) {
    Gender.WOMAN -> 1
    Gender.MAN -> 0
    Gender.NOT_SELECTED -> -1
}

fun Int.toGender(): Gender = when (this) {
    0 -> Gender.MAN
    1 -> Gender.WOMAN
    else -> Gender.NOT_SELECTED
}