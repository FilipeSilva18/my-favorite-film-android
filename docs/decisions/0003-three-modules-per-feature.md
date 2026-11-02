---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0003 — Three Gradle modules per feature

## Context and problem statement

Layer boundaries kept in packages depend on discipline. We want the compiler to stop a screen from touching Retrofit or Room.

## Considered options

* `domain`, `data` and `presentation` as separate modules
* Now in Android model: features are UI only and layers live in `core`
* One module per feature with layers in packages

## Decision outcome

Chosen option: Each feature has up to three modules: `domain`, `data`, `presentation`, because the module graph enforces the boundaries and enables incremental compilation per layer.

### Consequences

* Good: A feature can't import what it didn't declare
* Good: Parallel builds and compile avoidance
* Bad: More modules and more `build.gradle.kts` files (mitigated by convention plugins)

### Confirmation

Structure in `settings.gradle.kts`; module-graph-assert forbids `presentation -X> data`.
