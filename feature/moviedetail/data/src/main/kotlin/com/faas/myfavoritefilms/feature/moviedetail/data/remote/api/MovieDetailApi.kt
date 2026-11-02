package com.faas.myfavoritefilms.feature.moviedetail.data.remote.api

import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MovieDetailApi {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = APPEND_CREDITS_AND_VIDEOS,
    ): MovieDetailDto

    companion object {
        const val APPEND_CREDITS_AND_VIDEOS = "credits,videos"
    }
}
