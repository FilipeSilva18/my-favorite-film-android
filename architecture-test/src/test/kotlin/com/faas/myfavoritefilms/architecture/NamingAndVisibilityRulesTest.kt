package com.faas.myfavoritefilms.architecture

import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentNamed
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class NamingAndVisibilityRulesTest {
    private val classes get() = productionScope.flatMap { it.classes() }
    private val interfaces get() = productionScope.flatMap { it.interfaces() }

    @Test
    fun `use cases live in domain usecase and expose a single public operator invoke`() {
        classes.withNameEndingWith("UseCase").assertTrue {
            val hasInvoke = it.hasFunction { fn ->
                fn.name == "invoke" && fn.hasPublicOrDefaultModifier && fn.hasOperatorModifier
            }
            it.resideInPackage("..domain..usecase..") &&
                hasInvoke &&
                it.countFunctions { fn -> fn.hasPublicOrDefaultModifier } == 1
        }
    }

    @Test
    fun `use case implementations live in domain usecase`() {
        classes.withNameEndingWith("UseCaseImpl").assertTrue { it.resideInPackage("..domain..usecase..") }
    }

    @Test
    fun `repository contracts are interfaces in domain`() {
        interfaces.withNameEndingWith("Repository").assertTrue { it.resideInPackage("..domain..repository..") }
    }

    @Test
    fun `repository implementations are internal and live in data repository`() {
        classes.withNameEndingWith("RepositoryImpl").assertTrue {
            it.hasInternalModifier && it.resideInPackage("..data..repository..")
        }
    }

    @Test
    fun `data sources are internal and live in data datasource`() {
        classes.withNameEndingWith("DataSource")
            .filter { it.resideInPackage("..feature..") }
            .assertTrue { it.hasInternalModifier && it.resideInPackage("..data..datasource..") }
    }

    @Test
    fun `repositories talk to data sources, never to a DAO or a Retrofit API directly`() {
        classes.withNameEndingWith("RepositoryImpl").assertFalse { repository ->
            repository.primaryConstructor?.parameters?.any { param ->
                param.type.name.endsWith("Dao") || param.type.name.endsWith("Api")
            } == true
        }
    }

    @Test
    fun `DTOs are internal and live in remote dto`() {
        classes.withNameEndingWith("Dto").assertTrue {
            it.hasInternalModifier && it.resideInPackage("..data..remote..dto..")
        }
    }

    @Test
    fun `ViewModel subclasses end with ViewModel and live in presentation`() {
        classes.withParentNamed("ViewModel").assertTrue {
            it.name.endsWith("ViewModel") && it.resideInPackage("..presentation..")
        }
    }

    @Test
    fun `ViewModel depends on use cases, never on repositories (ADR-04)`() {
        classes.withParentNamed("ViewModel").assertFalse { viewModel ->
            viewModel.primaryConstructor?.parameters?.any { param ->
                param.type.name.endsWith("Repository") || param.type.name.endsWith("RepositoryImpl")
            } == true
        }
    }

    @Test
    fun `ViewModel never exposes public mutable flows`() {
        classes.withParentNamed("ViewModel").assertFalse { viewModel ->
            viewModel.properties().any { property ->
                property.hasPublicOrDefaultModifier &&
                    property.type?.name?.startsWith("Mutable") == true
            }
        }
    }

    @Test
    fun `Compose previews are private and end with Preview`() {
        productionScope
            .flatMap { it.functions() }
            .filter { fn -> fn.annotations.any { it.name == "Preview" } }
            .assertTrue { it.hasPrivateModifier && it.name.endsWith("Preview") }
    }
}
