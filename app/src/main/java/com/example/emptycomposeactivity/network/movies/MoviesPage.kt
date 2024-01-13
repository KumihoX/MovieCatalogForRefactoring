package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class MoviesPage(
    val pageSize: Int,
    val pageCount: Int,
    val currentPage: Int
)
