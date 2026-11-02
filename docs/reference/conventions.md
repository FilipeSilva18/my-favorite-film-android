# Code conventions

Summary for quick lookup. The full version, with examples, is in section 14 of [ARCHITECTURE.md](../../ARCHITECTURE.md#14-code-conventions).

## Names

| Element | Pattern | Example |
|---|---|---|
| Route | `<Screen>Route` (`@Serializable`) | `MovieDetailRoute(movieId)` |
| Composable with a ViewModel | `<Screen>Screen` | `MovieDetailScreen` |
| Stateless composable | `<Screen>Content` | `MovieDetailContent` |
| Screen state | `<Screen>UiState` (sealed interface) | `MovieDetailUiState` |
| UI model | `<Name>Ui` | `MovieCardUi` |
| Use case | Verb + noun + `UseCase` | `GetMovieDetailUseCase` |
| Implementation of an `api` use case | `<Name>UseCaseImpl` | `ToggleFavoriteUseCaseImpl` |
| Repository | `<Name>Repository` / `<Name>RepositoryImpl` (`internal`) | `MoviesRepositoryImpl` |
| Data source | `<Type>LocalDataSource` / `<Type>RemoteDataSource` (`internal`) | `MoviesRemoteDataSource` |
| DTO | `<Name>Dto` (`internal`) | `MovieDto` |
| Entity | `<Name>Entity` | `FavoriteMovieEntity` |
| Dagger module | Public `<Feature><Layer>Module` + internal `<Feature><Layer>BindsModule` | `MoviesDataModule` |
| Navigation | `NavGraphBuilder.<screen>Screen()` / `NavController.navigateTo<Screen>()` | `movieDetailScreen()` |
| Preview | `private fun <Name>Preview()` with `@Preview` | `<Screen>Content<State>Preview` |
| Test | `<Class>Test`, same package | `MoviesViewModelTest` |
| Fake | `Fake<Contract>` / `Test<Contract>` | `FakeFavorites`, `TestAnalytics` |

## Rules

- **Visibility:** `internal` by default. Only the module's contract is public.
- **Immutability:** `UiState` and models are `data class` with `val` and `List`.
- **Use cases:** one responsibility, `operator fun invoke`, stateless.
- **ViewModels:** expose `StateFlow` or `Flow<PagingData>`, receive events through `onXxx()`, never expose `MutableStateFlow`.
- **Compose:** `*Content` receives state and lambdas, no ViewModel; previews for each state.
- **Strings:** `values/strings.xml` (pt-BR) and `values-en/strings.xml`, including `contentDescription` and units.
- **Coroutines:** never catch `CancellationException` without rethrowing it.
- **Errors:** data returns `AppResult`; the UI shows `DomainError.messageRes()`, never `e.message`.
- **Comments:** no explanatory comments in code; the why goes to ADRs.
- **Language:** English for code, tests, build messages, CI and documentation. User-facing text lives in string resources (pt-BR and en).
- **Tests:** JVM test names in backticks (`` `blank query returns popular movies` ``); instrumented tests in camelCase. Fakes, never mocks.
- **Dependencies:** versions only in `gradle/libs.versions.toml`; `api(...)` only when the type appears in the module's public API (or for `:app`'s KSP).
