package com.faas.myfavoritefilms.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.faas.myfavoritefilms.core.domain.language.AppLanguageApplier
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import javax.inject.Inject

class AppCompatLanguageApplier @Inject constructor() : AppLanguageApplier {

    override fun current(): AppLanguage =
        AppLanguage.fromTag(AppCompatDelegate.getApplicationLocales().toLanguageTags())

    override fun apply(language: AppLanguage) {
        val locales = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            else -> LocaleListCompat.forLanguageTags(language.tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
