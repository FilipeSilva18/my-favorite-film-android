package com.faas.myfavoritefilms.feature.movies.data.remote.api

import com.faas.myfavoritefilms.feature.movies.data.remote.dto.GenresResponseDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.PagedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface MoviesApi {
    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int): PagedResponseDto<MovieDto>

    @GET("search/movie")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
    ): PagedResponseDto<MovieDto>

    @GET("discover/movie")
    suspend fun discover(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
        @Query("with_genres") withGenres: String? = null,
        @Query("primary_release_date.gte") releaseDateFrom: String? = null,
        @Query("primary_release_date.lte") releaseDateTo: String? = null,
        @Query("vote_average.gte") minRating: Double? = null,
        @Query("vote_count.gte") minVoteCount: Int? = null,
        @Query("include_adult") includeAdult: Boolean = false,
    ): PagedResponseDto<MovieDto>

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponseDto
}
