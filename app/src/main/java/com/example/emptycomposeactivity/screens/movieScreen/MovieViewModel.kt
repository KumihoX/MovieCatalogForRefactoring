package com.example.emptycomposeactivity.screens.movieScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesRepository
import com.example.emptycomposeactivity.network.movies.MoviesRepository
import com.example.emptycomposeactivity.network.review.ReviewBody
import com.example.emptycomposeactivity.network.review.ReviewRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    var movieDetails = Network.movieDetail
    val favoriteMovies = Network.favoriteMovies
    val profileData = Network.userData

    private val _inFavorites = mutableStateOf(false)
    var inFavorites: State<Boolean> = _inFavorites

    private val _userReview = mutableStateOf(false)
    var userReview: State<Boolean> = _userReview

    var userReviewPosition: Int? = null

    private val _reviewText = mutableStateOf("")
    var reviewText: State<String> = _reviewText

    private val _createDateTime = MutableLiveData("")
    var createDateTime: LiveData<String> = _createDateTime

    private val _rating = mutableStateOf(0)
    var rating: State<Int> = _rating

    private val _openReviewDialog = mutableStateOf(false)
    var openReviewDialog: State<Boolean> = _openReviewDialog

    private val _checkedState = mutableStateOf(false)
    var checkedState: State<Boolean> = _checkedState

    init {
        checkFavoriteMovies()
        checkReviews()
    }

    fun checkReviews() {
        var cur = 0
        while (cur != movieDetails!!.reviews.size) {
            if (profileData!!.id == movieDetails!!.reviews[cur].author?.userId) {
                _userReview.value = true
                userReviewPosition = cur
                _reviewText.value = movieDetails!!.reviews[cur].reviewText
                _checkedState.value = movieDetails!!.reviews[cur].isAnonymous
                _rating.value = movieDetails!!.reviews[cur].rating
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
                    _inFavorites.value = true
                    return
                }
                cur++
            }
        }
    }

    fun correctDateTime(cur: Int) {
        val inputData = movieDetails!!.reviews[cur].createDateTime.substringBefore("T").split('-')
        _createDateTime.value = inputData[2] + "." + inputData[1] + "." + inputData[0]
    }

    fun newRating(rating: Int) {
        _rating.value = rating
    }

    fun changeAnon(changes: Boolean) {
        _checkedState.value = !_checkedState.value
    }

    fun interactionWithReviewDialog(status: Boolean) {
        _openReviewDialog.value = status
    }

    fun onReviewChange(text: String) {
        _reviewText.value = text
    }

    fun deleteFavorites(id: String) {
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        viewModelScope.launch {
            repositoryFavoriteMovies.deleteFavoriteMovies(id)
            _inFavorites.value = false
            repositoryFavoriteMovies.getFavoriteMovies()
        }
    }

    fun addFavorites(id: String) {
        val repositoryFavoriteMovies = FavoriteMoviesRepository()
        viewModelScope.launch {
            repositoryFavoriteMovies.postFavoriteMovies(id)
            _inFavorites.value = true
            repositoryFavoriteMovies.getFavoriteMovies()
        }
    }

    fun addReview() {
        val repositoryReview = ReviewRepository()
        val repositoryMovies = MoviesRepository()
        viewModelScope.launch {
            repositoryReview.addReview(
                movieDetails!!.id, ReviewBody(
                    reviewText = _reviewText.value,
                    rating = _rating.value,
                    isAnonymous = _checkedState.value
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
                    reviewText = _reviewText.value,
                    rating = _rating.value,
                    isAnonymous = _checkedState.value
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
                _userReview.value = false
                _reviewText.value = ""
                _checkedState.value = false
                _rating.value = 0
                movieDetails = Network.movieDetail
            }
        }
    }
}