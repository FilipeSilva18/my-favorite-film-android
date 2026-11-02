---
status: superseded by [ADR-0017](0017-feature-api-module.md)
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0006 — Favorites in core:domain and core:data

## Context and problem statement

`movies` and `moviedetail` needed to toggle and observe favorites without depending on each other.

## Considered options

* Favorites in `core:domain` + `core:data`
* A `favorites` feature with a public module

## Decision outcome

Chosen option: Favorites in `core:domain` + `core:data`, because it was the simplest way to share the rule while respecting ADR-0005.

### Consequences

* Good: Simple
* Bad: `core` started holding a specific feature's rule
* Bad: The favorites screen was separated from its own rule

### Confirmation

Superseded: the current code follows ADR-0017.
