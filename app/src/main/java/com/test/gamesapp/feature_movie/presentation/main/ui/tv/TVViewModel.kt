package com.test.gamesapp.feature_movie.presentation.main.ui.tv

import androidx.lifecycle.viewModelScope
import com.test.gamesapp.feature_movie.presentation.component.params.MovieParams
import com.test.gamesapp.core.domain.repository.MovieRepository
import com.test.gamesapp.core.base.BaseViewModel
import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.TmDbModel
import com.test.gamesapp.core.utils.Constant.DUMMY
import com.test.gamesapp.core.utils.Resource
import com.test.gamesapp.core.utils.getDummyMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TVViewModel @Inject constructor(
    private val repo: MovieRepository
) : BaseViewModel() {

    private var _state = MutableStateFlow(TVUiState())
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
            getTvList(nextPage)
        }
    }

    private fun getTvGenres() = viewModelScope.launch(Dispatchers.IO) {
        repo.getTvGenres().onEach {
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

    fun getTvList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getTvList(
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
                                errorTvShow = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                listLoading = page == 1,
                                loadingNextPage = page != 1,
                                listTvShow = if (page != 1) {
                                    state.value.listTvShow + listOf(TmDbModel(title = DUMMY))
                                } else {
                                    getDummyMovie()
                                },
                                errorTvShow = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                listLoading = false,
                                errorTvShow = null,
                                loadingNextPage = false,
                                currentPage = page,
                                listTvShow = if (page != 1) {
                                    val temp = data.listTvShow as MutableList<TmDbModel>
                                    temp.remove(temp.find { dTemp -> dTemp == TmDbModel(title = DUMMY) })
                                    temp.addAll(it.data?.results ?: mutableListOf())
                                    temp
                                }
                                else it.data?.results as List<TmDbModel>,
                                totalPages = it.data?.total_pages
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }

      fun execute() = viewModelScope.launch(Dispatchers.IO) {
        val list = async { getTvGenres() }
        val movie = async { getTvList() }

        list.await()
        movie.await()
    }
}