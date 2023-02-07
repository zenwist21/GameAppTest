package com.test.movieApp.feature_movie.presentation.reviews

import com.test.movieApp.core.data.model.ReviewDataModel
import com.test.movieApp.feature_movie.util.DetailType

data class ReviewUiState(
    val initialLoading: Boolean = false,
    val loadingNextPage: Boolean = false,
    val error: String? = null,
    val listReview: List<ReviewDataModel> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val itemId: Int? = null,
    val type: DetailType? = DetailType.MOVIE
)