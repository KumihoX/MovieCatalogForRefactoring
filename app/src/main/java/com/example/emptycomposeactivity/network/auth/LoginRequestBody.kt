package com.example.emptycomposeactivity.network.auth

@kotlinx.serialization.Serializable
data class LoginRequestBody(
    val username: String,
    val password: String
)
