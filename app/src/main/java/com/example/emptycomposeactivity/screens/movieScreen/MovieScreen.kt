package com.example.emptycomposeactivity.screens.movieScreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MovieScreen(navController: NavController) {
    val viewModel: MovieViewModel = viewModel()

    var heart = remember { viewModel.inFavorites }

    reviewDialog(
        viewModel = viewModel,
        { viewModel.onReviewChange(it) },
        { viewModel.changeAnon(it) })

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .pin()
            )
            {
                moviePoster(viewModel = viewModel, state = state)
            }

            val textSize = (24 + (30 - 18) * state.toolbarState.progress).sp
            val startPadding = (16 + 33 * (1 - state.toolbarState.progress)).dp

            Text(
                text = viewModel.movieDetails!!.name,
                color = White,
                fontSize = textSize,
                fontFamily = IBM,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .road(
                        whenCollapsed = TopStart,
                        whenExpanded = BottomStart
                    )
                    .padding(startPadding, 5.dp, 48.dp, 16.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = if (state.toolbarState.progress.equals(0.toFloat())) 1 else Int.MAX_VALUE
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                backButton()
                favoriteButton(viewModel, state, heart)
            }
        })
    {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
        {
            movieDescription(viewModel)
            aboutFilm()
            movieInformation(viewModel)
            genres()

            FlowRow(modifier = Modifier.padding(16.dp, 0.dp)) {
                genresButtons(viewModel)
            }

            reviews(viewModel)
        }
    }
}

@Composable
fun moviePoster(
    viewModel: MovieViewModel,
    state: CollapsingToolbarScaffoldState
) {
    var image =
        "https://kartinkin.net/uploads/posts/2021-07/thumbs/1626193375_35-kartinkin-com-p-chernii-ekran-fon-krasivo-36.jpg"
    if (viewModel.movieDetails!!.poster!!.isNotEmpty()) {
        image = viewModel.movieDetails!!.poster
    }

    if (state.toolbarState.progress > 0.1) {
        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = stringResource(R.string.promoted_film),
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Black),
                        startY = size.height / 4,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.Darken)
                    }
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
    }
}


@Composable
fun backButton() {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back),
            tint = White
        )
    }
}

@Composable
fun favoriteButton(
    viewModel: MovieViewModel,
    state: CollapsingToolbarScaffoldState,
    heart: State<Boolean>
) {
    if (state.toolbarState.progress < 0.1) {
        IconButton(onClick = {
            if (heart.value) {
                viewModel.deleteFavorites(viewModel.movieDetails!!.id)
            } else {
                viewModel.addFavorites(viewModel.movieDetails!!.id)
            }
        }) {
            Icon(
                if (heart.value) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                contentDescription = stringResource(R.string.favorites),
                tint = DarkRed
            )
        }
    }
}

@Composable
fun movieDescription(viewModel: MovieViewModel) {
    Text(
        text = if (viewModel.movieDetails!!.description!!.isNotEmpty())
            viewModel.movieDetails!!.description
        else "",
        color = White,
        fontFamily = IBM,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        modifier = Modifier.padding(16.dp, 16.dp)
    )
}

@Composable
fun aboutFilm() {
    Text(
        text = stringResource(R.string.about_film),
        color = White,
        fontSize = 16.sp,
        fontFamily = IBM,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 6.dp)
    )
}

@Composable
fun movieInformation(viewModel: MovieViewModel) {
    Column(modifier = Modifier.padding(16.dp, 0.dp)) {
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.year))
            addData(text = viewModel.movieDetails!!.year.toString())
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.country))
            addData(text = viewModel.movieDetails!!.country)
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.time))
            val text = viewModel.movieDetails!!.time.toString()
            addData(text = "$text мин.")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.tagline))
            val text = viewModel.movieDetails!!.tagline
            addData(text = "\" $text \"")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.director))
            addData(text = viewModel.movieDetails!!.director)
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.budget))
            val text = viewModel.movieDetails!!.budget.toString()
            addData(text = "$ $text")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            addText(text = stringResource(R.string.fees))
            val text = viewModel.movieDetails!!.fees.toString()
            addData(text = "$ $text")
        }
        Row()
        {
            addText(text = stringResource(R.string.age))
            val text = viewModel.movieDetails!!.ageLimit.toString()
            addData(text = "$text+")
        }
    }
}

@Composable
fun genres() {
    Text(
        text = stringResource(R.string.genres),
        color = White,
        fontSize = 16.sp,
        fontFamily = IBM,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(16.dp, 14.dp, 16.dp, 8.dp)
    )
}

@Composable
fun genresButtons(viewModel: MovieViewModel) {
    if (viewModel.movieDetails!!.genres!!.isNotEmpty()) {
        val sizeGenres = viewModel.movieDetails!!.genres!!.size

        var curGenre = 0
        while (curGenre != sizeGenres) {
            val text = viewModel.movieDetails!!.genres[curGenre].name
            addGenres(text = "$text")
            curGenre++
        }
    }
}

@Composable
fun reviews(viewModel: MovieViewModel) {
    val userReviewExist = remember { viewModel.userReview }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {
        Text(
            text = stringResource(R.string.reviews),
            color = White,
            fontSize = 16.sp,
            fontFamily = IBM,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(0.dp, 14.dp, 0.dp, 8.dp)
                .align(CenterStart)
        )

        if (!userReviewExist.value) {
            IconButton(
                modifier = Modifier.align(CenterEnd),
                onClick = { viewModel.interactionWithReviewDialog(true) },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.app_info),
                    tint = DarkRed
                )
            }
        }
    }

    var userReview = viewModel.userReviewPosition

    if (userReview != null && userReviewExist.value) {
        listOfReviewsWithUsers(viewModel, userReview)
    } else {
        listOfReviewsWithoutUsers(viewModel)
    }
}

@Composable
fun addText(text: String) {
    Text(
        text = text,
        modifier = Modifier.width(100.dp),
        color = TextGray,
        fontSize = 12.sp,
        fontFamily = montserrat,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun addData(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp, 0.dp, 0.dp, 0.dp)
            .width(220.dp),
        color = White,
        fontSize = 12.sp,
        fontFamily = montserrat,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun addGenres(text: String) {
    Button(
        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp),
        onClick = {},
        enabled = false,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = DarkRed,
            disabledContentColor = White
        ),
    ) {
        Text(
            text = text,
            color = White,
            fontSize = 12.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun addUserReview(viewModel: MovieViewModel, userReview: Int) {
    val reviewDescription = remember { viewModel.reviewText }
    val reviewRating = remember { viewModel.rating }

    viewModel.correctDateTime(userReview)
    var image = ""
    var nickName = ""

    if (viewModel.movieDetails!!.reviews[userReview].author == null) {
        image =
            "https://s3-alpha-sig.figma.com/img/a92b/ba97/a13937d71ea4ab29b068a92fd325aa74?Expires=1668988800&Signature=PMOWOcFshz~iLSRZLvi6WE~CKCngLi7WpMoR44oMZEIROtNCJjaX3UxugCdLhzxYOX~iFsQ2YEt9GAe0yCLP0l8F4W7V-Ndc-3NFxenpkVmji5IweylmDYJ7ratNHIZ6NroftMSLiaPlglIssrOl0tg0NR~xPjrWKQLqOXLp9wFuoSvIm7IAcB4vNapJnOhMRF1Q9u1Da1h5H3Cl79Btg4WaB09aF7Yrf0IonCKszUYr189k6N7nDuQ5UgL7H9VeVtzkTNu1Y0SnjWYHONqOWHe8Q~3m3jo8eoAkX2OGYpm-QWKbJFGnqCbZkZoA~w3L8WVmSI4a6OHj8k-i16OStA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        nickName = stringResource(R.string.anonym)
    } else {
        image = viewModel.movieDetails!!.reviews[userReview].author!!.avatar
        nickName = viewModel.movieDetails!!.reviews[userReview].author!!.nickName
    }

    ReviewCard(
        viewModel = viewModel,
        image = image,
        nickName = nickName,
        description = reviewDescription.value,
        data = viewModel.createDateTime.value.toString(),
        rating = reviewRating.value.toString(),
        isUser = true
    )
}

@Composable
fun listOfReviewsWithoutUsers(viewModel: MovieViewModel, userReview: Int? = -1) {
    val countReviews = viewModel.movieDetails!!.reviews.size

    var curReview = 0
    while (curReview != countReviews) {
        if (curReview != userReview) {
            viewModel.correctDateTime(curReview)
            var image = ""
            var nickName = ""
            if (viewModel.movieDetails!!.reviews[curReview].author == null) {
                image =
                    "https://s3-alpha-sig.figma.com/img/a92b/ba97/a13937d71ea4ab29b068a92fd325aa74?Expires=1668988800&Signature=PMOWOcFshz~iLSRZLvi6WE~CKCngLi7WpMoR44oMZEIROtNCJjaX3UxugCdLhzxYOX~iFsQ2YEt9GAe0yCLP0l8F4W7V-Ndc-3NFxenpkVmji5IweylmDYJ7ratNHIZ6NroftMSLiaPlglIssrOl0tg0NR~xPjrWKQLqOXLp9wFuoSvIm7IAcB4vNapJnOhMRF1Q9u1Da1h5H3Cl79Btg4WaB09aF7Yrf0IonCKszUYr189k6N7nDuQ5UgL7H9VeVtzkTNu1Y0SnjWYHONqOWHe8Q~3m3jo8eoAkX2OGYpm-QWKbJFGnqCbZkZoA~w3L8WVmSI4a6OHj8k-i16OStA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
                nickName = stringResource(R.string.anonym)
            } else {
                image = viewModel.movieDetails!!.reviews[curReview].author!!.avatar
                nickName = viewModel.movieDetails!!.reviews[curReview].author!!.nickName
            }
            ReviewCard(
                viewModel = viewModel,
                image = image,
                nickName = nickName,
                description = viewModel.movieDetails!!.reviews[curReview].reviewText,
                data = viewModel.createDateTime.value.toString(),
                rating = viewModel.movieDetails!!.reviews[curReview].rating.toString(),
                isUser = false
            )
        }
        curReview++
    }
}

@Composable
fun listOfReviewsWithUsers(viewModel: MovieViewModel, userReview: Int) {
    addUserReview(viewModel, userReview)
    listOfReviewsWithoutUsers(viewModel = viewModel, userReview = userReview)
}

