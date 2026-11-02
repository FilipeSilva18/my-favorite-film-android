# Modules

27 Gradle modules. Base package: `com.faas.myfavoritefilms`. The source of truth is `settings.gradle.kts`.

## app and architecture tests

| Module | Type | Plugins | Responsibility |
|---|---|---|---|
| `:app` | Android app | `movies.android.application`, `compose`, `dagger`, `robolectric` | Composition root: `AppComponent`, `ViewModelComponent`, `MoviesNavHost`, bottom bar, App Check, debug/release variations |
| `:architecture-test` | JVM | `movies.jvm.library` | Konsist rules over the code of every module |

## core

| Module | Type | Responsibility | Depends on |
|---|---|---|---|
| `:core:common` | JVM | `AppResult`, `DomainError`, `DomainException`, SDUI model | — |
| `:core:domain` | JVM | Preferences: `UserPreferences`, `ThemeMode`, `AppLanguage`, use cases, `AppLanguageApplier` | `common` |
| `:core:data` | Android | `UserPreferencesRepositoryImpl` and mapper | `domain`, `datastore` |
| `:core:network` | Android | Retrofit, OkHttp, TMDB interceptors, `TmdbJson`, `safeApiCall`, `TmdbImages`, `SduiParser` | `common` |
| `:core:database` | Android | Room: `MoviesDatabase`, `FavoriteMovieEntity`, `FavoriteMovieDao` | — |
| `:core:datastore` | Android | `UserPreferencesDataSource` (DataStore Preferences) | — |
| `:core:designsystem` | Compose | `MoviesTheme`, colors, typography, `MoviePoster`, `SearchField`, `LabelChips` | — |
| `:core:ui` | Compose | `LoadingContent`, `ErrorContent`, `EmptyContent`, `DomainError.messageRes()`, `core:ui/movie` (`MovieCardUi`, `MovieCard`, `MovieGrid`), `sduiItems` | `designsystem`, `common`, `analytics` |
| `:core:di` | Compose | `ViewModelKey`, `DaggerViewModelFactory`, `daggerViewModel()` | — |
| `:core:analytics` | JVM | `Analytics`, `ErrorReporter`, `AnalyticsEvent`, asynchronous composites, `@AnalyticsDispatcher` | `common` |
| `:core:analytics-firebase` | Android | `FirebaseAnalyticsTracker`, `CrashlyticsErrorTracker`, `FirebaseModule` | `analytics` |
| `:core:ai` | JVM | `AiJsonGenerator`, `AiSchema`, `AiException` | — |
| `:core:ai-firebase` | Android | `GeminiJsonGenerator`, `SchemaMapper`, `UnavailableAiJsonGenerator`, `AiFirebaseModule`, `AppCheckInstaller` | `ai` |
| `:core:testing` | JVM | `MainDispatcherRule`, `TestAnalytics`, `FakeUserPreferencesRepository`, `FakeAppLanguageApplier` | `analytics`, `domain` |

## feature

| Module | Type | Responsibility |
|---|---|---|
| `:feature:movies:domain` | JVM | `Movie`, `Genre`, `MovieFilters`, `QueryInterpretation`, `MoviesRepository`, `MovieQueryInterpreter`, `GetMoviesUseCase`, `GetMoviesByFiltersUseCase`, `InterpretMovieQueryUseCase` |
| `:feature:movies:data` | Android | `MoviesRemoteDataSource`, `MoviesApi`, DTOs, `MoviesPagingSource`, `MoviesRepositoryImpl`, `AiMovieQueryInterpreter`, `MoviesDataModule` |
| `:feature:movies:presentation` | Compose | `MoviesScreen`, `MoviesViewModel`, route `MoviesRoute`, `MoviesPresentationModule` |
| `:feature:moviedetail:domain` | JVM | `MovieDetail`, `MovieDetailRepository`, `GetMovieDetailUseCase` |
| `:feature:moviedetail:data` | Android | `MovieDetailRemoteDataSource`, `MovieDetailApi`, `MovieDetailLayoutService` (simulated SDUI backend), `MovieDetailDataModule` |
| `:feature:moviedetail:presentation` | Compose | `MovieDetailScreen`, `MovieDetailViewModel`, route `MovieDetailRoute(movieId)` |
| `:feature:favorites:api` | JVM | `FavoriteMovie`, `ObserveFavoriteIdsUseCase`, `ObserveIsFavoriteUseCase`, `ToggleFavoriteUseCase`; `testFixtures`: `FakeFavorites` |
| `:feature:favorites:domain` | JVM | `FavoritesRepository`, `*UseCaseImpl`, `ObserveFavoritesUseCase`; `testFixtures`: `FakeFavoritesRepository` |
| `:feature:favorites:data` | Android | `FavoritesLocalDataSource`, `FavoritesRepositoryImpl`, `FavoritesDataModule` |
| `:feature:favorites:presentation` | Compose | `FavoritesScreen`, `FavoritesViewModel` |
| `:feature:settings:presentation` | Compose | `SettingsScreen`, `SettingsViewModel` (rules in `core:domain`) |

## Packages inside a feature module

```
domain/        model/  repository/  usecase/
data/          datasource/  remote/api/  remote/dto/  paging/  mapper/  repository/  di/
presentation/  di/  navigation/  model/  <Screen>UiState.kt  <Screen>ViewModel.kt  <Screen>Screen.kt
```
