---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0013 — Dagger modules as a public + internal pair

## Context and problem statement

With pure Dagger the `AppComponent` references each layer's modules, but implementations and DTOs must stay `internal`.

## Considered options

* A public `object XxxModule` that includes an `internal interface XxxBindsModule`
* Everything public

## Decision outcome

Chosen option: A public + internal pair, because `:app` only sees the aggregator and the implementations stay `internal`.

### Consequences

* Good: Encapsulation preserved with pure Dagger
* Bad: Two types per Dagger module

### Confirmation

Konsist: `repository implementations are internal...`, `data sources are internal...`, `DTOs are internal...`.
