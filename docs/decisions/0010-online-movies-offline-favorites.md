---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0010 — Online movies and offline favorites

## Context and problem statement

The v1 scope doesn't need a catalog cache, but favorites must survive without a network.

## Considered options

* Online movies, favorites in Room
* Full offline-first from the start

## Decision outcome

Chosen option: Movies straight from TMDB without a cache; favorites persisted in Room, because it delivers the essentials without the complexity of synchronization.

### Consequences

* Good: Less code in v1
* Bad: Without a network, the list doesn't load

### Confirmation

A future cache plugs in with `RemoteMediator` without changing `MoviesRepository` (ARCHITECTURE.md section 16).
