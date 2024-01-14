package com.example.emptycomposeactivity.screens.movieScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.screens.movieScreen.MovieViewModel.MovieScreenState
import com.example.emptycomposeactivity.ui.theme.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewCard(
    viewModel: MovieViewModel,
    state: MovieScreenState,
    image: String,
    nickName: String,
    description: String,
    data: String,
    rating: String,
    isUser: Boolean
) {

    var avatar = image

    if (avatar == "") {
        avatar =
            "https://s3-alpha-sig.figma.com/img/a92b/ba97/a13937d71ea4ab29b068a92fd325aa74?Expires=1668988800&Signature=PMOWOcFshz~iLSRZLvi6WE~CKCngLi7WpMoR44oMZEIROtNCJjaX3UxugCdLhzxYOX~iFsQ2YEt9GAe0yCLP0l8F4W7V-Ndc-3NFxenpkVmji5IweylmDYJ7ratNHIZ6NroftMSLiaPlglIssrOl0tg0NR~xPjrWKQLqOXLp9wFuoSvIm7IAcB4vNapJnOhMRF1Q9u1Da1h5H3Cl79Btg4WaB09aF7Yrf0IonCKszUYr189k6N7nDuQ5UgL7H9VeVtzkTNu1Y0SnjWYHONqOWHe8Q~3m3jo8eoAkX2OGYpm-QWKbJFGnqCbZkZoA~w3L8WVmSI4a6OHj8k-i16OStA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
    }

    val color = ColorUtils.blendARGB(Red.toArgb(), Green.toArgb(), rating.toInt() * 0.1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(16.dp, 4.dp),
        backgroundColor = Black,
        border = BorderStroke(1.dp, White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp, 8.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row()
                {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(White)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(avatar),
                            contentDescription = stringResource(R.string.user_avatar),
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }

                    Column()
                    {
                        Text(
                            text = nickName,
                            modifier = Modifier
                                .padding(8.dp, 0.dp, 0.dp, 0.dp),
                            //.align(CenterVertically),
                            color = White,
                            fontSize = 16.sp,
                            fontFamily = IBM,
                            fontWeight = FontWeight.Medium,
                        )
                        if (isUser) {
                            Text(
                                text = "Мой отзыв",
                                modifier = Modifier
                                    .padding(8.dp, 0.dp, 0.dp, 0.dp),
                                //.align(CenterVertically),
                                color = Gray,
                                fontSize = 12.sp,
                                fontFamily = IBM,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }


                Button(
                    contentPadding = PaddingValues(16.dp, 4.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .defaultMinSize(minWidth = 42.dp, minHeight = 28.dp),
                    onClick = {},
                    enabled = false,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = Color(color),
                        disabledContentColor = White
                    )
                ) {
                    Text(
                        text = rating,
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = IBM,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Text(
                text = description,
                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 4.dp),
                color = White,
                fontSize = 14.sp,
                fontFamily = IBM,
                fontWeight = FontWeight.Normal,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(0.dp, 0.dp, 0.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Bottom
            ) {
                Text(
                    text = data,
                    color = Gray,
                    fontSize = 12.sp,
                    fontFamily = IBM,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )

                if (isUser) {

                    reviewDialog(
                        viewModel = viewModel,
                        state = state,
                        { viewModel.onReviewChange(it) },
                        { viewModel.changeAnon(it) },
                        edit = true
                    )
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                    )
                    {
                        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false)
                        {
                            IconButton(onClick = { viewModel.interactionWithReviewDialog(true) }) {
                                Icon(
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 8.dp, 0.dp),
                                    painter = painterResource(R.drawable.edit_icon),
                                    contentDescription = stringResource(R.string.icon_edit),
                                    tint = Color.Unspecified
                                )
                            }
                            IconButton(onClick = { viewModel.deleteReview() }) {
                                Icon(
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 1.dp, 0.dp),
                                    painter = painterResource(R.drawable.delete_icon),
                                    contentDescription = stringResource(R.string.icon_delete),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

