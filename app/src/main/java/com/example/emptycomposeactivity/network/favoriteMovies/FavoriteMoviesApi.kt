package com.example.emptycomposeactivity.network.favoriteMovies

import com.example.emptycomposeactivity.network.TokenResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteMoviesApi {

    @GET("api/favorites")
    suspend fun getFavorites(): FavoriteMovies

    @POST("api/favorites/{id}/add")
    suspend fun postFavorites(@Path("id") addedId: String)

    @DELETE("api/favorites/{id}/delete")
    suspend fun deleteFavorites(@Path("id") deletedId: String)
}