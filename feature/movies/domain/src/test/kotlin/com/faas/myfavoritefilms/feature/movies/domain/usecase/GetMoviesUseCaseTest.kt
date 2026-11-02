package com.faas.myfavoritefilms.feature.movies.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class GetMoviesUseCaseTest {

    private val repository = FakeMoviesRepository()
    private val useCase = GetMoviesUseCase(repository)

    @Test
    fun `blank query returns popular movies`() {
        useCase("   ")
        assertEquals(FakeMoviesRepository.Call.Popular, repository.lastCall)
    }

    @Test
    fun `query shorter than 2 characters returns popular movies`() {
        useCase(" a ")
        assertEquals(FakeMoviesRepository.Call.Popular, repository.lastCall)
    }

    @Test
    fun `valid query is normalized before searching`() {
        useCase("  matrix ")
        assertEquals(FakeMoviesRepository.Call.Search("matrix"), repository.lastCall)
    }
}
