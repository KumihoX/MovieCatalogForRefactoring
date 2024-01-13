package com.example.emptycomposeactivity.network.auth

import com.example.emptycomposeactivity.network.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

//Лучше в отдельных интерфейсах держать что-то связанное друг с другом, здесь, например, аутентификация
interface AuthApi {

    //Сначала указываем какой тип запроса, потом путь до него(без начального слэша!)


    @POST("api/account/register")
    suspend fun register(@Body body: RegisterRequestBody): TokenResponse

    //suspend спец функция, которая говорит, что задача будет сделана не моментально, для длительных операций

    @POST("api/account/login")
    suspend fun login(@Body body: LoginRequestBody): TokenResponse

    @POST("api/account/logout")
    suspend fun logout(): TokenResponse

}