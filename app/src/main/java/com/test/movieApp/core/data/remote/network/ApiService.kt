package com.test.movieApp.core.data.remote.network

import com.test.movieApp.core.data.remote.response.BaseResponse
import com.test.movieApp.core.data.remote.response.ReviewResponse
import com.test.movieApp.core.data.remote.response.TmDBResponse
import com.test.movieApp.core.data.remote.response.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("language") language:String): Response<TmDBResponse>

    @GET("discover/movie")
    suspend fun getMovieList(@QueryMap param:HashMap<String, Any>):Response<BaseResponse<MutableList<TmDBResponse>>>

    @GET("genre/tv/list")
    suspend fun getTvGenres(@Query("language") language:String ="en-US"): Response<TmDBResponse>

    @GET("discover/tv")
    suspend fun getTvShowList(@QueryMap param:HashMap<String, Any>):Response<BaseResponse<MutableList<TmDBResponse>>>

    @GET("tv/{tv_id}")
    suspend fun getDetailTv(@Path("tv_id") tvID: Int): Response<TmDBResponse>

    @GET("movie/{id}")
    suspend fun getDetailMovie(@Path("id") movieID: Int): Response<TmDBResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewMovie(@Path("movie_id") movieID: String, @Query("page") page:String):Response<BaseResponse<MutableList<ReviewResponse>>>

    @GET("tv/{tv_id}/reviews")
    suspend fun getReviewTv(@Path("tv_id") tvID: String, @Query("page") page:String,@Query("language") language:String ="en-US"):Response<BaseResponse<MutableList<ReviewResponse>>>

    @GET("tv/{tv_id}/videos")
    suspend fun getTvVideo(@Path("tv_id") tvID: Int): Response<BaseResponse<MutableList<VideoResponse>>>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(@Path("movie_id") tvID: Int): Response<BaseResponse<MutableList<VideoResponse>>>
}