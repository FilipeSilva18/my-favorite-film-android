---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0005 — Features never depend on other features

## Context and problem statement

Lateral dependencies between features create cycles and coupling that prevent evolving a feature in isolation.

## Considered options

* Forbid dependencies between features; shared code moves to `core`
* Allow occasional dependencies

## Decision outcome

Chosen option: Features don't depend on features; shared code moves to `core` (with the controlled exception of ADR-0017), because it avoids lateral coupling and cycles.

### Consequences

* Good: Features evolve and can be removed in isolation
* Bad: `core` can grow too much if everything moves there without criteria (see ADR-0017)

### Confirmation

module-graph-assert: `:feature:x:.* -X> :feature:y:.*` rules; Konsist: `a feature only reaches another feature through its public api module (ADR-05)`.
