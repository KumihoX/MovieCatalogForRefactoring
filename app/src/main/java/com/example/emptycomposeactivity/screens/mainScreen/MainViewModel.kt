package com.example.emptycomposeactivity.screens.mainScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.favoriteMovies.ShortMovie
import com.example.emptycomposeactivity.network.movies.MoviesGenres
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesReviews
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var genresString: String = ""

    var rating: Float = 0f

    private var currentListOfMovies = Network.movies
    private var currentListOfFavorites = Network.favoriteMovies
    private val _hasErrors = mutableStateOf(false)

    private val _uiState = mutableStateOf(MainScreenState())
    var uiState: State<MainScreenState> = _uiState

    init {
        val repositoryMovies = MoviesRepository()
        val repositoryFavoriteMovies = FavoriteMoviesRepository()

        viewModelScope.launch {
            repositoryMovies.getMovies(_uiState.value.page).collect {
                currentListOfMovies = it
            }

            repositoryFavoriteMovies.getFavoriteMovies().collect {
                currentListOfFavorites = it
            }

            _uiState.value = _uiState.value.copy(
                sizeMovieList = currentListOfMovies!!.movies.size,
                sizeFavoriteList = currentListOfFavorites!!.movies.size,
                readyPage = true,
                listOfFavoriteMovies = currentListOfFavorites!!.movies,
                page = _uiState.value.page + 1
            )
        }
    }

    var movieList = mutableStateListOf(
        currentListOfMovies!!.movies[0],
        currentListOfMovies!!.movies[1],
        currentListOfMovies!!.movies[2],
        currentListOfMovies!!.movies[3],
        currentListOfMovies!!.movies[4],
        currentListOfMovies!!.movies[5]
    )

    fun update(id: String) {
        val repository = FavoriteMoviesRepository()
        viewModelScope.launch {
            repository.deleteFavoriteMovies(id)
            repository.getFavoriteMovies().catch { _hasErrors.value = true }
                .collect {
                    currentListOfFavorites = it
                    if (_hasErrors.value || currentListOfFavorites == null) return@collect
                    _uiState.value =
                        _uiState.value.copy(sizeFavoriteList = currentListOfFavorites!!.movies.size)
                    if (_uiState.value.sizeMovieList != 0) {
                        _uiState.value =
                            _uiState.value.copy(listOfFavoriteMovies = currentListOfFavorites!!.movies)
                    }
                }
        }
    }

    fun createCorrectGenresString(genres: List<MoviesGenres>) {
        val genresListSize = genres.size
        var curGenre = 0
        genresString = ""

        while (curGenre != genresListSize) {
            val txt = genres[curGenre].name
            genresString += if (curGenre == genresListSize - 1) {
                "$txt"
            } else {
                "$txt, "
            }
            curGenre++
        }
    }

    fun countRating(listReviews: List<MoviesReviews>) {
        rating = 0f
        val reviewsListSize = listReviews.size
        var curReview = 0

        while (curReview != reviewsListSize) {

            rating += listReviews[curReview].rating

            curReview++
        }
        rating /= (reviewsListSize)
    }

    fun getMovies() {
        val repositoryMovies = MoviesRepository()

        viewModelScope.launch {
            if (_uiState.value.page != 1) {
                repositoryMovies.getMovies(_uiState.value.page).catch { _hasErrors.value = true }
                    .collect {
                        movieList.addAll(it.movies)
                        _uiState.value = _uiState.value.copy(page = _uiState.value.page + 1)
                    }
            }

        }
    }

    fun getDetails(id: String, openMovieDescription: () -> Unit) {
        val repositoryMovies = MoviesRepository()

        viewModelScope.launch {
            repositoryMovies.getDetails(id).catch { _hasErrors.value = true }.collect {}
            if (!_hasErrors.value) {
                openMovieDescription()
            }
        }
    }

    data class MainScreenState(
        val sizeMovieList: Int = 0,
        val readyPage: Boolean = false,
        val sizeFavoriteList: Int = 0,
        val listOfFavoriteMovies: List<ShortMovie>? = null,
        val page: Int = 1
    )
}