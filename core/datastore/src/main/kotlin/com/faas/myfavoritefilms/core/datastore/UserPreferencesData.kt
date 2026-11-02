package com.faas.myfavoritefilms.core.datastore

data class UserPreferencesData(
    val themeMode: String? = null,
    val useDynamicColor: Boolean? = null,
    val languageTag: String? = null,
)
