package com.test.gamesapp.core.data.repository

import com.test.gamesapp.core.data.remote.response.BaseResponse
import com.test.gamesapp.feature_movie.data.model.GenreModel
import com.test.gamesapp.feature_movie.data.model.ReviewDataModel
import com.test.gamesapp.feature_movie.data.model.TmDbModel
import com.test.gamesapp.feature_movie.data.model.VideoModel
import com.test.gamesapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>>
    suspend fun getMovieGenres(language: String = "en-US"): Flow<Resource<MutableList<GenreModel>>>


    suspend fun getTvList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>>
    suspend fun getTvGenres(language: String = "en-US"): Flow<Resource<MutableList<GenreModel>>>

    suspend fun getDetailMovies(movieId:Int): Flow<Resource<TmDbModel>>
    suspend fun getDetailTV(tv:Int): Flow<Resource<TmDbModel>>

    suspend fun getReviewMovie(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>>
    suspend fun getReviewTv(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>>

    suspend fun getMovieVideo(movieId: Int) : Flow<Resource<BaseResponse<MutableList<VideoModel>>>>
    suspend fun getTVVideo(tvId:Int):Flow<Resource<BaseResponse<MutableList<VideoModel>>>>



}