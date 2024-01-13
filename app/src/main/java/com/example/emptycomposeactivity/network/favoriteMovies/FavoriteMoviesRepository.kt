package com.example.emptycomposeactivity.network.favoriteMovies

import com.example.emptycomposeactivity.network.Network
import com.example.emptycomposeactivity.network.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteMoviesRepository {
    private val api: FavoriteMoviesApi = Network.getFavoriteMoviesApi()

    fun getFavoriteMovies(): Flow<FavoriteMovies> = flow {
        val arrFavMovies = api.getFavorites()
        Network.favoriteMovies = arrFavMovies
        emit(arrFavMovies)
    }.flowOn(Dispatchers.IO)

    suspend fun postFavoriteMovies(id: String) {
        val post = api.postFavorites(addedId = id)
    }

    suspend fun deleteFavoriteMovies(id: String) {
        val tokenData = api.deleteFavorites(deletedId = id)
    }

}