package com.example.emptycomposeactivity.screens.mainScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.favoriteMovies.ShortMovie
import com.example.emptycomposeactivity.network.movies.MoviesDescription
import com.example.emptycomposeactivity.network.movies.MoviesGenres
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesReviews
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var genresString: String = ""

    var rating: Float = 0f

    var currentListOfMovies = Network.movies
    var currentListOfFavorites = Network.favoriteMovies

    private val _sizeMovieList = mutableStateOf(0)
    var sizeMovieList: State<Int> = _sizeMovieList

    private val _readyPage = mutableStateOf(false)
    var readyPage: State<Boolean> = _readyPage

    private val _hasErrors = mutableStateOf(false)

    private val _sizeFavoriteList = mutableStateOf(0)
    var sizeFavoriteList: State<Int> = _sizeFavoriteList

    var listOfFavoriteMovies: List<ShortMovie>? = null


    var page = 1

    init {
        val repositoryMovies = MoviesRepository()
        val repositoryFavoriteMovies = FavoriteMoviesRepository()

        viewModelScope.launch {
            repositoryMovies.getMovies(page).collect {
                currentListOfMovies = it
            }
            page++

            repositoryFavoriteMovies.getFavoriteMovies().collect {
                currentListOfFavorites = it
            }

            _sizeMovieList.value = currentListOfMovies!!.movies.size
            _sizeFavoriteList.value = currentListOfFavorites!!.movies.size
            listOfFavoriteMovies = currentListOfFavorites!!.movies
            _readyPage.value = true
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
                    if (!_hasErrors.value) {
                        if (currentListOfFavorites != null) {
                            _sizeFavoriteList.value = currentListOfFavorites!!.movies.size
                            if (_sizeMovieList.value != 0) {
                                listOfFavoriteMovies = currentListOfFavorites!!.movies
                            }
                        }
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
            if (page != 1) {
                repositoryMovies.getMovies(page).catch { _hasErrors.value = true }.collect {
                    movieList.addAll(it.movies)
                    page += 1
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
}