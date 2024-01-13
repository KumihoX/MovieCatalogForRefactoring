package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MoviesGenres(
    val id: String,
    val name: String = ""
)