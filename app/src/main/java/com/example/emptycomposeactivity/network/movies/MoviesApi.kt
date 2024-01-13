package com.example.emptycomposeactivity.network.movies

import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    @GET("api/movies/{page}")
    suspend fun getMovies(@Path("page") numberPage: Int): Movies

    @GET("api/movies/details/{id}")
    suspend fun getDetails(@Path("id") movieId: String): MovieDetails

}