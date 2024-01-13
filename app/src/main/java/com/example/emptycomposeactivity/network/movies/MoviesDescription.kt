package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MoviesDescription(
    val id: String,
    val name: String = "",
    val poster: String = "",
    val year: Int,
    val country: String = "",
    val genres: List<MoviesGenres> = emptyList(),
    val reviews: List<MoviesReviews> = emptyList()
)
