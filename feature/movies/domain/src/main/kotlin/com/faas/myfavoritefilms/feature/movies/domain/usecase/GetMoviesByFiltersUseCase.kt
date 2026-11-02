package com.faas.myfavoritefilms.feature.movies.domain.usecase

import androidx.paging.PagingData
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByFiltersUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {
    operator fun invoke(filters: MovieFilters): Flow<PagingData<Movie>> = repository.discoverMovies(filters)
}
