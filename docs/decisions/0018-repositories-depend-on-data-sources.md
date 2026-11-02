---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0018 — Repositories depend on data sources

## Context and problem statement

Repositories received a DAO or a Retrofit API directly, mixing source access with orchestration.

## Considered options

* `internal` data sources in `data/datasource` (`<Type>LocalDataSource`, `<Type>RemoteDataSource`)
* DAO/API directly in the repository

## Decision outcome

Chosen option: Repositories depend on data sources; each data source talks to a single source and returns data-layer models, because it is the data layer guide's shape; the repository focuses on policy (mapping, caching, errors).

### Consequences

* Good: Extension point for an offline cache
* Good: Consistency across repositories
* Bad: One more class per source

### Confirmation

Konsist: `repositories talk to data sources, never to a DAO or a Retrofit API directly`, `data sources are internal and live in data datasource`.
