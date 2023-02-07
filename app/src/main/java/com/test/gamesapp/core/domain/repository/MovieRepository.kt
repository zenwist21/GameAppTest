package com.test.gamesapp.core.domain.repository

import com.test.gamesapp.core.data.remote.response.BaseResponse
import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.ReviewDataModel
import com.test.gamesapp.core.data.model.TmDbModel
import com.test.gamesapp.core.data.model.VideoModel
import com.test.gamesapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
     fun getMovieList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>>
     fun getMovieGenres(language: String = "en-US"): Flow<Resource<MutableList<GenreModel>>>


     fun getTvList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>>
     fun getTvGenres(language: String = "en-US"): Flow<Resource<MutableList<GenreModel>>>

     fun getDetailMovies(movieId:Int): Flow<Resource<TmDbModel>>
     fun getDetailTV(tv:Int): Flow<Resource<TmDbModel>>

     fun getReviewMovie(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>>
     fun getReviewTv(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>>

     fun getMovieVideo(movieId: Int) : Flow<Resource<BaseResponse<MutableList<VideoModel>>>>
     fun getTVVideo(tvId:Int):Flow<Resource<BaseResponse<MutableList<VideoModel>>>>



}