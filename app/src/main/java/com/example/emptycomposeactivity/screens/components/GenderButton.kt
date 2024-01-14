package com.example.emptycomposeactivity.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.ui.theme.Black
import com.example.emptycomposeactivity.ui.theme.DarkRed
import com.example.emptycomposeactivity.ui.theme.White

@Composable
fun GenderButton(
    selectedWoman: Boolean,
    selectedMan: Boolean,
    buttonGenderIsPressed: (Int) -> Unit
) {
    val womanBack =
        if (selectedWoman) DarkRed else Black
    val manBack = if (selectedMan) DarkRed else Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp, 0.dp)
            .border(1.dp, White, RoundedCornerShape(8.dp))
    ) {
        Button(
            onClick = { buttonGenderIsPressed(1) },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = manBack,
                contentColor = White
            ),
            shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp),
        )
        {
            Text(
                stringResource(R.string.male),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }

        Divider(
            color = White,
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
        )

        Button(
            onClick = { buttonGenderIsPressed(2) },
            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = womanBack,
                contentColor = White
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        {
            Text(
                stringResource(R.string.female),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}
