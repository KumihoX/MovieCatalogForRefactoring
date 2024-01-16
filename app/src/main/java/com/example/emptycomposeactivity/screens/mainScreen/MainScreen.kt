package com.example.emptycomposeactivity.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.screens.mainScreen.MainViewModel.MainScreenState
import com.example.emptycomposeactivity.ui.theme.Black
import com.example.emptycomposeactivity.ui.theme.DarkRed
import com.example.emptycomposeactivity.ui.theme.IBM
import com.example.emptycomposeactivity.ui.theme.White

@Composable
fun MainScreen(movieDescription: () -> Unit) {

    val viewModel = MainViewModel()
    val state by remember { viewModel.uiState }
    val ready = remember { state.readyPage }

    if (ready) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            item { PromotedFilm(viewModel = viewModel, state = state) { movieDescription() } }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item { FavoritesList(viewModel = viewModel, state = state) }
            item { Gallery() }

            itemsIndexed(
                items = viewModel.movieList
            ) { index, item ->

                val lastIndex = viewModel.movieList.lastIndex

                if (index != 0) {
                    viewModel.createCorrectGenresString(item.genres)
                    viewModel.countRating(item.reviews)
                    val genres = viewModel.genresString
                    val rating = viewModel.rating

                    MovieCards(
                        viewModel = viewModel,
                        id = item.id,
                        title = item.name,
                        year = item.year,
                        country = item.country,
                        image = item.poster,
                        genres = genres,
                        rating = rating
                    ) { movieDescription() }
                }

                if (index == lastIndex && state.page < 6) {
                    viewModel.getMovies()
                }
            }

        }
    }
}

@Composable
fun PromotedFilm(viewModel: MainViewModel, state: MainScreenState, movieDescription: () -> Unit) {

    var image =
        "https://kartinkin.net/uploads/posts/2021-07/thumbs/1626193375_35-kartinkin-com-p-chernii-ekran-fon-krasivo-36.jpg"

    if (state.sizeMovieList != 0) {
        if (viewModel.movieList[0].poster != "") {
            image = viewModel.movieList[0].poster
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = stringResource(R.string.promoted_film),
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Transparent, Black),
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
        Button(
            onClick = {
                viewModel.getDetails(viewModel.movieList[0].id)
                { movieDescription() }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = DarkRed,
                contentColor = White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(0.dp, 0.dp, 0.dp, 32.dp)
                .height(44.dp)
                .width(160.dp),
        )
        {
            Text(
                stringResource(R.string.watch),
                color = White,
                fontFamily = IBM,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FavoritesList(viewModel: MainViewModel, state: MainScreenState) {
    if (state.sizeFavoriteList != 0) {

        Text(
            text = stringResource(R.string.favorite),
            color = DarkRed,
            fontFamily = IBM,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp, 0.dp)
        )

        val listState = rememberLazyListState()

        val fullyVisibleIndices: List<Int> by remember {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                if (visibleItemsInfo.isEmpty()) {
                    emptyList()
                } else {
                    val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()

                    val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
                    if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
                        fullyVisibleItemsInfo.removeFirst()
                    }

                    fullyVisibleItemsInfo.map { it.index }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            state = listState,
        ) {
            items(state.sizeFavoriteList) { index ->

                var width = 100.dp
                var height = 144.dp

                if (fullyVisibleIndices.isNotEmpty() && index == fullyVisibleIndices.first()) {
                    width = 120.dp
                    height = 172.dp
                }

                FavoritesCard(
                    viewModel = viewModel,
                    image = state.listOfFavoriteMovies!![index].poster,
                    id = state.listOfFavoriteMovies[index].id,
                    width,
                    height
                )
            }
        }
    }
}

@Composable
fun Gallery() {
    Text(
        text = stringResource(R.string.gallery),
        color = DarkRed,
        fontFamily = IBM,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp, 0.dp)
    )
}