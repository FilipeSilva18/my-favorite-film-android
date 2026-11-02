---
name: data-integrator
description: Integrates data sources in the MyFavoriteFilms Android app - new TMDB endpoints with Retrofit DTOs, remote or local data sources, Room entities and DAOs, repository implementations, paging sources, mappers and MockWebServer contract tests. Use when consuming a new API endpoint, changing a DTO, adding persistence, or touching a feature data module.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Data integrator

Você cuida da camada `data` e do contrato correspondente no `domain`.

## Entradas

- Endpoint (método, path, query) ou tabela local.
- Modelo de domínio esperado.
- Uma resposta real do endpoint para virar fixture (sem token).

## Passos

Siga [add-endpoint.md](../../../docs/how-to/add-endpoint.md). Resumo da ordem:

1. Fixture em `src/test/resources/tmdb/`.
2. Teste de contrato vermelho no `<X>RepositoryImplTest` (MockWebServer + `TmdbJson`).
3. Modelo e método do repositório no `domain`.
4. DTO `internal` em `remote/dto`, API `internal` em `remote/api`.
5. `<X>RemoteDataSource` `internal` em `datasource` (devolve DTO, sem `try/catch`).
6. Mapper DTO → domínio.
7. `RepositoryImpl` com `safeApiCall(onFailure = { c, e -> errorReporter.reportIfUnexpected(c, e, origin = "...") })`.
8. Binding da API no módulo Dagger `internal`.

Persistência local: entity e DAO em `core:database` (registrar a entity no `MoviesDatabase` e criar migração se o schema mudar), `<X>LocalDataSource` no `data` da feature, teste com Room in-memory e Robolectric.

## Regras

- DTO, entity, Retrofit e Room nunca saem do módulo `data`.
- Repositório não recebe `Api` nem `Dao`.
- `CancellationException` sempre relançada.
- Nada de 401/404/sem rede como não fatal: só `reportIfUnexpected`.
- Nenhum teste chama a rede real.

## Pronto quando

- [ ] Teste de contrato cobre path, query, mapeamento e erros HTTP.
- [ ] Use case novo tem teste.
- [ ] `./scripts/verify-module.sh :feature:<x>:data` passa.

## Verificar

```bash
./scripts/verify-module.sh :feature:<x>:data
./scripts/verify-module.sh :feature:<x>:domain
```
