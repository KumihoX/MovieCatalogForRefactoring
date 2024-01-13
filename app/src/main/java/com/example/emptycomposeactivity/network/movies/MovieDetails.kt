package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MovieDetails(
    val id: String,
    val name: String = "",
    val poster: String = "",
    val year: Int,
    val country: String = "",
    val genres: List<MoviesGenres> = emptyList(),
    val reviews: List<MoviesDetailsReviews> = emptyList(),
    val time: Int,
    val tagline: String = "",
    val director: String = "",
    val description: String = "",
    val budget: Int = 0,
    val fees: Int = 0,
    val ageLimit: Int
)
