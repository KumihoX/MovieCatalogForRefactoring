package com.example.emptycomposeactivity.network.movies

import com.example.emptycomposeactivity.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MoviesRepository {
    private val api: MoviesApi = Network.getMoviesApi()

    fun getMovies(numberPage: Int): Flow<Movies> = flow { //тут должна быть страница
        val arrMovies = api.getMovies(numberPage)
        Network.movies = arrMovies
        emit(arrMovies)
    }.flowOn(Dispatchers.IO)

    fun getDetails(movieId: String): Flow<MovieDetails> = flow {
        val arrDetails = api.getDetails(movieId)
        Network.movieDetail = arrDetails
        emit(arrDetails)
    }.flowOn(Dispatchers.IO)

}