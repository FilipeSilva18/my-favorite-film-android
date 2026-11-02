---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0008 — Navigation Compose with type-safe routes

## Context and problem statement

Features must be navigable without knowing each other.

## Considered options

* Navigation Compose with `@Serializable` routes; only `:app` knows all of them
* Navigation 3
* String routes

## Decision outcome

Chosen option: Type-safe Navigation Compose; each feature exposes `NavGraphBuilder` and `NavController` extensions, because it is Google's current recommendation and arguments are checked at compile time.

### Consequences

* Good: Decoupled features: `:app` wires the callbacks
* Bad: A future migration to Navigation 3 touches the `navigation/` packages

### Confirmation

`MoviesNavHost` in `:app`; routes in `presentation/navigation/`.
