package com.example.emptycomposeactivity.network.review

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewApi {

    @POST("api/movie/{movieId}/review/add")
    suspend fun addReview(@Path("movieId") userId: String, @Body body: ReviewBody)

    @PUT("/api/movie/{movieId}/review/{id}/edit")
    suspend fun putReview(
        @Path("movieId") movieId: String,
        @Path("id") puttedId: String,
        @Body body: ReviewBody
    )

    @DELETE("/api/movie/{movieId}/review/{id}/delete")
    suspend fun deleteReview(@Path("movieId") movieId: String, @Path("id") deletedId: String)
}