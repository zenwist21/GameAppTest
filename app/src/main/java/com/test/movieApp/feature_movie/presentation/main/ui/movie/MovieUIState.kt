package com.test.movieApp.feature_movie.presentation.main.ui.movie

import com.test.movieApp.core.data.model.GenreModel
import com.test.movieApp.core.data.model.TmDbModel


data class MovieUIState(
    val genreLoading: Boolean = false,
    val listLoading: Boolean = false,
    val listGenre: List<GenreModel> = emptyList(),
    val listMovie: List<TmDbModel> = emptyList(),
    val errorGenre:String? = null,
    val errorMovie:String? = null,
    val currentPage: Int? = 1,
    val totalPages: Int? = 1,
    val selectedGenre: Int? = null,
    val loadingNextPage: Boolean = false
)