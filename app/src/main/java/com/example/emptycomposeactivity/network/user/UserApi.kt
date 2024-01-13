package com.example.emptycomposeactivity.network.user

import retrofit2.http.*

interface UserApi {

    @GET("api/account/profile")
    suspend fun getUserData(): UserData

    @PUT("api/account/profile")
    suspend fun putUserData(@Body body: UserData)

}