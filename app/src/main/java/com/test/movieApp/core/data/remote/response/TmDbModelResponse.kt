package com.test.movieApp.core.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TmDBResponse(
    val adult: Boolean = false,
    val backdrop_path: String? = null,
    val budget: Int? = 0,
    val genres: List<GenreResponse>? = emptyList(),
    val homepage: String? = null,
    val id: Int? = 0,
    val imdb_id: String? = null,
    val original_language: String? = null,
    val first_air_date: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = 0.0,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompanyResponse>? = emptyList(),
    val production_countries: List<ProductionCountryResponse>? = emptyList(),
    val release_date: String? = null,
    val revenue: Long? = 0,
    val runtime: Int? = 0,
    val spoken_languages: List<SpokenLanguageResponse>? = emptyList(),
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
) : Parcelable

@Parcelize
data class ProductionCompanyResponse(
    val id: Int? = 0,
    val logo_path: String? = null,
    val name: String? = null,
    val origin_country: String? = null
) : Parcelable

@Parcelize
data class ProductionCountryResponse(
    val iso_3166_1: String? = null,
    val name: String? = null
) : Parcelable

@Parcelize
data class GenreResponse(
    val id: Int? = 0,
    val name: String? = null,
    val message: String? = null,
    var isSelected: Boolean = false,
) : Parcelable

@Parcelize
data class VideoResponse(
    val id: String? = null,
    val iso_3166_1: String ? = null,
    val iso_639_1: String ? = null,
    val key: String ? = null,
    val name: String ? = null,
    val official: Boolean = false,
    val published_at: String ? = null,
    val site: String ? = null,
    val size: Int = 0,
    val type: String ? = null
) : Parcelable

@Parcelize
data class ReviewResponse(
    val author: String? = null,
    val author_details: AuthorDetailsResponse? = null,
    val content: String? = null,
    val created_at: String? = null,
    val id: String? = null,
    val updated_at: String? = null,
    val url: String? = null
) : Parcelable

@Parcelize
data class SpokenLanguageResponse(
    val english_name: String? = null,
    val iso_639_1: String? = null,
    val name: String? = null
) : Parcelable

@Parcelize
data class AuthorDetailsResponse(
    val avatar_path: String? = null,
    val name: String? = null,
    val rating: Double? = 0.0,
    val username: String? = null
) : Parcelable