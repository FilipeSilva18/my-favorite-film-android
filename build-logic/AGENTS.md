# build-logic/ — convention plugins

Complements the [root AGENTS.md](../AGENTS.md).

Modules don't repeat configuration: each `build.gradle.kts` applies `movies.*` plugins and declares only its own dependencies. Versions live in `gradle/libs.versions.toml`, shared with this included build.

| Plugin | Class | Used in |
|---|---|---|
| `movies.android.application` | `AndroidApplicationConventionPlugin` | `:app` (SDKs, Orchestrator, Kover, `unitTest` task) |
| `movies.android.library` | `AndroidLibraryConventionPlugin` | Every Android module |
| `movies.android.compose` | `AndroidComposeConventionPlugin` | Modules with Compose |
| `movies.jvm.library` | `JvmLibraryConventionPlugin` | JVM modules (`domain`, `api`, `common`, `analytics`, `ai`, `testing`) |
| `movies.dagger` | `DaggerConventionPlugin` | Modules with `@Inject`, `@Module` or `@Component` |
| `movies.android.room` | `AndroidRoomConventionPlugin` | `core:database` |
| `movies.android.feature.data` | `AndroidFeatureDataConventionPlugin` | `feature:*:data` |
| `movies.android.feature.presentation` | `AndroidFeaturePresentationConventionPlugin` | `feature:*:presentation` |
| `movies.android.robolectric` | `AndroidRobolectricConventionPlugin` | Modules with Robolectric, Compose UI Test and Roborazzi |

## Rules

- Configuration repeated in two or more modules becomes a convention plugin; don't copy blocks between `build.gradle.kts` files.
- New plugin: class in `convention/src/main/kotlin`, registration in `convention/build.gradle.kts`, alias under `[plugins]` in the catalog.
- AGP 9 uses built-in Kotlin: don't apply `kotlin-android`. `jvmTarget` is configured on the `KotlinJvmCompile` tasks (`KotlinConfig.kt`).
- Robolectric tests need the `--add-opens` and `--enable-native-access` flags from `AndroidRobolectricConventionPlugin` on JDK 24+. Don't remove them.
- Every base plugin registers the aggregating `unitTest` task; CI depends on it.
- A change here affects every module: run the full `./scripts/verify.sh`.
