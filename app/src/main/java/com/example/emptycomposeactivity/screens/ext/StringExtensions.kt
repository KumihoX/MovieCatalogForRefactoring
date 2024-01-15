package com.example.emptycomposeactivity.screens.ext

private fun String.convertToTwoDigitDateFormat(): String =
    if (this.length < 2) "0$this" else this

fun String.convertToRequiredExternalDateFormat(): String {
    val dateParts = this.split(".")

    val year = dateParts[2]
    val month = dateParts[1].convertToTwoDigitDateFormat()
    val day = dateParts[0].convertToTwoDigitDateFormat()

    return "$year-$month-$day"
}

fun String.convertToRequiredUIDateFormat(): String {
    val dateParts = this.substringBefore("T").split('-')

    val year = dateParts[0]
    val month = dateParts[1]
    val day = dateParts[2]

    return "$year.$month.$day"
}