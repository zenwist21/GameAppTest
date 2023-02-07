package com.test.gamesapp.core.data.repository

import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.ReviewDataModel
import com.test.gamesapp.core.data.model.TmDbModel
import com.test.gamesapp.core.data.model.VideoModel
import com.test.gamesapp.core.data.remote.response.BaseResponse
import com.test.gamesapp.core.data.remote.source.MovieRemoteDataImpl
import com.test.gamesapp.core.domain.repository.MovieRepository
import com.test.gamesapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieRepositoryImpl @Inject constructor(
    private val remoteData: MovieRemoteDataImpl,
    private val ioDispatcher: CoroutineContext
) : MovieRepository {

    override fun getMovieList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getListMovie(params))
        }.flowOn(ioDispatcher)
    }

    override fun getMovieGenres(language: String): Flow<Resource<MutableList<GenreModel>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getMovieGenres(language))
        }.flowOn(ioDispatcher)
    }

    override fun getTvList(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<TmDbModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getListTvShow(params))
        }.flowOn(ioDispatcher)
    }

    override fun getTvGenres(language: String): Flow<Resource<MutableList<GenreModel>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getTVShowGenres(language))
        }.flowOn(ioDispatcher)
    }

    override fun getDetailMovies(movieId: Int): Flow<Resource<TmDbModel>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getDetailMovie(movieId))
        }.flowOn(ioDispatcher)
    }


    override fun getDetailTV(tv: Int): Flow<Resource<TmDbModel>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getDetailTVShow(tv))
        }.flowOn(ioDispatcher)
    }

    override fun getReviewMovie(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getReviewMovie(params))
        }.flowOn(ioDispatcher)
    }

    override fun getReviewTv(params: HashMap<String, Any>): Flow<Resource<BaseResponse<MutableList<ReviewDataModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getReviewTV(params))
        }.flowOn(ioDispatcher)
    }

    override fun getMovieVideo(movieId: Int): Flow<Resource<BaseResponse<MutableList<VideoModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getMovieVideos(movieId))
        }.flowOn(ioDispatcher)
    }

    override fun getTVVideo(tvId: Int): Flow<Resource<BaseResponse<MutableList<VideoModel>>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getTvVideos(tvId))
        }.flowOn(ioDispatcher)
    }



}