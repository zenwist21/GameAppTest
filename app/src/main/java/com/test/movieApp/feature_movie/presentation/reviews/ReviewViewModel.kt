package com.test.movieApp.feature_movie.presentation.reviews

import androidx.lifecycle.viewModelScope
import com.test.movieApp.feature_movie.presentation.component.params.MovieParams
import com.test.movieApp.core.base.BaseViewModel
import com.test.movieApp.core.domain.repository.MovieRepository
import com.test.movieApp.core.data.model.ReviewDataModel
import com.test.movieApp.feature_movie.util.DetailType
import com.test.movieApp.core.utils.Constant.DUMMY
import com.test.movieApp.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repo: MovieRepository
) : BaseViewModel() {

    private var _state = MutableStateFlow(ReviewUiState())
    val state get() = _state.asStateFlow()

    fun setMovieId(id: Int?, type: DetailType = DetailType.MOVIE) {
        _state.update {
            it.copy(itemId = id, type = type)
        }
    }

    fun getData(){
        state.value.let {
            if (it.itemId != 0){
                when(it.type){
                    DetailType.MOVIE -> {
                        getReviewMovie()
                    }
                    else -> {
                        getReviewTv()
                    }
                }
            }
        }
    }

    fun loadMoreActivities() {
        if (state.value.currentPage <= state.value.totalPages) {
            val nextPage = state.value.currentPage + 1
            _state.update {
                it.copy(currentPage = it.currentPage + 1)
            }
            when(state.value.type){
                DetailType.MOVIE -> {
                    getReviewMovie(nextPage)
                }
                else-> {
                    getReviewTv(nextPage)
                }
            }
        }
    }


    private fun getReviewMovie(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getReviewMovie(
                MovieParams.getReview(
                    page = page,
                    movieId = state.value.itemId
                )
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                loadingNextPage = false,
                                error = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = page == 1,
                                loadingNextPage = page != 1,
                                listReview = if (page != 1) {
                                    state.value.listReview + listOf(ReviewDataModel(author = DUMMY))
                                } else {
                                  emptyList()
                                },
                                error = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                currentPage = page,
                                totalPages = it.data?.total_pages ?:1,
                                initialLoading = false,
                                error = null,
                                loadingNextPage = false,
                                listReview = if (page != 1) {
                                    val temp = data.listReview as MutableList<ReviewDataModel>
                                    temp.remove(temp.find { dTemp ->dTemp == ReviewDataModel(author = DUMMY) })
                                    temp.addAll(it.data?.results ?: mutableListOf())
                                    temp
                                } else {
                                    (it.data?.results as List<ReviewDataModel>)
                                },
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }

    private fun getReviewTv(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getReviewTv(
                MovieParams.getReview(
                    page = page,
                    movieId = state.value.itemId
                )
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                loadingNextPage = false,
                                error = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = page == 1,
                                loadingNextPage = page != 1,
                                listReview = if (page != 1) {
                                    state.value.listReview + listOf(ReviewDataModel(author = DUMMY))
                                } else {
                                    emptyList()
                                },
                                error = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                currentPage = page,
                                totalPages = it.data?.total_pages ?:1,
                                initialLoading = false,
                                error = null,
                                loadingNextPage = false,
                                listReview = if (page != 1) {
                                    val temp = data.listReview as MutableList<ReviewDataModel>
                                    temp.remove(temp.find { dTemp ->dTemp == ReviewDataModel(author = DUMMY) })
                                    temp.addAll(it.data?.results ?: mutableListOf())
                                    temp
                                } else {
                                    (it.data?.results as List<ReviewDataModel>)
                                }
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }
}