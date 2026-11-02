---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0016 — Tracking through multibinding

## Context and problem statement

We want to add or replace analytics tools without touching features and without `if (DEBUG)`.

## Considered options

* `Analytics`/`ErrorReporter` (JVM) with destinations in a `Set<Tracker>` through `@IntoSet`/`@ElementsIntoSet`
* The Firebase SDK called directly from features

## Decision outcome

Chosen option: Contracts in `core:analytics` and destinations registered through multibinding, dispatched on a serialized queue, because Open/Closed: a new destination is a new binding; features depend only on the abstraction.

### Consequences

* Good: Logcat only in debug through a source set
* Good: `TestAnalytics` in tests
* Good: A slow destination never blocks the main thread
* Bad: More indirection than calling the SDK

### Confirmation

Konsist: `only analytics-firebase and ai-firebase know the Firebase SDK`; `CompositeAnalyticsTest`.
