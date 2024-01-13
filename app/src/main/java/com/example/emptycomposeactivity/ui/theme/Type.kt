package com.example.emptycomposeactivity.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.emptycomposeactivity.R

val IBM = FontFamily(
    Font(R.font.ibm_regular),
    Font(R.font.ibm_bold, FontWeight.Bold),
    Font(R.font.ibm_medium, FontWeight.Medium)
)

val montserrat = FontFamily(Font(R.font.montserrat_medium, FontWeight.Medium))

// Set of Material typography styles to start with
val Typography = Typography(
    body2 = TextStyle(
        fontFamily = IBM,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = IBM,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        fontFamily = IBM,
        fontWeight = FontWeight.Bold
    )
)
