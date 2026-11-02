---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0015 — Shared list components in core:ui

## Context and problem statement

The main list and favorites show the same card and the same grid.

## Considered options

* `MovieCardUi`, `MovieCard`, `MovieGrid` and skeletons in `core:ui/movie`
* A card per feature

## Decision outcome

Chosen option: Components in `core:ui/movie`, because per ADR-0005, what two features use moves to the core.

### Consequences

* Good: Consistent visuals and shared screenshots
* Bad: A card change affects two screens (covered by screenshots)

### Confirmation

Screenshots of `MovieGrid` and of the screens that use it.
