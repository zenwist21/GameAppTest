package com.test.movieApp.feature_movie.presentation.main.ui.movie

import androidx.lifecycle.viewModelScope
import com.test.movieApp.feature_movie.presentation.component.params.MovieParams
import com.test.movieApp.core.domain.repository.MovieRepository
import com.test.movieApp.core.base.BaseViewModel
import com.test.movieApp.core.data.model.GenreModel
import com.test.movieApp.core.data.model.TmDbModel
import com.test.movieApp.core.utils.Constant.DUMMY
import com.test.movieApp.core.utils.Resource
import com.test.movieApp.core.utils.getDummyMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : BaseViewModel() {

    private var _state = MutableStateFlow(MovieUIState())
    val state get() = _state.asStateFlow()

    init {
        execute()
    }


    fun setGenre(genre: Int?) {
        _state.update {
            it.copy(selectedGenre = genre, currentPage = 1)
        }
    }

    fun loadMoreActivities() {
        if ((state.value.currentPage ?: 1) <= (state.value.totalPages ?: 1)) {
            val nextPage = (state.value.currentPage ?: 1) + 1
            getMoviesList(nextPage)
        }
    }

    private fun getMovieGenres() = viewModelScope.launch(Dispatchers.IO) {
        repo.getMovieGenres().onEach {
            when (it) {
                is Resource.DataError -> {
                    _state.update { data ->
                        data.copy(
                            genreLoading = false,
                            errorGenre = it.errorMessage
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { data ->
                        data.copy(
                            genreLoading = true,
                            listLoading = true,
                            errorGenre = null
                        )
                    }
                }
                is Resource.Success -> {
                    delay(1000)
                    _state.update { data ->
                        data.copy(
                            genreLoading = false,
                            errorGenre = null,
                            listGenre = it.data as List<GenreModel>
                        )
                    }
                }
            }
        }.launchIn(this)
    }

    fun getMoviesList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMovieList(
                MovieParams.getMovieParams(
                    page = page,
                    genres = if (_state.value.selectedGenre != null) _state.value.selectedGenre else null
                )
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                listLoading = false,
                                errorMovie = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                listLoading = page == 1,
                                loadingNextPage = page != 1,
                                listMovie = if (page != 1) {
                                    state.value.listMovie + listOf(TmDbModel(title = DUMMY))
                                } else {
                                  getDummyMovie()
                                },
                                errorMovie = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                listLoading = false,
                                errorMovie = null,
                                loadingNextPage = false,
                                currentPage = page,
                                listMovie = if (page != 1) {
                                    val temp = data.listMovie as MutableList<TmDbModel>
                                    temp.remove(temp.find { dTemp -> dTemp == TmDbModel(title = DUMMY) })
                                    temp.addAll(it.data?.results ?: mutableListOf())
                                    temp
                                }
                                else (it.data?.results as List<TmDbModel>),
                                totalPages = it.data?.total_pages
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }

     fun execute() = viewModelScope.launch(Dispatchers.IO) {
        val list = async { getMovieGenres() }
        val movie = async { getMoviesList() }

        list.await()
        movie.await()
    }
}