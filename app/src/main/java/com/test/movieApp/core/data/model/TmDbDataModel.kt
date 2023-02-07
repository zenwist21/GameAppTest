package com.test.movieApp.core.data.model

import android.os.Parcelable
import com.test.movieApp.core.data.remote.response.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class TmDbModel(
    val adult: Boolean = false,
    val backdrop_path: String? = null,
    val budget: Int? = 0,
    val genres: List<GenreModel>? = emptyList(),
    val homepage: String? = null,
    val id: Int? = 0,
    val imdb_id: String? = null,
    val original_language: String? = null,
    val first_air_date: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = 0.0,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompanyModel>? = emptyList(),
    val production_countries: List<ProductionCountryModel>? = emptyList(),
    val release_date: String? = null,
    val revenue: Long? = 0,
    val runtime: Int? = 0,
    val spoken_languages: List<SpokenLanguageModel>? = emptyList(),
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val name: String? = null,
    val original_name: String? = null,
    val video: Boolean = false,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0,
    val message: String? = "",
    val totalPage: Int = 1,
) : Parcelable {
    object Mapper {
        fun from(form: TmDBResponse): TmDbModel {
            return TmDbModel(
                form.adult,
                form.backdrop_path,
                form.budget,
                GenreModel.Mapper.fromResponse(form = form.genres ?: emptyList()),
                form.homepage,
                form.id,
                form.imdb_id,
                form.original_language,
                form.first_air_date,
                form.original_title,
                form.overview,
                form.popularity,
                form.poster_path,
                ProductionCompanyModel.Mapper.fromResponse(form.production_companies ?: emptyList()),
                ProductionCountryModel.Mapper.fromResponse(form.production_countries ?: emptyList()),
                form.release_date,
                form.revenue,
                form.runtime,
                SpokenLanguageModel.Mapper.fromResponse(form.spoken_languages  ?: emptyList()),
                form.status,
                form.tagline,
                form.title,
                form.name,
                form.original_name,
                form.video,
                form.vote_average,
                form.vote_count,
                form.message,
                form.totalPage,
            )
        }


        fun fromResponse(form: BaseResponse<MutableList<TmDBResponse>>): BaseResponse<MutableList<TmDbModel>> {
            val data = mutableListOf<TmDbModel>()
            if (!form.results.isNullOrEmpty()) {
                form.results.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return BaseResponse(
                form.currentPage,
                results = data,
                form.total_pages
            )
        }
    }
}

@Parcelize
data class ProductionCompanyModel(
    val id: Int? = 0,
    val logo_path: String? = null,
    val name: String? = null,
    val origin_country: String? = null
) : Parcelable {
    object Mapper {
        private fun from(form: ProductionCompanyResponse) =
            ProductionCompanyModel(
                form.id,
                form.logo_path,
                form.name,
                form.origin_country,
            )

        fun fromResponse(form: List<ProductionCompanyResponse>): MutableList<ProductionCompanyModel> {
            val data = mutableListOf<ProductionCompanyModel>()
            if (form.isNotEmpty()) {
                form.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return data
        }
    }
}

@Parcelize
data class ProductionCountryModel(
    val iso_3166_1: String? = null,
    val name: String? = null
) : Parcelable {
    object Mapper {
        private fun from(form: ProductionCountryResponse) =
            ProductionCountryModel(
                form.iso_3166_1,
                form.name,
            )

        fun fromResponse(form: List<ProductionCountryResponse>): MutableList<ProductionCountryModel> {
            val data = mutableListOf<ProductionCountryModel>()
            if (form.isNotEmpty()) {
                form.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return data
        }
    }

}

@Parcelize
data class GenreModel(
    val id: Int? = 0,
    val name: String? = null,
    val message: String? = null,
    var isSelected: Boolean = false,
) : Parcelable {
    object Mapper {
        private fun from(form: GenreResponse) =
            GenreModel(
                form.id,
                form.name,
                form.message,
                form.isSelected,
            )

        fun fromResponse(form: List<GenreResponse>): MutableList<GenreModel> {
            val data = mutableListOf<GenreModel>()
            if (form.isNotEmpty()) {
                form.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return data
        }
    }
}

@Parcelize
data class VideoModel(
    val id: String? = null,
    val iso_3166_1: String? = null,
    val iso_639_1: String? = null,
    val key: String? = null,
    val name: String? = null,
    val official: Boolean = false,
    val published_at: String? = null,
    val site: String? = null,
    val size: Int = 0,
    val type: String? = null
) : Parcelable {
    object Mapper {
        private fun from(form: VideoResponse) =
            VideoModel(
                form.id,
                form.iso_3166_1,
                form.iso_639_1,
                form.key,
                form.name,
                form.official,
                form.published_at,
                form.site,
                form.size,
                form.type
            )

        fun fromBaseResponse(form: BaseResponse<MutableList<VideoResponse>>): BaseResponse<MutableList<VideoModel>> {
            val data = mutableListOf<VideoModel>()
            if (!form.results.isNullOrEmpty()) {
                form.results.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return BaseResponse(
                form.currentPage,
                results = data,
                form.total_pages
            )
        }
    }
}

@Parcelize
data class ReviewDataModel(
    val author: String? = null,
    val author_details: AuthorDetailModel? = null,
    val content: String? = null,
    val created_at: String? = null,
    val id: String? = null,
    val updated_at: String? = null,
    val url: String? = null
) : Parcelable {
    object Mapper {
        private fun from(form: ReviewResponse) =
            ReviewDataModel(
                form.author,
                AuthorDetailModel.Mapper.from(form.author_details ?: AuthorDetailsResponse()),
                form.content,
                form.created_at,
                form.id,
                form.updated_at,
                form.url,
            )

        fun fromBaseResponse(form: BaseResponse<MutableList<ReviewResponse>>): BaseResponse<MutableList<ReviewDataModel>> {
            val data = mutableListOf<ReviewDataModel>()
            if (!form.results.isNullOrEmpty()) {
                form.results.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return BaseResponse(
                form.currentPage,
                results = data,
                form.total_pages
            )
        }
    }
}

@Parcelize
data class SpokenLanguageModel(
    val english_name: String? = null,
    val iso_639_1: String? = null,
    val name: String? = null
) : Parcelable {
    object Mapper {
        private fun from(form: SpokenLanguageResponse) =
            SpokenLanguageModel(
                form.english_name,
                form.iso_639_1,
                form.name,
            )

        fun fromResponse(form: List<SpokenLanguageResponse>): MutableList<SpokenLanguageModel> {
            val data = mutableListOf<SpokenLanguageModel>()
            if (form.isNotEmpty()) {
                form.forEach { dForm ->
                    data.add(from(dForm))
                }
            }
            return data
        }
    }
}

@Parcelize
data class AuthorDetailModel(
    val avatar_path: String? = null,
    val name: String? = null,
    val rating: Double? = 0.0,
    val username: String? = null
) : Parcelable {
    object Mapper {
        fun from(form: AuthorDetailsResponse) =
            AuthorDetailModel(
                form.avatar_path,
                form.name,
                form.rating,
                form.username,
            )
    }
}