# Consumir um novo endpoint da TMDB

Exemplo: `GET movie/{id}/reviews` numa feature existente. Contexto: ADR-0002, ADR-0013 e ADR-0018. Skill: [`data-integrator`](../../.agents/skills/data-integrator/SKILL.md).

## Passos

### 1. Domain (`feature:<x>:domain`, JVM)

- Modelo em `model/` (`data class` imutável, tipos do domínio, nada de `@SerialName`).
- Método na interface `repository/<X>Repository`: `suspend fun ...(): AppResult<T>` para chamada única, `fun ...(): Flow<PagingData<T>>` para lista paginada.
- Use case em `usecase/`, `@Inject constructor`, um `operator fun invoke` público.

### 2. Data (`feature:<x>:data`)

1. **Fixture:** salve uma resposta real (reduzida) em `src/test/resources/tmdb/<endpoint>.json`. Nunca inclua o token.
2. **DTO** `internal` em `remote/dto/`, `@Serializable`, `@SerialName` para campos `snake_case`, campos opcionais com valor padrão.
3. **API Retrofit** `internal` em `remote/api/`: `@GET("movie/{movie_id}/reviews") suspend fun ...`.
4. **Data source** `internal` em `datasource/<X>RemoteDataSource`: um método por chamada, devolve DTO, sem mapear e sem tratar erro.
5. **Mapper** em `mapper/`: `fun XDto.toDomain(): X`. Imagens via `TmdbImages`.
6. **Repositório** `internal` em `repository/<X>RepositoryImpl`:
   ```kotlin
   override suspend fun reviews(movieId: Int): AppResult<List<Review>> = safeApiCall(
       onFailure = { cause, error -> errorReporter.reportIfUnexpected(cause, error, origin = "movie_reviews") },
   ) {
       remoteDataSource.reviews(movieId).results.map { it.toDomain() }
   }
   ```
   Paginado: `Pager` com um `PagingSource` em `paging/` que chama o data source (veja `MoviesPagingSource`).
7. **Dagger:** se a API é nova, `@Provides @Singleton` no módulo `internal` do `<X>DataModule`. Data sources com `@Inject constructor` não precisam de binding.

### 3. Testes

| Teste | Onde | Cobre |
|---|---|---|
| `<UseCase>Test` | domain | Regra com `Fake<X>Repository` |
| `<X>RepositoryImplTest` | data | MockWebServer + fixture real + `TmdbJson`: path, query, mapeamento, HTTP 401/404/500 → `DomainError` |
| `<X>MapperTest` | data | Campos nulos, imagens, formatos |

### 4. Verificar

```bash
./scripts/verify-module.sh :feature:<x>:data
./scripts/verify.sh --quick
```

## Regras que o build cobra

- DTO e data source `internal` nos pacotes certos.
- Repositório sem `Api` ou `Dao` no construtor.
- Use case com teste.

## Erros comuns

- Devolver DTO pelo repositório. O que sai do `data` é modelo de domínio.
- Capturar `Exception` no data source. Erros são tratados uma vez, no `safeApiCall` do repositório.
- Reportar 401, 404 ou falta de rede como não fatal. `reportIfUnexpected` já filtra; não chame `recordNonFatal` direto.
