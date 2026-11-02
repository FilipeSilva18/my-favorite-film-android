package com.faas.myfavoritefilms.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {

    @Test
    fun `maps language tags to supported languages`() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromTag(""))
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromTag(null))
        assertEquals(AppLanguage.PORTUGUESE_BRAZIL, AppLanguage.fromTag("pt-BR"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromTag("en-US,pt-BR"))
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromTag("fr"))
    }
}
