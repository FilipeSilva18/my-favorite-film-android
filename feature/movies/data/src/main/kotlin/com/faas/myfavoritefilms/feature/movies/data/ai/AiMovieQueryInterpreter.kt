package com.faas.myfavoritefilms.feature.movies.data.ai

import com.faas.myfavoritefilms.core.ai.AiException
import com.faas.myfavoritefilms.core.ai.AiJsonGenerator
import com.faas.myfavoritefilms.core.analytics.ErrorReporter
import com.faas.myfavoritefilms.core.analytics.reportIfUnexpected
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.network.TmdbJson
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.AiMovieFiltersDto
import com.faas.myfavoritefilms.feature.movies.domain.interpreter.MovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

internal class AiMovieQueryInterpreter(
    private val generator: AiJsonGenerator,
    private val errorReporter: ErrorReporter,
    private val today: () -> LocalDate,
) : MovieQueryInterpreter {

    @Inject
    constructor(generator: AiJsonGenerator, errorReporter: ErrorReporter) : this(generator, errorReporter, LocalDate::now)

    override suspend fun interpret(description: String, genres: List<Genre>): AppResult<MovieFilters> {
        val currentYear = today().year
        return try {
            val json = generator.generate(
                instructions = MovieQueryPrompt.instructions(genres, currentYear),
                input = description,
                schema = MovieQueryPrompt.schema,
            )
            val dto = TmdbJson.decodeFromString(AiMovieFiltersDto.serializer(), json)
            AppResult.Success(dto.toFilters(genres, currentYear))
        } catch (e: CancellationException) {
            throw e
        } catch (e: AiException.Unavailable) {
            AppResult.Failure(DomainError.Unknown)
        } catch (e: AiException.QuotaExceeded) {
            AppResult.Failure(DomainError.RateLimited)
        } catch (e: IOException) {
            AppResult.Failure(DomainError.Network)
        } catch (e: Exception) {
            errorReporter.reportIfUnexpected(e, DomainError.Unknown, origin = ORIGIN)
            AppResult.Failure(DomainError.Unknown)
        }
    }

    private companion object {
        const val ORIGIN = "movies_ai_interpreter"
    }
}

internal fun AiMovieFiltersDto.toFilters(genres: List<Genre>, currentYear: Int): MovieFilters {
    val byId = genres.associateBy { it.id }
    val validYears = FIRST_MOVIE_YEAR..currentYear + MAX_YEARS_AHEAD
    val first = yearFrom?.takeIf { it in validYears }
    val last = yearTo?.takeIf { it in validYears }
    val swapped = first != null && last != null && first > last
    val start = if (swapped) last else first
    val end = if (swapped) first else last
    return MovieFilters(
        genres = genreIds.distinct().mapNotNull { byId[it] }.take(MAX_GENRES),
        yearFrom = start,
        yearTo = end,
        minRating = minRating?.takeIf { it > 0.0 }?.coerceAtMost(MAX_RATING),
        sort = MovieSort.entries.firstOrNull { it.name == sort } ?: MovieSort.POPULARITY,
    )
}

internal const val MAX_GENRES = 3
private const val FIRST_MOVIE_YEAR = 1874
private const val MAX_YEARS_AHEAD = 2
private const val MAX_RATING = 10.0
