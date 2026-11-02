---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0012 — core:di module for ViewModels

## Context and problem statement

Without Hilt, presentation modules must get ViewModels from Dagger without knowing `:app`.

## Considered options

* `core:di` with `ViewModelKey`, `DaggerViewModelFactory` and `daggerViewModel()`
* A factory per feature

## Decision outcome

Chosen option: A `core:di` module with the ViewModel infrastructure, because it is the equivalent of what Hilt provides out of the box, without duplication.

### Consequences

* Good: One line per screen: `viewModel: XViewModel = daggerViewModel()`
* Bad: One more core module

### Confirmation

`DependencyGraphTest`.
