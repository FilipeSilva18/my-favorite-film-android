# feature/

Full step by step: [docs/how-to/add-feature.md](../docs/how-to/add-feature.md). Agents: the [`feature-builder`](../.agents/skills/feature-builder/SKILL.md) skill.

Current features: `movies` (list + AI search), `moviedetail` (detail + SDUI), `favorites` (`api` + `domain`/`data`/`presentation`, ADR-17) and `settings` (presentation only).

**Before creating UI, reuse the core:** `MovieCard`/`MovieGrid`/skeletons (`core:ui/movie`),
`LoadingContent`/`ErrorContent`/`EmptyContent` (`core:ui`), `MoviePoster`/`SearchField`/`LabelChips`
(`core:designsystem`) and `sduiItems(...)` for server-driven blocks (`core:ui/sdui`).

Each feature lives in up to three Gradle modules (ADR-03 in ARCHITECTURE.md):

```
feature/<name>/
├── domain/        plugin movies.jvm.library (+ movies.dagger if it has @Inject)
├── data/          plugin movies.android.feature.data
└── presentation/  plugin movies.android.feature.presentation
```

## Checklist for adding a feature

1. Create the three modules and add the `include`s in `settings.gradle.kts`.
   Add the feature to the `-X>` rules of `moduleGraphAssert` (root build) and to `FEATURES` in `architecture-test/.../KonsistScopes.kt`.
2. **domain**: models, repository interface, use cases with `@Inject constructor`.
3. **data**: `internal` DTOs, `internal` Retrofit interface, `internal` `RepositoryImpl`.
   The repository doesn't receive the Retrofit API or the DAO directly: create an `internal`
   `<Type>RemoteDataSource` (or `<Type>LocalDataSource` for Room) in `data/datasource`, which talks only to that
   source and returns DTOs/entities. The repository depends on the data source and maps to the domain (ADR-18).
   Dagger module with the public + internal pattern:
   ```kotlin
   @Module(includes = [MoviesDataBindsModule::class])
   object MoviesDataModule

   @Module
   internal interface MoviesDataBindsModule {
       @Binds fun bindRepository(impl: MoviesRepositoryImpl): MoviesRepository
       companion object {
           @Provides fun provideApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)
       }
   }
   ```
   and add `MoviesDataModule::class` to the `modules` list of `AppComponent`.
4. **presentation**: ViewModel with `@Inject constructor` (it may ask for `SavedStateHandle`),
   ViewModel module:
   ```kotlin
   @Module
   interface MoviesPresentationModule {
       @Binds @IntoMap @ViewModelKey(MoviesViewModel::class)
       fun bindMoviesViewModel(vm: MoviesViewModel): ViewModel
   }
   ```
   and add `MoviesPresentationModule::class` to `:app`'s `ViewModelComponent`.
   On the screen: `viewModel: MoviesViewModel = daggerViewModel()`.
5. **navigation**: `@Serializable` route, `NavGraphBuilder.<screen>Screen()` and
   `NavController.navigateTo<Screen>()` in `presentation/navigation/`; register it in `MoviesNavHost`.
6. `:app` starts depending on `feature:<name>:presentation` and `feature:<name>:data`.
7. For a new tab: an entry in the `TopLevelDestination` enum (`app/navigation`).

A feature that only uses core rules (like `settings`) can have **only the presentation module**,
with `api(projects.core.domain)`.

**A feature consumed by other features** (like `favorites`) gets an `api` module on top of the three layer modules (ADR-17):

```
feature/<name>/
├── api/           plugin movies.jvm.library + `java-test-fixtures`: models and thin use case interfaces; fakes in src/testFixtures
├── domain/        implements the api interfaces (`<Name>UseCaseImpl`) + the feature's internal use cases
├── data/          repository + `<Name>DataModule`, which binds the api interfaces to their implementations
└── presentation/  screen, ViewModel and navigation
```

- Consumers declare `api(projects.feature.<name>.api)` in their own `domain` and, in tests, `testImplementation(testFixtures(projects.feature.<name>.api))`.
- `<Name>UseCaseImpl` lives in `domain/usecase` and needs a `<Name>UseCaseImplTest` (Konsist enforces it). The binding lives in `data`, because `domain` can't have Dagger.
- Another feature never depends on this one's `domain`, `data` or `presentation`: module-graph-assert fails.

> **Watch out (pure Dagger):** `data` and `presentation` declare the feature's `domain` as **`api`**,
> not `implementation`. The Dagger components are generated in `:app` and must see the domain
> types (repository, use cases). Without it: `ComponentProcessingStep was unable to process
> AppComponent because '...Repository' could not be resolved`.

## Tests for a new feature (Konsist enforces the first two)

1. `<Name>UseCaseTest` for each use case (domain, fakes).
2. `<Name>ViewModelTest` (Turbine / `asSnapshot`, `MainDispatcherRule`).
3. Data: contract test with MockWebServer + real JSON in `src/test/resources/tmdb/`.
4. `<Screen>ContentTest` (Compose + Robolectric) covering data, loading, error and empty.
5. `<Screen>ScreenshotTest` (Roborazzi, light/dark) and `./gradlew recordRoborazziDebug`.
6. Register the new ViewModel in `:app`'s `DependencyGraphTest`.

## Tracking in a new feature

- Screen: `TrackScreenView(ScreenNames.X)` at the top of the `*Screen` (add the name to `ScreenNames`).
- Actions: inject `Analytics` in the ViewModel and call `analytics.track(...)` with a `core:analytics` factory.
- Unexpected errors in data: `safeApiCall(onFailure = { c, e -> errorReporter.reportIfUnexpected(c, e, "origin") })`.
- Never import `com.google.firebase.*` outside `core:analytics-firebase` and `core:ai-firebase` (Konsist fails). To use AI in a feature, depend only on `core:ai` (`AiJsonGenerator`) and implement the domain contract in `data`, like `AiMovieQueryInterpreter`.
