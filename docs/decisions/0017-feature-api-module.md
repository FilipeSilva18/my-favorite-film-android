---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0017 — api module for features consumed by other features

## Context and problem statement

Favorites in the core (ADR-0006) mixed a feature rule with cross-cutting rules. `movies` and `moviedetail` still need to toggle favorites without depending on the whole feature.

## Considered options

* `:feature:favorites:api` (JVM) with the model and thin interfaces + `domain`/`data`/`presentation`
* Keep it in the core
* A single `:impl` with layers in packages
* An `api` module in every feature

## Decision outcome

Chosen option: An `api` module only for features consumed by others; the feature keeps its three layer modules, because it applies Dependency Inversion and Interface Segregation between modules; the `api` module solves visibility and the layers solve internal organization.

### Consequences

* Good: Core only with cross-cutting rules
* Good: Compile avoidance: consumers don't recompile when the implementation changes
* Good: Fakes in `testFixtures`
* Bad: Public `*UseCaseImpl` classes, because the binding lives in `data` (protected by the graph)

### Confirmation

module-graph-assert: `:feature:(movies|moviedetail|settings):.* -X> :feature:favorites:(domain|data|presentation)`; Konsist: another feature is imported only through `.api.`.
