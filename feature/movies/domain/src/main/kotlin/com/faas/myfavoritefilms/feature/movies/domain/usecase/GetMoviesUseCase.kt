package com.faas.myfavoritefilms.feature.movies.domain.usecase

import androidx.paging.PagingData
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<Movie>> {
        val normalized = query.trim()
        return if (normalized.length < MIN_QUERY_LENGTH) {
            repository.popularMovies()
        } else {
            repository.searchMovies(normalized)
        }
    }

    companion object {
        const val MIN_QUERY_LENGTH = 2
    }
}
