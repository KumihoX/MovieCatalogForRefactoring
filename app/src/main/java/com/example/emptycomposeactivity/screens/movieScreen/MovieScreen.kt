package com.example.emptycomposeactivity.screens.movieScreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.screens.movieScreen.MovieViewModel.MovieScreenState
import com.example.emptycomposeactivity.ui.theme.Black
import com.example.emptycomposeactivity.ui.theme.DarkRed
import com.example.emptycomposeactivity.ui.theme.IBM
import com.example.emptycomposeactivity.ui.theme.TextGray
import com.example.emptycomposeactivity.ui.theme.White
import com.example.emptycomposeactivity.ui.theme.montserrat
import com.google.accompanist.flowlayout.FlowRow
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MovieScreen() {
    val viewModel: MovieViewModel = viewModel()
    val uiState: MovieScreenState by remember { viewModel.uiState }

    var heart = remember { viewModel.uiState.value.inFavorites }

    ReviewDialog(
        viewModel = viewModel,
        state = uiState,
        { viewModel.onReviewChange(it) },
        { viewModel.changeAnon() })

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
                MoviePoster(viewModel = viewModel, state = state)
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
                BackButton()
                FavoriteButton(viewModel, state, heart)
            }
        })
    {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
        {
            MovieDescription(viewModel)
            AboutFilm()
            MovieInformation(viewModel)
            Genres()

            FlowRow(modifier = Modifier.padding(16.dp, 0.dp)) {
                GenresButtons(viewModel)
            }

            Reviews(viewModel, uiState)
        }
    }
}

@Composable
fun MoviePoster(
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
fun BackButton() {
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
fun FavoriteButton(
    viewModel: MovieViewModel,
    state: CollapsingToolbarScaffoldState,
    heart: Boolean
) {
    if (state.toolbarState.progress < 0.1) {
        IconButton(onClick = {
            if (heart) {
                viewModel.deleteFavorites(viewModel.movieDetails!!.id)
            } else {
                viewModel.addFavorites(viewModel.movieDetails!!.id)
            }
        }) {
            Icon(
                if (heart) {
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
fun MovieDescription(viewModel: MovieViewModel) {
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
fun AboutFilm() {
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
fun MovieInformation(viewModel: MovieViewModel) {
    Column(modifier = Modifier.padding(16.dp, 0.dp)) {
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.year))
            AddData(text = viewModel.movieDetails!!.year.toString())
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.country))
            AddData(text = viewModel.movieDetails!!.country)
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.time))
            val text = viewModel.movieDetails!!.time.toString()
            AddData(text = "$text мин.")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.tagline))
            val text = viewModel.movieDetails!!.tagline
            AddData(text = "\" $text \"")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.director))
            AddData(text = viewModel.movieDetails!!.director)
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.budget))
            val text = viewModel.movieDetails!!.budget.toString()
            AddData(text = "$ $text")
        }
        Row(modifier = Modifier.padding(0.dp, 2.dp))
        {
            AddText(text = stringResource(R.string.fees))
            val text = viewModel.movieDetails!!.fees.toString()
            AddData(text = "$ $text")
        }
        Row()
        {
            AddText(text = stringResource(R.string.age))
            val text = viewModel.movieDetails!!.ageLimit.toString()
            AddData(text = "$text+")
        }
    }
}

@Composable
fun Genres() {
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
fun GenresButtons(viewModel: MovieViewModel) {
    if (viewModel.movieDetails!!.genres!!.isNotEmpty()) {
        val sizeGenres = viewModel.movieDetails!!.genres!!.size

        var curGenre = 0
        while (curGenre != sizeGenres) {
            val text = viewModel.movieDetails!!.genres[curGenre].name
            AddGenres(text = "$text")
            curGenre++
        }
    }
}

@Composable
fun Reviews(viewModel: MovieViewModel, state: MovieScreenState) {
    val userReviewExist = remember { state.userReview }

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

        if (!userReviewExist) {
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

    if (userReview != null && userReviewExist) {
        ListOfReviewsWithUsers(viewModel, state, userReview)
    } else {
        ListOfReviewsWithoutUsers(viewModel, state)
    }
}

@Composable
fun AddText(text: String) {
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
fun AddData(text: String) {
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
fun AddGenres(text: String) {
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
fun AddUserReview(viewModel: MovieViewModel, state: MovieScreenState, userReview: Int) {
    val reviewDescription = remember { state.reviewText }
    val reviewRating = remember { state.rating }

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
        state = state,
        image = image,
        nickName = nickName,
        description = reviewDescription,
        data = state.createDateTime,
        rating = reviewRating.toString(),
        isUser = true
    )
}

@Composable
fun ListOfReviewsWithoutUsers(
    viewModel: MovieViewModel,
    state: MovieScreenState,
    userReview: Int? = -1
) {
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
                state = state,
                image = image,
                nickName = nickName,
                description = viewModel.movieDetails!!.reviews[curReview].reviewText,
                data = state.createDateTime,
                rating = viewModel.movieDetails!!.reviews[curReview].rating.toString(),
                isUser = false
            )
        }
        curReview++
    }
}

@Composable
fun ListOfReviewsWithUsers(viewModel: MovieViewModel, state: MovieScreenState, userReview: Int) {
    AddUserReview(viewModel, state, userReview)
    ListOfReviewsWithoutUsers(viewModel = viewModel, state = state, userReview = userReview)
}

