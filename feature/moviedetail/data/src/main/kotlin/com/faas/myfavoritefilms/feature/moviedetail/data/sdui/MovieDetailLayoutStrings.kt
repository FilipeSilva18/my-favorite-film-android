package com.faas.myfavoritefilms.feature.moviedetail.data.sdui

import android.content.res.Resources
import com.faas.myfavoritefilms.feature.moviedetail.data.R

internal data class MovieDetailLayoutStrings(
    val technicalSheet: String,
    val originalTitle: String,
    val originalLanguage: String,
    val status: String,
    val releaseDate: String,
    val budget: String,
    val revenue: String,
    val voteCount: String,
    val popularity: String,
    val genres: String,
    val productionCompanies: String,
    val productionCountries: String,
    val spokenLanguages: String,
    val homepage: String,
    val imdb: String,
    val cast: String,
    val videos: String,
    val statuses: Map<String, String>,
) {
    companion object {
        fun from(resources: Resources) = MovieDetailLayoutStrings(
            technicalSheet = resources.getString(R.string.sdui_technical_sheet),
            originalTitle = resources.getString(R.string.sdui_original_title),
            originalLanguage = resources.getString(R.string.sdui_original_language),
            status = resources.getString(R.string.sdui_status),
            releaseDate = resources.getString(R.string.sdui_release_date),
            budget = resources.getString(R.string.sdui_budget),
            revenue = resources.getString(R.string.sdui_revenue),
            voteCount = resources.getString(R.string.sdui_vote_count),
            popularity = resources.getString(R.string.sdui_popularity),
            genres = resources.getString(R.string.sdui_genres),
            productionCompanies = resources.getString(R.string.sdui_production_companies),
            productionCountries = resources.getString(R.string.sdui_production_countries),
            spokenLanguages = resources.getString(R.string.sdui_spoken_languages),
            homepage = resources.getString(R.string.sdui_homepage),
            imdb = resources.getString(R.string.sdui_imdb),
            cast = resources.getString(R.string.sdui_cast),
            videos = resources.getString(R.string.sdui_videos),
            statuses = mapOf(
                "Released" to resources.getString(R.string.sdui_status_released),
                "Post Production" to resources.getString(R.string.sdui_status_post_production),
                "In Production" to resources.getString(R.string.sdui_status_in_production),
                "Planned" to resources.getString(R.string.sdui_status_planned),
                "Rumored" to resources.getString(R.string.sdui_status_rumored),
                "Canceled" to resources.getString(R.string.sdui_status_canceled),
            ),
        )
    }
}
