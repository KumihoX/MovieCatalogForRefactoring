package com.example.emptycomposeactivity.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.ui.theme.Green
import com.example.emptycomposeactivity.ui.theme.IBM
import com.example.emptycomposeactivity.ui.theme.Red
import com.example.emptycomposeactivity.ui.theme.White

@Composable
fun MovieCards(
    viewModel: MainViewModel,
    id: String,
    title: String,
    year: Int,
    country: String,
    image: String,
    genres: String,
    rating: Float,
    openMovieDescription: () -> Unit
) {

    val color = ColorUtils.blendARGB(Red.toArgb(), Green.toArgb(), rating * 0.1f)

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            Modifier
                .wrapContentSize()
                .clickable {
                    viewModel.getDetails(id) { openMovieDescription() }
                }
                .padding(0.dp, 8.dp),
            backgroundColor = colorResource(R.color.white),
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = colorResource(R.color.black))
            )
            {
                Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = stringResource(R.string.movie_poster),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .height(144.dp)
                        .weight(0.3f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, end = 16.dp)
                        .weight(0.7f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        color = White,
                        fontFamily = IBM,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$year • $country",
                        color = colorResource(R.color.white),
                        style = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = genres,
                        color = colorResource(R.color.white),
                        style = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        contentPadding = PaddingValues(),
                        modifier = Modifier.defaultMinSize(minWidth = 42.dp, minHeight = 28.dp),
                        onClick = {},
                        enabled = false,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledBackgroundColor = Color(color),
                            disabledContentColor = White
                        )
                    ) {
                        Text(
                            text = String.format("%.1f", rating),
                            color = White,
                            fontFamily = IBM,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}