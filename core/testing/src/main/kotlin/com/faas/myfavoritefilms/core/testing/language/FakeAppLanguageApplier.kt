package com.faas.myfavoritefilms.core.testing.language

import com.faas.myfavoritefilms.core.domain.language.AppLanguageApplier
import com.faas.myfavoritefilms.core.domain.model.AppLanguage

class FakeAppLanguageApplier(
    var systemLanguage: AppLanguage = AppLanguage.SYSTEM,
) : AppLanguageApplier {

    val applied = mutableListOf<AppLanguage>()

    override fun current(): AppLanguage = systemLanguage

    override fun apply(language: AppLanguage) {
        applied += language
        systemLanguage = language
    }
}
