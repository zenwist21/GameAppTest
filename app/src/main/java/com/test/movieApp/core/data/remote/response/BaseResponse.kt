package com.test.movieApp.core.data.remote.response

data class BaseResponse<T>(
    val currentPage: Int = 0,
    val results: T? = null,
    val total_pages: Int = 0
)