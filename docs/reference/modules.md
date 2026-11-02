# Módulos

27 módulos Gradle. Pacote base: `com.faas.myfavoritefilms`. A fonte da verdade é o `settings.gradle.kts`.

## app e testes de arquitetura

| Módulo | Tipo | Plugins | Responsabilidade |
|---|---|---|---|
| `:app` | Android app | `movies.android.application`, `compose`, `dagger`, `robolectric` | Composition root: `AppComponent`, `ViewModelComponent`, `MoviesNavHost`, bottom bar, App Check, variações debug/release |
| `:architecture-test` | JVM | `movies.jvm.library` | Regras Konsist sobre o código de todos os módulos |

## core

| Módulo | Tipo | Responsabilidade | Depende de |
|---|---|---|---|
| `:core:common` | JVM | `AppResult`, `DomainError`, `DomainException`, modelo SDUI | — |
| `:core:domain` | JVM | Preferências: `UserPreferences`, `ThemeMode`, `AppLanguage`, use cases, `AppLanguageApplier` | `common` |
| `:core:data` | Android | `UserPreferencesRepositoryImpl` e mapper | `domain`, `datastore` |
| `:core:network` | Android | Retrofit, OkHttp, interceptors TMDB, `TmdbJson`, `safeApiCall`, `TmdbImages`, `SduiParser` | `common` |
| `:core:database` | Android | Room: `MoviesDatabase`, `FavoriteMovieEntity`, `FavoriteMovieDao` | — |
| `:core:datastore` | Android | `UserPreferencesDataSource` (DataStore Preferences) | — |
| `:core:designsystem` | Compose | `MoviesTheme`, cores, tipografia, `MoviePoster`, `SearchField`, `LabelChips` | — |
| `:core:ui` | Compose | `LoadingContent`, `ErrorContent`, `EmptyContent`, `DomainError.messageRes()`, `core:ui/movie` (`MovieCardUi`, `MovieCard`, `MovieGrid`), `sduiItems` | `designsystem`, `common`, `analytics` |
| `:core:di` | Compose | `ViewModelKey`, `DaggerViewModelFactory`, `daggerViewModel()` | — |
| `:core:analytics` | JVM | `Analytics`, `ErrorReporter`, `AnalyticsEvent`, composites assíncronos, `@AnalyticsDispatcher` | `common` |
| `:core:analytics-firebase` | Android | `FirebaseAnalyticsTracker`, `CrashlyticsErrorTracker`, `FirebaseModule` | `analytics` |
| `:core:ai` | JVM | `AiJsonGenerator`, `AiSchema`, `AiException` | — |
| `:core:ai-firebase` | Android | `GeminiJsonGenerator`, `SchemaMapper`, `UnavailableAiJsonGenerator`, `AiFirebaseModule`, `AppCheckInstaller` | `ai` |
| `:core:testing` | JVM | `MainDispatcherRule`, `TestAnalytics`, `FakeUserPreferencesRepository`, `FakeAppLanguageApplier` | `analytics`, `domain` |

## feature

| Módulo | Tipo | Responsabilidade |
|---|---|---|
| `:feature:movies:domain` | JVM | `Movie`, `Genre`, `MovieFilters`, `QueryInterpretation`, `MoviesRepository`, `MovieQueryInterpreter`, `GetMoviesUseCase`, `GetMoviesByFiltersUseCase`, `InterpretMovieQueryUseCase` |
| `:feature:movies:data` | Android | `MoviesRemoteDataSource`, `MoviesApi`, DTOs, `MoviesPagingSource`, `MoviesRepositoryImpl`, `AiMovieQueryInterpreter`, `MoviesDataModule` |
| `:feature:movies:presentation` | Compose | `MoviesScreen`, `MoviesViewModel`, rota `MoviesRoute`, `MoviesPresentationModule` |
| `:feature:moviedetail:domain` | JVM | `MovieDetail`, `MovieDetailRepository`, `GetMovieDetailUseCase` |
| `:feature:moviedetail:data` | Android | `MovieDetailRemoteDataSource`, `MovieDetailApi`, `MovieDetailLayoutService` (backend SDUI simulado), `MovieDetailDataModule` |
| `:feature:moviedetail:presentation` | Compose | `MovieDetailScreen`, `MovieDetailViewModel`, rota `MovieDetailRoute(movieId)` |
| `:feature:favorites:api` | JVM | `FavoriteMovie`, `ObserveFavoriteIdsUseCase`, `ObserveIsFavoriteUseCase`, `ToggleFavoriteUseCase`; `testFixtures`: `FakeFavorites` |
| `:feature:favorites:domain` | JVM | `FavoritesRepository`, `*UseCaseImpl`, `ObserveFavoritesUseCase`; `testFixtures`: `FakeFavoritesRepository` |
| `:feature:favorites:data` | Android | `FavoritesLocalDataSource`, `FavoritesRepositoryImpl`, `FavoritesDataModule` |
| `:feature:favorites:presentation` | Compose | `FavoritesScreen`, `FavoritesViewModel` |
| `:feature:settings:presentation` | Compose | `SettingsScreen`, `SettingsViewModel` (regras no `core:domain`) |

## Pacotes dentro de um módulo de feature

```
domain/        model/  repository/  usecase/
data/          datasource/  remote/api/  remote/dto/  paging/  mapper/  repository/  di/
presentation/  di/  navigation/  model/  <Tela>UiState.kt  <Tela>ViewModel.kt  <Tela>Screen.kt
```
