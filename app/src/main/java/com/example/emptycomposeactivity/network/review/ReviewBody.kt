package com.example.emptycomposeactivity.network.review

@kotlinx.serialization.Serializable
data class ReviewBody(
    val reviewText: String,
    val rating: Int,
    val isAnonymous: Boolean
)