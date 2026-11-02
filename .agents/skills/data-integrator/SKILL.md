---
name: data-integrator
description: Integrates data sources in the MyFavoriteFilms Android app - new TMDB endpoints with Retrofit DTOs, remote or local data sources, Room entities and DAOs, repository implementations, paging sources, mappers and MockWebServer contract tests. Use when consuming a new API endpoint, changing a DTO, adding persistence, or touching a feature data module.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Data integrator

You own the `data` layer and the matching contract in `domain`.

## Inputs

- Endpoint (method, path, query) or local table.
- Expected domain model.
- A real response from the endpoint to use as a fixture (without the token).

## Steps

Follow [add-endpoint.md](../../../docs/how-to/add-endpoint.md). Order:

1. Fixture in `src/test/resources/tmdb/`.
2. Failing contract test in `<X>RepositoryImplTest` (MockWebServer + `TmdbJson`).
3. Model and repository method in `domain`.
4. `internal` DTO in `remote/dto`, `internal` API in `remote/api`.
5. `internal` `<X>RemoteDataSource` in `datasource` (returns DTOs, no `try/catch`).
6. DTO → domain mapper.
7. `RepositoryImpl` with `safeApiCall(onFailure = { c, e -> errorReporter.reportIfUnexpected(c, e, origin = "...") })`.
8. API binding in the `internal` Dagger module.

Local persistence: entity and DAO in `core:database` (register the entity in `MoviesDatabase` and add a migration if the schema changes), `<X>LocalDataSource` in the feature's `data` module, test with in-memory Room and Robolectric.

## Rules

- DTOs, entities, Retrofit and Room never leave the `data` module.
- Repositories don't receive an `Api` or a `Dao`.
- `CancellationException` is always rethrown.
- 401/404/no network are never non-fatal reports: only `reportIfUnexpected`.
- No test calls the real network.

## Done when

- [ ] The contract test covers path, query, mapping and HTTP errors.
- [ ] Every new use case has a test.
- [ ] `./scripts/verify-module.sh :feature:<x>:data` passes.

## Verify

```bash
./scripts/verify-module.sh :feature:<x>:data
./scripts/verify-module.sh :feature:<x>:domain
```
