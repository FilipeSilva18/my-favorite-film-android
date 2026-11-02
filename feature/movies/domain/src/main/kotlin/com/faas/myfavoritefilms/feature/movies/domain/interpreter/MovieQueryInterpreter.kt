package com.faas.myfavoritefilms.feature.movies.domain.interpreter

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters

interface MovieQueryInterpreter {
    suspend fun interpret(description: String, genres: List<Genre>): AppResult<MovieFilters>
}
