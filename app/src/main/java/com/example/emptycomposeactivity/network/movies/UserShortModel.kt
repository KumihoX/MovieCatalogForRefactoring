package com.example.emptycomposeactivity.network.movies

@kotlinx.serialization.Serializable
data class UserShortModel(
    val userId: String,
    val nickName: String = "",
    val avatar: String = ""
)
