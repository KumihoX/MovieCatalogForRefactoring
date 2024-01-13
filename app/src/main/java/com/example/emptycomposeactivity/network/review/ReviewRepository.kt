package com.example.emptycomposeactivity.network.review

import com.example.emptycomposeactivity.network.Network

class ReviewRepository {
    private val api: ReviewApi = Network.getReviewApi()

    suspend fun addReview(id: String, body: ReviewBody) {
        api.addReview(id, body)
    }

    suspend fun changeReview(movieId: String, id: String, body: ReviewBody) {
        api.putReview(movieId, id, body)
    }

    suspend fun deleteReview(movieId: String, id: String) {
        api.deleteReview(movieId, id)
    }
}