package com.example.emptycomposeactivity.network.favoriteMovies

@kotlinx.serialization.Serializable
data class ShortMovie(
    val id: String,
    val name: String = "",
    val poster: String = "",
    val year: Int,
    val country: String = "",
    val genres: List<Genres> = emptyList(),
    val reviews: List<Reviews> = emptyList()
)
