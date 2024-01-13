package com.example.emptycomposeactivity.network

import com.example.emptycomposeactivity.network.auth.AuthApi
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMovies
import com.example.emptycomposeactivity.network.favoriteMovies.FavoriteMoviesApi
import com.example.emptycomposeactivity.network.movies.MovieDetails
import com.example.emptycomposeactivity.network.movies.Movies
import com.example.emptycomposeactivity.network.movies.MoviesApi
import com.example.emptycomposeactivity.network.review.ReviewApi
import com.example.emptycomposeactivity.network.user.UserApi
import com.example.emptycomposeactivity.network.user.UserData
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object Network {

    private const val BASE_URL = "https://react-midterm.kreosoft.space/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun getHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor(Interceptor())

            val logLevel = HttpLoggingInterceptor.Level.BODY
            addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
        }
        return client.build()
    }

    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .client(getHttpClient())
            .build()
    }


    private val retrofit = getRetrofit()

    var token: TokenResponse? = null
    var favoriteMovies: FavoriteMovies? = null
    var movies: Movies? = null
    var movieDetail: MovieDetails? = null
    var userData: UserData? = null

    fun getAuthApi(): AuthApi = retrofit.create(AuthApi::class.java)
    fun getFavoriteMoviesApi(): FavoriteMoviesApi = retrofit.create(FavoriteMoviesApi::class.java)
    fun getMoviesApi(): MoviesApi = retrofit.create(MoviesApi::class.java)
    fun getUserApi(): UserApi = retrofit.create(UserApi::class.java)
    fun getReviewApi(): ReviewApi = retrofit.create(ReviewApi::class.java)
}