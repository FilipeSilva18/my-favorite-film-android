package com.faas.myfavoritefilms.architecture

import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test

class UiStringRulesTest {

    private val hardcodedUiText = Regex(
        """\bText\(\s*"|\b(text|contentDescription|placeholder)\s*=\s*"""",
    )

    @Test
    fun `composables never show hardcoded text - everything comes from string resources`() {
        productionScope
            .flatMap { it.functions() }
            .filter { fn -> fn.annotations.any { it.name == "Composable" } }
            .filterNot { fn -> fn.annotations.any { it.name == "Preview" } }
            .assertFalse { fn -> hardcodedUiText.containsMatchIn(fn.text) }
    }
}
