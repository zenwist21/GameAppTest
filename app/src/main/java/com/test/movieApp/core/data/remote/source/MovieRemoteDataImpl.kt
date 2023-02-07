package com.test.movieApp.core.data.remote.source

import com.test.movieApp.core.data.model.GenreModel
import com.test.movieApp.core.data.model.ReviewDataModel
import com.test.movieApp.core.data.model.TmDbModel
import com.test.movieApp.core.data.model.VideoModel
import com.test.movieApp.core.data.remote.network.ApiService
import com.test.movieApp.core.data.remote.response.BaseResponse
import com.test.movieApp.core.data.remote.response.GenreResponse
import com.test.movieApp.core.domain.source.MovieRemoteDataSource
import com.test.movieApp.core.utils.Constant.NO_DATA_EXIST
import com.test.movieApp.core.utils.Constant.NO_VIDEO
import com.test.movieApp.core.utils.Constant.networkError
import com.test.movieApp.core.utils.Constant.noInternet
import com.test.movieApp.core.utils.NetworkConnectivity
import com.test.movieApp.core.utils.Resource
import com.test.movieApp.core.utils.convertErrorMessage
import java.io.IOException
import javax.inject.Inject

class MovieRemoteDataImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkConnectivity: NetworkConnectivity
) : MovieRemoteDataSource {

    override suspend fun getListMovie(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<TmDbModel>>> {
        val responseCall = apiService::getMovieList
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(params)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = TmDbModel.Mapper.fromResponse(response.body()!!))

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getMovieGenres(language: String): Resource<MutableList<GenreModel>> {
        val responseCall = apiService::getMovieGenres
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(language)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.genres.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = GenreModel.Mapper.fromResponse(response.body()?.genres as MutableList<GenreResponse>))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getListTvShow(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<TmDbModel>>> {
        val responseCall = apiService::getTvShowList
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(params)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = TmDbModel.Mapper.fromResponse(response.body()!!))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getTVShowGenres(language: String): Resource<MutableList<GenreModel>> {
        val responseCall = apiService::getTvGenres
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(language)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.genres.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = GenreModel.Mapper.fromResponse(response.body()?.genres as MutableList<GenreResponse>))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getDetailTVShow(tvID: Int): Resource<TmDbModel> {
        val responseCall = apiService::getDetailTv
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(tvID)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            if (response.body() == null) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = TmDbModel.Mapper.from(response.body()!!))

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getDetailMovie(movieID: Int): Resource<TmDbModel> {
        val responseCall = apiService::getDetailMovie
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(movieID)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            if (response.body() == null) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = TmDbModel.Mapper.from(response.body()!!))

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getReviewMovie(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<ReviewDataModel>>> {
        val responseCall = apiService::getReviewMovie
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response =
                responseCall.invoke(params["movie_id"].toString(), params["page"].toString())
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )

            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = ReviewDataModel.Mapper.fromBaseResponse(response.body()!!))

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getReviewTV(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<ReviewDataModel>>> {
        val responseCall = apiService::getReviewTv
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(
                params["movie_id"].toString(),
                params["page"].toString(),
                "en-US"
            )
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )

            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_DATA_EXIST
                )
            }
            Resource.Success(data = ReviewDataModel.Mapper.fromBaseResponse(response.body()!!))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getMovieVideos(movieID: Int): Resource<BaseResponse<MutableList<VideoModel>>> {
        val responseCall = apiService::getMovieVideo
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(movieID)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_VIDEO
                )
            }
            Resource.Success(data = VideoModel.Mapper.fromBaseResponse(response.body()!!))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }

    override suspend fun getTvVideos(tvID: Int): Resource<BaseResponse<MutableList<VideoModel>>> {
        val responseCall = apiService::getTvVideo
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = noInternet)
        }
        return try {
            val response = responseCall.invoke(tvID)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = convertErrorMessage(response.errorBody())
                )
            }
            if (response.body()?.results.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NO_VIDEO
                )
            }
            Resource.Success(data = VideoModel.Mapper.fromBaseResponse(response.body()!!))
        } catch (e: IOException) {
            return Resource.DataError(errorMessage = networkError)
        }
    }


}