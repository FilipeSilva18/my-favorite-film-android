package com.faas.myfavoritefilms.feature.movies.domain.usecase

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.movies.domain.interpreter.MovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.domain.model.QueryInterpretation
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class InterpretMovieQueryUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val interpreter: MovieQueryInterpreter,
) {
    suspend operator fun invoke(description: String): QueryInterpretation {
        val normalized = description.trim()
        if (normalized.length < MIN_DESCRIPTION_LENGTH) return QueryInterpretation.NotUnderstood

        val genres = when (val result = repository.genres()) {
            is AppResult.Success -> result.data
            is AppResult.Failure -> return QueryInterpretation.Unavailable(result.error)
        }

        return when (val result = interpreter.interpret(normalized, genres)) {
            is AppResult.Success -> if (result.data.hasCriteria) {
                QueryInterpretation.Understood(result.data)
            } else {
                QueryInterpretation.NotUnderstood
            }
            is AppResult.Failure -> QueryInterpretation.Unavailable(result.error)
        }
    }

    companion object {
        const val MIN_DESCRIPTION_LENGTH = 3
    }
}
