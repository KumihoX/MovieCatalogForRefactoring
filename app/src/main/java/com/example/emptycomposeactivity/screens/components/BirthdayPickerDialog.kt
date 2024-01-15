package com.example.emptycomposeactivity.screens.components

import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.emptycomposeactivity.R
import java.util.Calendar
import java.util.Date

@Composable
fun BirthdayPickerDialog(
    onBirthdateChange: (Int, Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    val context = LocalContext.current

    val mDatePickerDialog = android.app.DatePickerDialog(
        context,
        R.style.MyDatePickerDialogTheme,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            onBirthdateChange(mDayOfMonth, mMonth, mYear)
        },
        year,
        month,
        day
    )
    mDatePickerDialog.show()
}