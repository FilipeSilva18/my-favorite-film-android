# AGENTS.md

Instructions for AI agents and people working in this repository. Open [AGENTS.md](https://agents.md) format: it applies to any assistant. The `AGENTS.md` closest to the file being edited complements this one; an explicit request from the user takes precedence over both.

## Project

**MyFavoriteFilms** (`com.faas.myfavoritefilms`): Android movie app on top of the TMDB API.

- Kotlin 2.4, Jetpack Compose (Material 3, single Activity), type-safe Navigation Compose.
- Clean Architecture on top of Google's official guide: `presentation` → `domain` ← `data`.
- Monorepo with 27 Gradle modules: `:app`, 14 in `core/`, 11 in `feature/`, `:architecture-test`.
- DI with **pure Dagger 2** (no Hilt), KSP. Paging 3, Room, DataStore, Retrofit 3, Coil 3.
- Firebase: Analytics, Crashlytics, AI Logic (Gemini) and App Check, always behind contracts in `core`.

## Map

| Path | What it is | Instructions |
|---|---|---|
| `app/` | Composition root: `AppComponent`, `ViewModelComponent`, `MoviesNavHost`, bottom bar | [app/AGENTS.md](app/AGENTS.md) |
| `core/` | Infrastructure and cross-cutting rules (network, database, DataStore, design system, SDUI, analytics, AI) | [core/AGENTS.md](core/AGENTS.md) |
| `feature/` | `movies`, `moviedetail`, `favorites` (with `api`), `settings` | [feature/AGENTS.md](feature/AGENTS.md) |
| `build-logic/` | `movies.*` convention plugins | [build-logic/AGENTS.md](build-logic/AGENTS.md) |
| `architecture-test/` | Executable architecture rules (Konsist) | [architecture-test/AGENTS.md](architecture-test/AGENTS.md) |
| `docs/` | Documentation (Diátaxis) and ADRs (MADR) | [docs/README.md](docs/README.md) |
| `.agents/skills/` | Agent skills (open Agent Skills standard) | [docs/how-to/use-ai-agents.md](docs/how-to/use-ai-agents.md) |
| `scripts/` | Local verification and git hooks | [docs/reference/commands.md](docs/reference/commands.md) |
| `ARCHITECTURE.md` | Complete architecture document, with code excerpts | — |

## Commands

```bash
./scripts/verify.sh                         # everything the JVM CI job runs: graph, Konsist, tests, screenshots, lint
./scripts/verify-module.sh :feature:movies:presentation
./gradlew assertModuleGraph                 # module graph rules
./gradlew :architecture-test:unitTest       # Konsist rules
./gradlew unitTest                          # every JVM test (unit, integration, Compose, DI)
./scripts/record-screenshots.sh             # re-record Roborazzi references (review and commit the PNGs)
./gradlew :app:assembleDebug
```

Use JDK 21. Without `TMDB_ACCESS_TOKEN` in `local.properties` the app builds, but TMDB answers 401.

## Non-negotiable rules

1. **Secrets never go into Git.** The TMDB token lives only in `local.properties` (ignored) or in the `TMDB_ACCESS_TOKEN` CI secret. `app/google-services.json` is ignored and comes from the `GOOGLE_SERVICES_JSON` secret. The App Check debug token is never committed. Run `./scripts/install-git-hooks.sh` once.
2. **Layers:** `domain` is pure Kotlin (JVM) and defines the interfaces; `data` implements them; `presentation` only talks to use cases. A ViewModel never receives a repository.
3. **Features do not depend on features.** The only exception is the `:api` module of a feature consumed by others (today `:feature:favorites:api`). What two features share moves to `core`.
4. **Repositories depend on data sources**, never on a DAO or a Retrofit API (`<Type>LocalDataSource`/`<Type>RemoteDataSource` in `data/datasource`, `internal`).
5. **Implementations are `internal`.** Dagger modules come in pairs: a public `object XxxModule` that includes an `internal interface XxxBindsModule`.
6. **Firebase only in `core:analytics-firebase` and `core:ai-firebase`.** Features use `Analytics`, `ErrorReporter` and `AiJsonGenerator`.
7. **No hardcoded UI text.** Everything comes from `strings.xml` (`values/` pt-BR and `values-en/`), including `contentDescription`.
8. **No explanatory comments in code.** The reasoning goes to `docs/decisions/` or `ARCHITECTURE.md`.
9. **Everything in English:** code, tests, CI and documentation. JVM test names in backticks; instrumented tests in camelCase.
10. **Fakes, never mocks.** Shared fakes live in `core:testing` or in `testFixtures`.

Rules 2 to 7 are checked by `assertModuleGraph` and Konsist. If one fails, fix the code; do not relax the rule without a new ADR.

## Definition of done

- [ ] `./scripts/verify.sh` passes.
- [ ] Every new `UseCase`, `UseCaseImpl` and `ViewModel` has a `<Name>Test` (Konsist enforces it).
- [ ] New screens have `<Screen>ContentTest` and `<Screen>ScreenshotTest`; PNGs re-recorded and reviewed.
- [ ] New ViewModels registered in the `:app` `DependencyGraphTest`.
- [ ] New strings in `values/` and `values-en/`.
- [ ] New or changed decisions recorded in `docs/decisions/` and reflected in `ARCHITECTURE.md`.
- [ ] No secret, `local.properties` or `google-services.json` in the diff.

## How to work here

- Before creating UI, look in `core:ui` and `core:designsystem` (`MovieCard`, `MovieGrid`, `LoadingContent`, `ErrorContent`, `EmptyContent`, `SearchField`, `sduiItems`).
- Copy the pattern of an existing feature instead of inventing a new one. `movies` is the complete reference; `favorites` is the reference for an `api` module.
- Recurring tasks have skills in `.agents/skills/`: `feature-builder`, `data-integrator`, `compose-ui`, `sdui-designer`, `test-engineer`, `architecture-guardian`, `docs-keeper`.
- When code and documentation disagree, the code is the source of truth; update the documentation in the same PR.
