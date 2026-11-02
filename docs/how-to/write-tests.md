# Write tests

Full strategy: ARCHITECTURE.md section 13. Skill: [`test-engineer`](../../.agents/skills/test-engineer/SKILL.md).

## What to test in each layer

| Layer | Test | Tools | Reference in the code |
|---|---|---|---|
| domain | `<UseCase>Test` (mandatory) | JUnit 4, coroutines-test, fakes | `GetMoviesUseCaseTest` |
| feature api | `<Name>UseCaseImplTest` (mandatory) | Repository fake in `testFixtures` | `ToggleFavoriteUseCaseImplTest` |
| remote data | `<X>RepositoryImplTest` | MockWebServer + real JSON in `src/test/resources/tmdb/` + `TmdbJson` | `MovieDetailRepositoryImplTest` |
| local data | `<X>RepositoryImplTest` | Robolectric + in-memory Room | `FavoritesRepositoryImplTest` |
| mapper | `<X>MapperTest` | JUnit 4 | `MovieDetailMapperTest` |
| ViewModel | `<X>ViewModelTest` (mandatory) | Turbine, `MainDispatcherRule`, `asSnapshot`, `TestAnalytics` | `MoviesViewModelTest` |
| UI | `<Screen>ContentTest` | Robolectric + `createComposeRule` | `MoviesContentTest` |
| Visual | `<Screen>ScreenshotTest` | Roborazzi, light and dark | `MoviesScreenshotTest` |
| DI | `DependencyGraphTest` | Real Dagger | `:app` |
| E2E | instrumented | AndroidX Test, Orchestrator | `MoviesAppNavigationTest` |

## Rules

- **Fakes, never mocks.** Fakes of core contracts → `core:testing`. Fakes of a feature consumed by others → `testFixtures` of its `api` or `domain` module.
- **Names:** JVM tests in English in backticks, describing behavior: `` `toggling an unfavorited movie adds it` ``. Instrumented tests in camelCase.
- **Robolectric:** `@RunWith(AndroidJUnit4::class)` and `@Config(sdk = [36])`.
- **Strings in UI tests:** always `context.getString(R.string.x)`; Robolectric's locale is `en`.
- **Screenshots:** `@GraphicsMode(NATIVE)`, `qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi"`, `MoviesTheme(darkTheme = ..., dynamicColor = false)`. PNGs in `src/test/screenshots/` are committed.
- **Paging in ViewModel tests:** fakes return a `Pager` with a simple `PagingSource`; a static `PagingData.from` makes `asSnapshot` hang.
- **Paging in UI tests:** `flowOf(PagingData.from(list)).collectAsLazyPagingItems()`; states with `PagingData.empty(sourceLoadStates = ...)`.
- **ViewModel with a route:** `SavedStateHandle(mapOf("movieId" to 603))`.
- **Off-screen element:** `performScrollTo()` before `performClick()`.
- **No test calls the real network** (TMDB or Gemini).

## Run

```bash
./scripts/verify-module.sh :feature:movies:presentation
./gradlew unitTest
./scripts/record-screenshots.sh :feature:movies:presentation
```
