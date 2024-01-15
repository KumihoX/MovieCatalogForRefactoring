package com.example.emptycomposeactivity.screens.movieScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.review.ReviewBody
import com.example.emptycomposeactivity.network.review.ReviewRepository
import com.example.emptycomposeactivity.screens.ext.convertToRequiredUIDateFormat
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    var movieDetails = Network.movieDetail
    val favoriteMovies = Network.favoriteMovies
    val profileData = Network.userData

    var userReviewPosition: Int? = null

    private val _uiState = mutableStateOf(MovieScreenState())
    var uiState: State<MovieScreenState> = _uiState

    init {
        checkFavoriteMovies()
        checkReviews()
    }

    fun checkReviews() {
        var cur = 0
        while (cur != movieDetails!!.reviews.size) {
            if (profileData!!.id == movieDetails!!.reviews[cur].author?.userId) {
                _uiState.value = _uiState.value.copy(userReview = true)
                userReviewPosition = cur
                _uiState.value = _uiState.value.copy(
                    reviewText = movieDetails!!.reviews[cur].reviewText,
                    checkedState = movieDetails!!.reviews[cur].isAnonymous,
                    rating = movieDetails!!.reviews[cur].rating
                )
                return
            }
            cur++
        }
    }

    private fun checkFavoriteMovies() {
        if (favoriteMovies != null) {
            var cur = 0
            repeat(favoriteMovies.movies.size) {
                if (movieDetails!!.id == favoriteMovies.movies[cur].id) {
                    _uiState.value = _uiState.value.copy(inFavorites = true)
                    return
                }
                cur++
            }
        }
    }

    fun correctDateTime(cur: Int) {
        val inputData = movieDetails!!.reviews[cur].createDateTime.convertToRequiredUIDateFormat()
        _uiState.value = _uiState.value.copy(createDateTime = inputData)
    }

    fun newRating(rating: Int) {
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun changeAnon(changes: Boolean) {
        _uiState.value = _uiState.value.copy(checkedState = !_uiState.value.checkedState)
    }

    fun interactionWithReviewDialog(status: Boolean) {
        _uiState.value = _uiState.value.copy(openReviewDialog = status)
    }

    fun onReviewChange(text: String) {
        _uiState.value = _uiState.value.copy(reviewText = text)
    }

    fun deleteFavorites(id: String) {
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        viewModelScope.launch {
            repositoryFavoriteMovies.deleteFavoriteMovies(id)
            _uiState.value = _uiState.value.copy(inFavorites = false)
            repositoryFavoriteMovies.getFavoriteMovies()
        }
    }

    fun addFavorites(id: String) {
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        viewModelScope.launch {
            repositoryFavoriteMovies.postFavoriteMovies(id)
            _uiState.value = _uiState.value.copy(inFavorites = true)
            repositoryFavoriteMovies.getFavoriteMovies()
        }
    }

    fun addReview() {
        val repositoryReview = ReviewRepository()
        val repositoryMovies = MoviesRepository()
        viewModelScope.launch {
            repositoryReview.addReview(
                movieDetails!!.id, ReviewBody(
                    reviewText = _uiState.value.reviewText,
                    rating = _uiState.value.rating,
                    isAnonymous = _uiState.value.checkedState
                )
            )
            repositoryMovies.getDetails(movieDetails!!.id).collect {}
            movieDetails = Network.movieDetail
            checkReviews()
        }
    }

    fun changeReview() {
        val repositoryReview = ReviewRepository()
        viewModelScope.launch {
            repositoryReview.changeReview(
                movieDetails!!.id,
                Network.movieDetail!!.reviews[userReviewPosition!!].id,
                ReviewBody(
                    reviewText = _uiState.value.reviewText,
                    rating = _uiState.value.rating,
                    isAnonymous = _uiState.value.checkedState
                )
            )
        }

    }

    fun deleteReview() {
        val repositoryReview = ReviewRepository()
        val repositoryMovies = MoviesRepository()
        viewModelScope.launch {
            repositoryReview.deleteReview(
                movieDetails!!.id,
                Network.movieDetail!!.reviews[userReviewPosition!!].id,
            )

            viewModelScope.launch {
                repositoryMovies.getDetails(movieDetails!!.id).collect {}
                userReviewPosition = null
                _uiState.value = _uiState.value.copy(
                    userReview = false,
                    reviewText = "",
                    checkedState = false,
                    rating = 0
                )
                movieDetails = Network.movieDetail
            }
        }
    }

    data class MovieScreenState(
        val inFavorites: Boolean = false,
        val userReview: Boolean = false,
        val reviewText: String = "",
        val createDateTime: String = "",
        val rating: Int = 0,
        val openReviewDialog: Boolean = false,
        val checkedState: Boolean = false
    )
}