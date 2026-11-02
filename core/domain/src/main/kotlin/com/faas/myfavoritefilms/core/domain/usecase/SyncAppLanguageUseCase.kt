package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.language.AppLanguageApplier
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SyncAppLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
    private val applier: AppLanguageApplier,
) {
    suspend operator fun invoke() = repository.setLanguage(applier.current())
}
