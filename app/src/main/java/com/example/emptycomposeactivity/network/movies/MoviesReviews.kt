package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MoviesReviews(
    val id: String,
    val rating: Int
)
