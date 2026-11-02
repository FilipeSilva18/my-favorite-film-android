package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<UserPreferences> = repository.observeUserPreferences()
}
