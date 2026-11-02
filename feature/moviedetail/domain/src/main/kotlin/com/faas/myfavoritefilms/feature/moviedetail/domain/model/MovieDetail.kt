package com.faas.myfavoritefilms.feature.moviedetail.domain.model

import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import java.time.LocalDate

data class MovieDetail(
    val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?,
    val rating: Double,
    val voteCount: Int,
    val genres: List<String>,
    val infoSections: List<SduiComponent>,
)
