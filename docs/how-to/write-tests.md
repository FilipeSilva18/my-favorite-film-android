# Escrever testes

Estratégia completa: ARCHITECTURE.md seção 13. Skill: [`test-engineer`](../../.agents/skills/test-engineer/SKILL.md).

## O que testar em cada camada

| Camada | Teste | Ferramentas | Referência no código |
|---|---|---|---|
| domain | `<UseCase>Test` (obrigatório) | JUnit4, coroutines-test, fakes | `GetMoviesUseCaseTest` |
| api de feature | `<Nome>UseCaseImplTest` (obrigatório) | Fake do repositório em `testFixtures` | `ToggleFavoriteUseCaseImplTest` |
| data remoto | `<X>RepositoryImplTest` | MockWebServer + JSON real em `src/test/resources/tmdb/` + `TmdbJson` | `MovieDetailRepositoryImplTest` |
| data local | `<X>RepositoryImplTest` | Robolectric + Room in-memory | `FavoritesRepositoryImplTest` |
| mapper | `<X>MapperTest` | JUnit4 | `MovieDetailMapperTest` |
| ViewModel | `<X>ViewModelTest` (obrigatório) | Turbine, `MainDispatcherRule`, `asSnapshot`, `TestAnalytics` | `MoviesViewModelTest` |
| UI | `<Tela>ContentTest` | Robolectric + `createComposeRule` | `MoviesContentTest` |
| Visual | `<Tela>ScreenshotTest` | Roborazzi, claro e escuro | `MoviesScreenshotTest` |
| DI | `DependencyGraphTest` | Dagger real | `:app` |
| E2E | instrumentado | AndroidX Test, Orchestrator | `MoviesAppNavigationTest` |

## Regras

- **Fakes, nunca mocks.** Fake de contrato do core → `core:testing`. Fake de contrato de feature consumida por outras → `testFixtures` do `api` ou do `domain`.
- **Nomes:** JVM em inglês entre crases, descrevendo o comportamento: `` `toggling an unfavorited movie adds it` ``. Instrumentados em camelCase.
- **Robolectric:** `@RunWith(AndroidJUnit4::class)` e `@Config(sdk = [36])`.
- **Strings em testes de UI:** sempre `context.getString(R.string.x)`; o locale do Robolectric é `en`.
- **Screenshots:** `@GraphicsMode(NATIVE)`, `qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi"`, `MoviesTheme(darkTheme = ..., dynamicColor = false)`. PNGs em `src/test/screenshots/` são versionados.
- **Paging em ViewModel:** fakes devolvem um `Pager` com um `PagingSource` simples; `PagingData.from` estático faz o `asSnapshot` travar.
- **Paging em UI:** `flowOf(PagingData.from(lista)).collectAsLazyPagingItems()`; estados com `PagingData.empty(sourceLoadStates = ...)`.
- **ViewModel com rota:** `SavedStateHandle(mapOf("movieId" to 603))`.
- **Elemento fora da tela:** `performScrollTo()` antes de `performClick()`.
- **Nenhum teste chama a rede real** (TMDB ou Gemini).

## Rodar

```bash
./scripts/verify-module.sh :feature:movies:presentation
./gradlew unitTest
./scripts/record-screenshots.sh :feature:movies:presentation
```
