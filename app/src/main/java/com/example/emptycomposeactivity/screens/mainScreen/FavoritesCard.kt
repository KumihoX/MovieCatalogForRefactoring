package com.example.emptycomposeactivity.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.emptycomposeactivity.R
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.screens.movieScreen.MovieViewModel
import com.example.emptycomposeactivity.ui.theme.Black
import com.example.emptycomposeactivity.ui.theme.TextGray
import com.example.emptycomposeactivity.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesCard(
    viewModel: MainViewModel,
    image: String,
    id: String,

    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp, 0.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            Modifier
                .wrapContentSize()
                .clickable {},
            backgroundColor = Black
        ) {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = stringResource(R.string.favorites_poster),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .width(width)
                    .height(height)
            )
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false)
            {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    onClick = { viewModel.update(id) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.icon_delete),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}
