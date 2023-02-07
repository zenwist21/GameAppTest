package com.test.gamesapp.feature_movie.presentation.main.ui.tv

import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.TmDbModel

data class TVUiState(
    val genreLoading: Boolean = false,
    val listLoading: Boolean = false,
    val listGenre: List<GenreModel> = emptyList(),
    val listTvShow: List<TmDbModel> = emptyList(),
    val errorGenre:String? = null,
    val errorTvShow:String? = null,
    val currentPage: Int? = 1,
    val totalPages: Int? = 1,
    val selectedGenre: Int? = null,
    val loadingNextPage: Boolean = false
)