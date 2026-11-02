---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0007 — Paging 3 for paged lists

## Context and problem statement

The TMDB API is paginated and the list has infinite scrolling, search and retry.

## Considered options

* Paging 3 with `paging-common` (JVM) in the domain
* Manual pagination in the ViewModel

## Decision outcome

Chosen option: Paging 3, with `paging-common` in the domain, because it is the official solution, handles loading/error/retry per page and integrates with Compose.

### Consequences

* Good: `LoadState` ready for skeletons, footer and retry
* Good: `asSnapshot` for tests
* Bad: `PagingData` crosses the domain (accepted: `paging-common` is pure JVM)

### Confirmation

`MoviesViewModelTest` (asSnapshot) and `MoviesRepositoryImplTest`.
