plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.module.graph.assert)
}

dependencies {
    subprojects.filter { it.buildFile.exists() }.forEach { kover(project(it.path)) }
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*.BuildConfig",
                    "*.R", "*.R$*",
                    "*_Factory", "*_Factory$*", "*_Provide*Factory*", "*_MembersInjector",
                    "*.Dagger*", "*_Impl", "*_Impl$*",
                    "*ComposableSingletons*",
                    "*.buildlogic.*",
                )
                annotatedBy(
                    "androidx.compose.ui.tooling.preview.Preview",
                    "dagger.Module",
                )
            }
        }
    }
}

moduleGraphAssert {
    maxHeight = 6
    restricted = arrayOf(
        ":feature:movies:.* -X> :feature:(moviedetail|settings):.*",
        ":feature:moviedetail:.* -X> :feature:(movies|settings):.*",
        ":feature:favorites:.* -X> :feature:(movies|moviedetail|settings):.*",
        ":feature:settings:.* -X> :feature:(movies|moviedetail|favorites):.*",
        ":feature:(movies|moviedetail|settings):.* -X> :feature:favorites:(domain|data|presentation)",
        ":feature:favorites:api -X> :feature:favorites:(domain|data|presentation)",
        ":feature:.*:api -X> :core:(data|network|database|datastore|ui|designsystem|di|analytics|ai)",

        ":core:.* -X> :feature:.*",

        ":feature:.*:presentation -X> .*:data",
        ":feature:.*:data -X> .*:presentation",

        ".*:domain -X> :core:(data|network|database|datastore|ui|designsystem|di)",
        ":core:(network|database|datastore) -X> :core:(domain|data)",
        ":feature:.* -X> :core:ai-firebase",
        ":feature:.*:presentation -X> :core:datastore",
        ":core:common -X> :core:.*",
    )
}
