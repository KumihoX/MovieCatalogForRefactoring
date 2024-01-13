package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class Movies(
    val movies: List<MoviesDescription> = emptyList(),
    val pageInfo: MoviesPage
)
