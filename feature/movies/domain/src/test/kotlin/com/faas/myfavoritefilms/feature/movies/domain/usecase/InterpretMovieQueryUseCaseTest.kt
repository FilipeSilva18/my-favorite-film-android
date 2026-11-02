package com.faas.myfavoritefilms.feature.movies.domain.usecase

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.feature.movies.domain.interpreter.MovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import com.faas.myfavoritefilms.feature.movies.domain.model.QueryInterpretation
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class InterpretMovieQueryUseCaseTest {
    private val action = Genre(28, "Action")
    private val repository = FakeMoviesRepository().apply { genresResult = AppResult.Success(listOf(action)) }
    private val interpreter = object : MovieQueryInterpreter {
        val requests = mutableListOf<Pair<String, List<Genre>>>()
        var result: AppResult<MovieFilters> = AppResult.Success(MovieFilters())

        override suspend fun interpret(description: String, genres: List<Genre>): AppResult<MovieFilters> {
            requests += description to genres
            return result
        }
    }
    private val useCase = InterpretMovieQueryUseCase(repository, interpreter)

    @Test
    fun `understood description returns the filters`() = runTest {
        val filters = MovieFilters(genres = listOf(action), yearFrom = 1990, yearTo = 1999)
        interpreter.result = AppResult.Success(filters)

        assertEquals(QueryInterpretation.Understood(filters), useCase("  action movies from the 90s "))
        assertEquals(listOf("action movies from the 90s" to listOf(action)), interpreter.requests)
    }

    @Test
    fun `sort alone counts as a criterion`() = runTest {
        val filters = MovieFilters(sort = MovieSort.RATING)
        interpreter.result = AppResult.Success(filters)

        assertEquals(QueryInterpretation.Understood(filters), useCase("best rated"))
    }

    @Test
    fun `filters without criteria are not understood`() = runTest {
        interpreter.result = AppResult.Success(MovieFilters())

        assertEquals(QueryInterpretation.NotUnderstood, useCase("something nice"))
    }

    @Test
    fun `too short description skips the interpreter and the genres request`() = runTest {
        assertEquals(QueryInterpretation.NotUnderstood, useCase(" ab "))
        assertEquals(0, repository.genresRequests)
        assertEquals(emptyList<Pair<String, List<Genre>>>(), interpreter.requests)
    }

    @Test
    fun `genres failure makes the interpretation unavailable`() = runTest {
        repository.genresResult = AppResult.Failure(DomainError.Network)

        assertEquals(QueryInterpretation.Unavailable(DomainError.Network), useCase("action movies"))
        assertEquals(emptyList<Pair<String, List<Genre>>>(), interpreter.requests)
    }

    @Test
    fun `interpreter failure makes the interpretation unavailable`() = runTest {
        interpreter.result = AppResult.Failure(DomainError.Unknown)

        assertEquals(QueryInterpretation.Unavailable(DomainError.Unknown), useCase("action movies"))
    }
}
