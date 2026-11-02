pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MyFavoriteFilms"

include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:ui")
include(":core:di")
include(":core:testing")
include(":core:analytics")
include(":core:analytics-firebase")
include(":core:ai")
include(":core:ai-firebase")
include(":feature:movies:domain")
include(":feature:movies:data")
include(":feature:movies:presentation")
include(":feature:moviedetail:domain")
include(":feature:moviedetail:data")
include(":feature:moviedetail:presentation")
include(":feature:favorites:api")
include(":feature:favorites:domain")
include(":feature:favorites:data")
include(":feature:favorites:presentation")
