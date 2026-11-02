package com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailDto(
    val id: Int,
    val title: String,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    val tagline: String? = null,
    val overview: String = "",
    val status: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    val runtime: Int? = null,
    val budget: Long = 0,
    val revenue: Long = 0,
    val popularity: Double = 0.0,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val homepage: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    val genres: List<NamedDto> = emptyList(),
    @SerialName("production_companies") val productionCompanies: List<NamedDto> = emptyList(),
    @SerialName("production_countries") val productionCountries: List<NamedDto> = emptyList(),
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto> = emptyList(),
    val credits: CreditsDto = CreditsDto(),
    val videos: VideosDto = VideosDto(),
)

@Serializable
internal data class CreditsDto(val cast: List<CastDto> = emptyList())

@Serializable
internal data class CastDto(
    val id: Int,
    val name: String,
    val character: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int = Int.MAX_VALUE,
)

@Serializable
internal data class VideosDto(val results: List<VideoDto> = emptyList())

@Serializable
internal data class VideoDto(
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean = false,
)

@Serializable
internal data class NamedDto(val name: String)

@Serializable
internal data class SpokenLanguageDto(
    val name: String = "",
    @SerialName("english_name") val englishName: String = "",
)
