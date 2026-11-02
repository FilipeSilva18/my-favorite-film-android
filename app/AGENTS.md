# app/ — composition root

Complements the [root AGENTS.md](../AGENTS.md).

`:app` only builds the graph and the navigation. It has no business rules, never calls a repository and holds no feature screens.

## Where things live

| File | Role |
|---|---|
| `di/AppComponent.kt` | `@Singleton @Component` with the core modules and one `<Feature>DataModule` per feature |
| `di/ViewModelComponent.kt` | `@Subcomponent` per ViewModel (receives `SavedStateHandle`) with one `<Feature>PresentationModule` per feature |
| `navigation/MoviesNavHost.kt` | Registers the features' `NavGraphBuilder.<screen>Screen()` extensions |
| `navigation/TopLevelDestination.kt` | Bottom bar tabs |
| `navigation/MoviesApp.kt` | `Scaffold` with `NavigationBar` and insets |
| `MoviesApplication.kt` | Creates the `AppComponent` and installs App Check |
| `src/debug`, `src/release` | Build type variations (Logcat tracker, `DebugOverlay`), without `if (BuildConfig.DEBUG)` |

## When adding a feature

1. `implementation(projects.feature.<name>.presentation)` and `implementation(projects.feature.<name>.data)` in `app/build.gradle.kts`.
2. `<Name>DataModule::class` in `AppComponent.modules`.
3. `<Name>PresentationModule::class` in `ViewModelComponent.modules`.
4. `<screen>Screen(...)` in `MoviesNavHost`; for a tab, an entry in `TopLevelDestination`.
5. The new ViewModel in `DependencyGraphTest` (`src/test/.../di/`).

## Don't

- Import `internal` classes from other modules or call repositories from here.
- Commit `google-services.json`. The `google-services` and `crashlytics` plugins are only applied when the file exists.
- Use Hilt or automatic aggregation annotations (ADR-0009).

## Tests

- `DependencyGraphTest` (Robolectric): every ViewModel can be created by the real graph.
- `MainActivityViewModelTest`.
- `src/androidTest`: end-to-end navigation with Orchestrator (`./gradlew connectedDebugAndroidTest`).
