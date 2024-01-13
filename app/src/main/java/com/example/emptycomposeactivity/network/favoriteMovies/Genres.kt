package com.example.emptycomposeactivity.network.favoriteMovies

@kotlinx.serialization.Serializable
data class Genres(
    val id: String,
    val name: String = ""
)
