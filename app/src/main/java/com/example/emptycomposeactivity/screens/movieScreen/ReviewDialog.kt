package com.example.emptycomposeactivity.screens.movieScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.ui.theme.*

@Composable
fun reviewDialog(
    viewModel: MovieViewModel,
    onReviewChange: (String) -> Unit,
    changeAnon: (Boolean) -> Unit,
    edit: Boolean? = false
) {
    val openDialog: Boolean by remember { viewModel.openReviewDialog }
    val text: String by remember { viewModel.reviewText }
    val checkedState: Boolean by remember { viewModel.checkedState }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.interactionWithReviewDialog(false) },
            modifier = Modifier.wrapContentSize(),
            buttons = {
                Column(modifier = Modifier.padding(16.dp, 16.dp))
                {
                    Text(
                        text = stringResource(R.string.leave_review),
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = White,
                        fontSize = 24.sp,
                        fontFamily = IBM,
                        fontWeight = FontWeight.Bold
                    )

                    starRatingBar(viewModel = viewModel)
                    OutlinedTextField(
                        value = text,
                        onValueChange = onReviewChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 16.dp, 0.dp, 16.dp)
                            .height(120.dp),
                        placeholder = {
                            Text(
                                stringResource(R.string.review),
                                fontFamily = IBM,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.7.sp
                            )
                        },
                        enabled = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = White,
                            unfocusedBorderColor = White,
                            focusedBorderColor = White,
                            placeholderColor = ReviewTextGray,
                            cursorColor = Black,
                            textColor = Black
                        ),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(R.string.anonyms_review),
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                                .align(Alignment.CenterVertically),
                            color = White,
                            fontSize = 16.sp,
                            fontFamily = IBM,
                            fontWeight = FontWeight.Medium,
                        )
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = changeAnon,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(0.dp, 0.dp, 0.dp, 16.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = ReviewDialogBack,
                                uncheckedColor = GrayFaded,
                                checkmarkColor = DarkRed
                            )
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DarkRed,
                            contentColor = White
                        ),
                        onClick = {
                            if (edit == true) {
                                viewModel.changeReview()
                            } else {
                                viewModel.addReview()
                            }
                            viewModel.interactionWithReviewDialog(false)
                        }
                    )
                    {
                        Text(
                            stringResource(R.string.save),
                            color = White,
                            fontSize = 16.sp,
                            fontFamily = IBM,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ReviewDialogBack,
                            contentColor = DarkRed
                        ),
                        onClick = { viewModel.interactionWithReviewDialog(false) }
                    )
                    {
                        Text(
                            stringResource(R.string.cancel),
                            color = DarkRed,
                            fontSize = 16.sp,
                            fontFamily = IBM,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = ReviewDialogBack
        )
    }
}

@Composable
fun starRatingBar(
    modifier: Modifier = Modifier,
    stars: Int = 10,
    starsColor: Color = DarkRed,
    unfilledStarsColor: Color = Gray,
    viewModel: MovieViewModel,
) {
    val rating: Int by remember { viewModel.rating }
    val filledStars = rating
    val unfilledStars = stars - rating

    Row(modifier = modifier.padding(0.dp, 16.dp)) {
        var star = 0
        repeat(filledStars) {
            val cur = star
            Icon(
                modifier = Modifier.clickable {
                    viewModel.newRating(cur + 1)
                },
                painter = painterResource(R.drawable.star_foreground),
                contentDescription = null,
                tint = starsColor
            )
            Spacer(modifier = Modifier.width(5.dp))

            star++
        }

        repeat(unfilledStars) {
            val cur = star
            Icon(
                modifier = Modifier.clickable { viewModel.newRating(cur + 1) },
                painter = painterResource(R.drawable.star_background),
                contentDescription = null,
                tint = unfilledStarsColor
            )
            Spacer(modifier = Modifier.width(5.dp))

            star++
        }
    }
}