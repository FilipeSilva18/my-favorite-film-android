---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0009 — Pure Dagger 2, no Hilt

## Context and problem statement

We want the dependency graph explicit and readable, with visible scopes and subcomponents.

## Considered options

* Pure Dagger 2 with KSP
* Hilt
* Koin

## Decision outcome

Chosen option: Pure Dagger 2: a root `AppComponent` + a `ViewModelComponent` per ViewModel with `SavedStateHandle`, because full control of the graph, verified at compile time, without hidden aggregated generation.

### Consequences

* Good: Graph errors at compile time
* Good: Graph readable in two `:app` files
* Bad: Extra `api(...)` dependencies so `:app`'s KSP can see types (ARCHITECTURE.md 12.8)
* Bad: More code than Hilt

### Confirmation

`DependencyGraphTest` creates every ViewModel through the real graph.
