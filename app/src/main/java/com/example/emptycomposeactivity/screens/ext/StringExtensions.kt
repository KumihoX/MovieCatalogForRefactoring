package com.example.emptycomposeactivity.screens.ext

private fun String.convertToTwoDigitDateFormat(): String =
    if (this.length < 2) "0$this" else this

fun String.toRequiredDateFormat(): String {
    val dateParts = this.split(".")

    val year = dateParts[2]
    val month = dateParts[1].convertToTwoDigitDateFormat()
    val day = dateParts[0].convertToTwoDigitDateFormat()

    return "$year-$month-$day"
}