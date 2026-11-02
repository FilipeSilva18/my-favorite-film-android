---
name: feature-builder
description: Creates a new feature module set (domain, data, presentation, optional api) in the MyFavoriteFilms Android app, wiring Gradle, Dagger, navigation, analytics and tests following the project's Clean Architecture. Use when asked to add a new feature, screen with its own data, tab, or a feature that other features consume.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Feature builder

You build features end to end following the project's patterns.

## Inputs

- Feature name (lowercase, no hyphen: `watchlist`).
- What the user does on the screen and where the data comes from (TMDB endpoint, Room, `core:domain` rules).
- Whether another feature will consume something from it.

If any of these is missing, ask before creating modules.

## Steps

1. **Shape:** choose the modules with the table in section 0 of [add-feature.md](../../../docs/how-to/add-feature.md). A feature consumed by others gets an `api` module (ADR-0017); a UI-only feature over the core keeps only `presentation`.
2. **Reference:** open the closest existing feature and copy its structure. `movies` is the complete reference; `favorites`, the `api` reference; `settings`, the presentation-only reference.
3. **Modules and Gradle:** `build.gradle.kts` with the `movies.*` plugins, `include` in `settings.gradle.kts`, `api(projects.feature.<name>.domain)` in `data` and `presentation`.
4. **Graph rules:** add the feature to the `-X>` rules of `moduleGraphAssert` in the root build and to `FEATURES` in `architecture-test/.../KonsistScopes.kt`.
5. **domain:** models, repository interface, use cases.
6. **data:** delegate the details to the `data-integrator` skill ([add-endpoint.md](../../../docs/how-to/add-endpoint.md)).
7. **presentation:** delegate the details to the `compose-ui` skill.
8. **app:** dependencies, `<Name>DataModule` in `AppComponent`, `<Name>PresentationModule` in `ViewModelComponent`, route in `MoviesNavHost`, tab in `TopLevelDestination` if needed, ViewModel in `DependencyGraphTest`.
9. **Tests:** delegate to the `test-engineer` skill. Minimum: use cases, ViewModel, repository, `ContentTest`, `ScreenshotTest`.
10. **Docs:** delegate to the `docs-keeper` skill.

## Rules

- No dependency on another feature, except its `:api` module.
- `internal` implementations; Dagger modules as a public + `internal` pair.
- Repositories depend on data sources.
- ViewModels receive only use cases (plus `Analytics` and `SavedStateHandle`).
- No hardcoded text in Composables; no explanatory comments in code.

## Done when

- [ ] `./scripts/verify.sh` passes.
- [ ] The feature appears in `ARCHITECTURE.md`, `feature/AGENTS.md` and `docs/reference/modules.md`.
- [ ] No secret file in the diff.

## Verify

```bash
./scripts/verify.sh --quick
./scripts/verify-module.sh :feature:<name>:presentation
./scripts/verify.sh
```
