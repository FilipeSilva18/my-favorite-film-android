package com.faas.myfavoritefilms.feature.moviedetail.presentation

import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.feature.moviedetail.presentation.model.MovieDetailUi

internal object MovieDetailTestData {
    val movie = MovieDetailUi(
        id = 603,
        title = "Matrix",
        tagline = "Bem-vindo ao mundo real.",
        overview = "Um hacker descobre que a realidade é uma simulação.",
        posterUrl = null,
        backdropUrl = null,
        releaseDateLabel = "30 de mar. de 1999",
        year = "1999",
        runtimeMinutes = 136,
        ratingLabel = "8.2",
        voteCountLabel = "25.431",
        genres = listOf("Ação", "Ficção científica"),
        infoSections = listOf(
            SduiComponent.KeyValueList(
                title = "Ficha técnica",
                items = listOf(
                    KeyValueItem("Título original", "The Matrix", "info"),
                    KeyValueItem("Orçamento", "$63,000,000", "money"),
                ),
            ),
            SduiComponent.ChipGroup("Produtoras", listOf("Warner Bros.")),
            SduiComponent.LinkButton("Ver no IMDb", "https://www.imdb.com/title/tt0133093"),
        ),
    )
}
