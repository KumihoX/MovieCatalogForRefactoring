package com.example.emptycomposeactivity.network.auth

import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository {
    private val api: AuthApi = Network.getAuthApi()

    fun register(body: RegisterRequestBody): Flow<TokenResponse> = flow {
        val tokenData = api.register(body)
        Network.token = tokenData
        emit(tokenData)
    }.flowOn(Dispatchers.IO)

    fun login(body: LoginRequestBody): Flow<TokenResponse> = flow {
        val tokenData = api.login(body)
        Network.token = tokenData
        emit(tokenData)
    }.flowOn(Dispatchers.IO)

    fun logout(): Flow<TokenResponse> = flow {
        val tokenData = api.logout()
        Network.token = tokenData
        emit(tokenData)
    }.flowOn(Dispatchers.IO)

}