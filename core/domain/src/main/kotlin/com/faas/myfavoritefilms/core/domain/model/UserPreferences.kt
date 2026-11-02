package com.faas.myfavoritefilms.core.domain.model

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = true,
    val language: AppLanguage = AppLanguage.SYSTEM,
)

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

enum class AppLanguage(val tag: String) {
    SYSTEM(""),
    PORTUGUESE_BRAZIL("pt-BR"),
    ENGLISH("en"),
    ;

    companion object {
        fun fromTag(tag: String?): AppLanguage {
            val primary = tag.orEmpty().split(",").first().trim()
            return when {
                primary.isEmpty() -> SYSTEM
                primary.startsWith("pt", ignoreCase = true) -> PORTUGUESE_BRAZIL
                primary.startsWith("en", ignoreCase = true) -> ENGLISH
                else -> SYSTEM
            }
        }
    }
}
