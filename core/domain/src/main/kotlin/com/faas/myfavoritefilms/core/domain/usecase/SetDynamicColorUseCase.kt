package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetDynamicColorUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
) {
    suspend operator fun invoke(useDynamicColor: Boolean) = repository.setUseDynamicColor(useDynamicColor)
}
