# Consume a new TMDB endpoint

Example: `GET movie/{id}/reviews` in an existing feature. Context: ADR-0002, ADR-0013 and ADR-0018. Skill: [`data-integrator`](../../.agents/skills/data-integrator/SKILL.md).

## Steps

### 1. Domain (`feature:<x>:domain`, JVM)

- Model in `model/` (immutable `data class`, domain types, no `@SerialName`).
- Method in the `repository/<X>Repository` interface: `suspend fun ...(): AppResult<T>` for a single call, `fun ...(): Flow<PagingData<T>>` for a paged list.
- Use case in `usecase/`, `@Inject constructor`, a single public `operator fun invoke`.

### 2. Data (`feature:<x>:data`)

1. **Fixture:** save a real (trimmed) response in `src/test/resources/tmdb/<endpoint>.json`. Never include the token.
2. **DTO**, `internal`, in `remote/dto/`, `@Serializable`, `@SerialName` for `snake_case` fields, optional fields with default values.
3. **Retrofit API**, `internal`, in `remote/api/`: `@GET("movie/{movie_id}/reviews") suspend fun ...`.
4. **Data source**, `internal`, in `datasource/<X>RemoteDataSource`: one method per call, returns the DTO, no mapping and no error handling.
5. **Mapper** in `mapper/`: `fun XDto.toDomain(): X`. Images through `TmdbImages`.
6. **Repository**, `internal`, in `repository/<X>RepositoryImpl`:
   ```kotlin
   override suspend fun reviews(movieId: Int): AppResult<List<Review>> = safeApiCall(
       onFailure = { cause, error -> errorReporter.reportIfUnexpected(cause, error, origin = "movie_reviews") },
   ) {
       remoteDataSource.reviews(movieId).results.map { it.toDomain() }
   }
   ```
   Paged: a `Pager` with a `PagingSource` in `paging/` that calls the data source (see `MoviesPagingSource`).
7. **Dagger:** for a new API, a `@Provides @Singleton` in the `internal` module of `<X>DataModule`. Data sources with `@Inject constructor` need no binding.

### 3. Tests

| Test | Where | Covers |
|---|---|---|
| `<UseCase>Test` | domain | Rule with `Fake<X>Repository` |
| `<X>RepositoryImplTest` | data | MockWebServer + real fixture + `TmdbJson`: path, query, mapping, HTTP 401/404/500 → `DomainError` |
| `<X>MapperTest` | data | Null fields, images, formats |

### 4. Verify

```bash
./scripts/verify-module.sh :feature:<x>:data
./scripts/verify.sh --quick
```

## Rules the build enforces

- DTOs and data sources are `internal` and in the right packages.
- No `Api` or `Dao` in a repository constructor.
- Every use case has a test.

## Common mistakes

- Returning a DTO from the repository. What leaves `data` is a domain model.
- Catching `Exception` in the data source. Errors are handled once, in the repository's `safeApiCall`.
- Reporting 401, 404 or no network as non-fatal. `reportIfUnexpected` already filters; don't call `recordNonFatal` directly.
