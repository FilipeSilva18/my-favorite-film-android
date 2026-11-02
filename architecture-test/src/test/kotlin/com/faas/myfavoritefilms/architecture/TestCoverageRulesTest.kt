package com.faas.myfavoritefilms.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentNamed
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class TestCoverageRulesTest {
    private val classes get() = productionScope.flatMap { it.classes() }

    private val testClassNames by lazy {
        Konsist.scopeFromTest().classes().map { it.name }.toSet()
    }

    @Test
    fun `every use case has a test`() {
        classes.withNameEndingWith("UseCase", "UseCaseImpl").assertTrue { "${it.name}Test" in testClassNames }
    }

    @Test
    fun `every ViewModel has a test`() {
        classes.withParentNamed("ViewModel").assertTrue { "${it.name}Test" in testClassNames }
    }
}
