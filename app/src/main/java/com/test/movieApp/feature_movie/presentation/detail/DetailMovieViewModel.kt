package com.test.movieApp.feature_movie.presentation.detail

import androidx.lifecycle.viewModelScope
import com.test.movieApp.core.domain.repository.MovieRepository
import com.test.movieApp.core.base.BaseViewModel
import com.test.movieApp.core.utils.Constant
import com.test.movieApp.core.utils.Resource
import com.test.movieApp.core.data.model.VideoModel
import com.test.movieApp.feature_movie.util.DetailType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : BaseViewModel() {

    private var _state = MutableStateFlow(DetailUIState())
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
                        getDetailMovie()
                    }
                    else -> {
                        getDetailTV()
                    }
                }
            }
        }
    }

    private fun getDetailMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDetailMovies(
                _state.value.itemId ?: 1
            ) .onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                errorResult = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = true,
                                errorResult = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                errorResult = null,
                                result = it.data
                            )
                        }

                    }
                }
            }.launchIn(this)
            }
    }
    fun clearLink(){
        _state.update {
            it.copy(
                resultVideo = emptyList()
            )
        }
    }
     fun getMovieVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMovieVideo(
                _state.value.itemId ?: 1
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                loadingMovies = false,
                                errorVideo = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                loadingMovies = true,
                                errorVideo = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                loadingMovies = false,
                                errorVideo = null,
                                resultVideo = it.data?.results as List<VideoModel>
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }
     fun getTvVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getTVVideo(
                _state.value.itemId ?: 1
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                loadingMovies = false,
                                errorVideo = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                loadingMovies = true,
                                errorVideo = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                loadingMovies = false,
                                errorVideo = if ((it.data?.results as List<VideoModel>).isEmpty()) Constant.NO_VIDEO else null,
                                resultVideo = it.data.results as List<VideoModel>
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }
    private fun getDetailTV() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDetailTV(
                _state.value.itemId ?: 1
            ).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                errorResult = it.errorMessage
                            )
                        }

                    }
                    is Resource.Loading -> {
                        _state.update { data ->
                            data.copy(
                                initialLoading = true,
                                errorResult = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        delay(1000)
                        _state.update { data ->
                            data.copy(
                                initialLoading = false,
                                errorResult = null,
                                result =  it.data
                            )
                        }

                    }
                }
            }.launchIn(this)
        }
    }

}