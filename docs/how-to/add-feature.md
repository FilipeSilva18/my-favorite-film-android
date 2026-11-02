# Create a feature

Example: a `watchlist` feature with a list and an add action. Compact checklist: [feature/README.md](../../feature/README.md). Skill: [`feature-builder`](../../.agents/skills/feature-builder/SKILL.md).

## 0. Choose the shape

| Question | Answer → shape |
|---|---|
| Does it have its own business rules or data source? | Yes → `domain` + `data` + `presentation` |
| Does it only display and change rules that already live in `core:domain`? | Only `presentation` (like `settings`) |
| Will another feature need something from it? | Add an `api` module (like `favorites`, ADR-0017) |
| Will two features need the same cross-cutting rule (not a feature)? | Put the rule in `core` |

## 1. Modules

```
feature/watchlist/
├── domain/build.gradle.kts         plugins: movies.jvm.library (+ movies.dagger)
├── data/build.gradle.kts           plugins: movies.android.feature.data         → api(projects.feature.watchlist.domain)
└── presentation/build.gradle.kts   plugins: movies.android.feature.presentation → api(projects.feature.watchlist.domain)
```

- `include(":feature:watchlist:domain")` etc. in `settings.gradle.kts`.
- Android namespace: `com.faas.myfavoritefilms.feature.watchlist.data` / `.presentation`.
- Rules in `moduleGraphAssert`: the new feature doesn't depend on the others, and the others don't depend on it.
- `"watchlist"` in `FEATURES` (`architecture-test/.../KonsistScopes.kt`).

## 2. domain

`model/`, `repository/WatchlistRepository` (interface), `usecase/` with `@Inject constructor` and a single `operator fun invoke`. No Android.

## 3. data

Follow [add-endpoint.md](add-endpoint.md) for each remote source. Local source: entity and DAO in `core:database`, `WatchlistLocalDataSource` in `datasource/`. Dagger module:

```kotlin
@Module(includes = [WatchlistDataBindsModule::class])
object WatchlistDataModule

@Module
internal interface WatchlistDataBindsModule {
    @Binds fun bindRepository(impl: WatchlistRepositoryImpl): WatchlistRepository
}
```

## 4. presentation

- `WatchlistUiState` (sealed interface), `WatchlistViewModel` with `@Inject constructor` taking only use cases (and `Analytics`, `SavedStateHandle` if needed).
- `WatchlistScreen` (gets the ViewModel with `daggerViewModel()`, collects with `collectAsStateWithLifecycle`) and `WatchlistContent` (stateless, previews).
- Reuse `core:ui` and `core:designsystem` before creating components.
- `TrackScreenView(ScreenNames.WATCHLIST)`.
- `di/WatchlistPresentationModule`: `@Binds @IntoMap @ViewModelKey(WatchlistViewModel::class)`.
- `navigation/`: `@Serializable data object WatchlistRoute`, `NavGraphBuilder.watchlistScreen(...)`, `NavController.navigateToWatchlist()`.
- Strings in `values/` and `values-en/`.

## 5. app

- `implementation` of `presentation` and `data` in `app/build.gradle.kts`.
- `WatchlistDataModule::class` in `AppComponent`; `WatchlistPresentationModule::class` in `ViewModelComponent`.
- `watchlistScreen(...)` in `MoviesNavHost`; a new tab → `TopLevelDestination`.
- The ViewModel in `DependencyGraphTest`.

## 6. Tests

Use case, ViewModel, repository (MockWebServer or Room), `WatchlistContentTest`, `WatchlistScreenshotTest`. Details in [write-tests.md](write-tests.md).

## 7. Documentation

- `ARCHITECTURE.md`: scope (section 1), structure (section 4), the feature's section.
- `README.md`: features table.
- `feature/AGENTS.md`: row in the features table.
- An ADR if the feature introduces a new pattern.

## 8. Verify

```bash
./scripts/verify.sh
```
