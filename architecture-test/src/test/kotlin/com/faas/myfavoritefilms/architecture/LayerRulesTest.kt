package com.faas.myfavoritefilms.architecture

import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test

class  LayerRulesTest {
    private val featureApiPackage = Regex("""${Regex.escape(BASE_PACKAGE)}\.feature\.[a-z]+\.api""")
    private val infraImports = listOf("android.", "androidx.compose.", "androidx.room.", "retrofit2.", "okhttp3.", "dagger.")

    @Test
    fun `domain, common and the AI contract are pure Kotlin - no Android, Compose, Room, Retrofit, OkHttp or Dagger`() {
        productionScope
            .filter {
                it.packagee?.name?.let { pkg ->
                    ".domain" in pkg || ".core.common" in pkg || (".core.ai" in pkg && ".core.ai.firebase" !in pkg)
                } == true
            }
            .assertFalse { file ->
                file.hasImport { import -> infraImports.any { import.name.startsWith(it) } }
            }
    }

    @Test
    fun `presentation never imports the data layer`() {
        productionScope
            .filter { it.packagee?.name?.contains(".presentation") == true }
            .assertFalse { file ->
                file.hasImport { import ->
                    import.name.contains(".data.") ||
                        import.name.startsWith("retrofit2.") ||
                        import.name.startsWith("androidx.room.")
                }
            }
    }

    @Test
    fun `a feature only reaches another feature through its public api module (ADR-05)`() {
        FEATURES.forEach { feature ->
            val others = FEATURES - feature
            productionScope
                .filter { it.packagee?.name?.startsWith("$BASE_PACKAGE.feature.$feature.") == true }
                .assertFalse { file ->
                    file.hasImport { import ->
                        others.any { other ->
                            import.name.startsWith("$BASE_PACKAGE.feature.$other.") &&
                                !import.name.startsWith("$BASE_PACKAGE.feature.$other.api.")
                        }
                    }
                }
        }
    }

    @Test
    fun `public api modules only hold contracts - no Android, Compose, Room, Retrofit or Dagger`() {
        productionScope
            .filter { file -> file.packagee?.name?.matches(featureApiPackage) == true }
            .assertFalse { file ->
                file.hasImport { import -> infraImports.any { import.name.startsWith(it) } }
            }
    }

    @Test
    fun `core never imports features`() {
        productionScope
            .filter { it.packagee?.name?.startsWith("$BASE_PACKAGE.core.") == true }
            .assertFalse { file -> file.hasImport { it.name.startsWith("$BASE_PACKAGE.feature.") } }
    }

    @Test
    fun `android util Log only in the debug source set`() {
        productionScope
            .filterNot { it.path.contains("/src/debug/") }
            .assertFalse { file -> file.hasImport { it.name == "android.util.Log" } }
    }

    @Test
    fun `only analytics-firebase and ai-firebase know the Firebase SDK`() {
        val firebaseModules = listOf("$BASE_PACKAGE.core.analytics.firebase", "$BASE_PACKAGE.core.ai.firebase")
        productionScope
            .filterNot { file -> firebaseModules.any { file.packagee?.name?.startsWith(it) == true } }
            .assertFalse { file -> file.hasImport { it.name.startsWith("com.google.firebase.") } }
    }

    @Test
    fun `features talk to AI only through the core ai contract`() {
        productionScope
            .filter { it.packagee?.name?.startsWith("$BASE_PACKAGE.feature.") == true }
            .assertFalse { file -> file.hasImport { it.name.startsWith("$BASE_PACKAGE.core.ai.firebase") } }
    }
}
