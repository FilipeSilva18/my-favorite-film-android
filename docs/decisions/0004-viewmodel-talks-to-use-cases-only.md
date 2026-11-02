---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0004 — ViewModels only talk to use cases

## Context and problem statement

The guide allows a ViewModel to access a repository when there is no logic. Exceptions make review subjective.

## Considered options

* ViewModels only use use cases, always
* Use cases only when there is logic

## Decision outcome

Chosen option: ViewModels receive only use cases in their constructor, because a single rule with no exceptions is easy to review and to check automatically.

### Consequences

* Good: Objective review
* Good: Use cases reusable across screens
* Bad: Thin use cases that just delegate to the repository

### Confirmation

Konsist: `ViewModel depends on use cases, never on repositories (ADR-04)`.
