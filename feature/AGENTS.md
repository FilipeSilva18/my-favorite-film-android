# feature/ — features

Complements the [root AGENTS.md](../AGENTS.md). Detailed checklist: [feature/README.md](README.md). Step by step: [docs/how-to/add-feature.md](../docs/how-to/add-feature.md).

## Features

| Feature | Modules | Reference for |
|---|---|---|
| `movies` | `domain`, `data`, `presentation` | Paging 3, debounced search, AI search, remote data source |
| `moviedetail` | `domain`, `data`, `presentation` | `SavedStateHandle`, SDUI, screen with states |
| `favorites` | `api`, `domain`, `data`, `presentation` | Feature consumed by others (ADR-0017), local data source (Room) |
| `settings` | `presentation` | UI-only feature over `core:domain` rules |

## Structure and plugins

```
feature/<name>/
├── api/           movies.jvm.library + java-test-fixtures   (only if another feature consumes it)
├── domain/        movies.jvm.library (+ movies.dagger)       model/ repository/ usecase/
├── data/          movies.android.feature.data                datasource/ remote/ mapper/ repository/ di/
└── presentation/  movies.android.feature.presentation        di/ navigation/ model/ <Screen>Screen/ViewModel/UiState
```

## Rules the build checks

- A feature only imports another through the `.api.` package and only depends on its `:api` module.
- `presentation` does not depend on `data`; `data` does not depend on `presentation`.
- `domain` without Android, Room, Retrofit, Compose or Dagger (only `javax.inject`).
- Use cases in `domain/usecase`, with a single public `operator fun invoke`.
- Repository: interface in `domain/repository`; `internal` `<Name>RepositoryImpl` in `data/repository`, depending on data sources.
- `internal` DTOs in `data/remote/dto`; `internal` data sources in `data/datasource`.
- ViewModels in `presentation`, ending in `ViewModel`, receiving only use cases, never exposing `MutableStateFlow`.
- `@Preview` functions private and ending in `Preview`. No literal text in a `@Composable`.

## Pure Dagger: watch out

`data` and `presentation` declare `api(projects.feature.<name>.domain)`. The `AppComponent` is generated in `:app` and must see the domain types. Without it KSP fails with `'...Repository' could not be resolved`.

## Minimum tests

`<UseCase>Test`, `<ViewModel>Test`, a data contract test with MockWebServer and real JSON in `src/test/resources/tmdb/`, `<Screen>ContentTest`, `<Screen>ScreenshotTest`. Details: [docs/how-to/write-tests.md](../docs/how-to/write-tests.md).
