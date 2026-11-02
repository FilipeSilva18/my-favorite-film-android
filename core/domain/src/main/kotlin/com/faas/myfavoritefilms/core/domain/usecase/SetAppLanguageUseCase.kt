package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.language.AppLanguageApplier
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
    private val applier: AppLanguageApplier,
) {
    suspend operator fun invoke(language: AppLanguage) {
        repository.setLanguage(language)
        applier.apply(language)
    }
}
