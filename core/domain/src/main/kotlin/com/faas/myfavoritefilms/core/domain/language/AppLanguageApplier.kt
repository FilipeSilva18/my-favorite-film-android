package com.faas.myfavoritefilms.core.domain.language

import com.faas.myfavoritefilms.core.domain.model.AppLanguage

interface AppLanguageApplier {
    fun current(): AppLanguage
    fun apply(language: AppLanguage)
}
