package com.example.emptycomposeactivity.network.user

import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository {
    private val api: UserApi = Network.getUserApi()

    fun getUserData(): Flow<UserData> = flow {
        val arrUserData = api.getUserData()
        Network.userData = arrUserData
        emit(arrUserData)
    }.flowOn(Dispatchers.IO)

    suspend fun putUserData(body: UserData) {
        api.putUserData(body)
    }
}