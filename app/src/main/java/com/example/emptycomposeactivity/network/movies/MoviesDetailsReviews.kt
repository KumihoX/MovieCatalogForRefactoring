package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MoviesDetailsReviews(
    val id: String,
    val rating: Int,
    val reviewText: String = "",
    val isAnonymous: Boolean,
    val createDateTime: String,
    val author: UserShortModel? = null
)
