# core/ — infrastructure and cross-cutting rules

Complements the [root AGENTS.md](../AGENTS.md). Full module reference: [docs/reference/modules.md](../docs/reference/modules.md).

**`core` never depends on `feature`.** Something belongs here when two or more features need it, or when it is infrastructure (network, database, SDK).

## Modules

| Module | Type | Contents |
|---|---|---|
| `common` | JVM | `AppResult`, `DomainError`, `DomainException`, SDUI model (`sdui/SduiComponent`). Depends on no other module |
| `domain` | JVM | Cross-cutting rules: preferences (theme, language) |
| `data` | Android | Implementation of the `core:domain` repositories |
| `network` | Android | Retrofit, OkHttp, interceptors, `safeApiCall`, `TmdbImages`, `SduiParser` |
| `database` | Android | Room: `MoviesDatabase`, entities and DAOs (including feature ones) |
| `datastore` | Android | DataStore Preferences: `UserPreferencesDataSource` |
| `designsystem` | Compose | Theme, colors, typography, `MoviePoster`, `SearchField`, `LabelChips` |
| `ui` | Compose | `LoadingContent`/`ErrorContent`/`EmptyContent`, `core:ui/movie` (`MovieCard`, `MovieGrid`), SDUI renderer (`sduiItems`) |
| `di` | Compose | `ViewModelKey`, `DaggerViewModelFactory`, `daggerViewModel()` |
| `analytics` | JVM | `Analytics`, `ErrorReporter`, events, asynchronous composites over `Set<Tracker>` |
| `analytics-firebase` | Android | The only place with Firebase Analytics and Crashlytics |
| `ai` | JVM | `AiJsonGenerator`, `AiSchema`, `AiException` |
| `ai-firebase` | Android | The only place with Firebase AI Logic (Gemini) and App Check |
| `testing` | JVM | `MainDispatcherRule`, fakes of the core contracts (`TestAnalytics`, fake repositories) |

## Rules

- JVM modules (`common`, `domain`, `analytics`, `ai`, `testing`) do not import Android, Compose, Room, Retrofit or Dagger beyond `javax.inject`. Konsist enforces it.
- `network`, `database` and `datastore` do not depend on `domain` or `data`.
- A new third-party SDK goes behind a JVM contract here, with the implementation in a `-<vendor>` module (the `analytics`/`analytics-firebase` and `ai`/`ai-firebase` pattern).
- Every module with Dagger uses the public + `internal` pair (ADR-0013).
- A new contract in `core` gets a fake in `core:testing`.

## Common tasks

- New SDUI block → `sdui-designer` skill and [docs/how-to/add-sdui-block.md](../docs/how-to/add-sdui-block.md).
- Analytics event → [docs/how-to/add-analytics-event.md](../docs/how-to/add-analytics-event.md).
- Shared visual component → `designsystem` (unaware of domain models) or `ui` (knows `DomainError`, `MovieCardUi`).
- Entity/DAO for a feature → `core:database` (the central `@Database` must list the entity); access goes through a `LocalDataSource` in the feature's `data` module.
