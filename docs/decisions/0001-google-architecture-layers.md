---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0001 — Layers from Google's architecture guide

## Context and problem statement

The app needs a separation of responsibilities that the industry and AI agents already know, with unidirectional data flow and observable state.

## Considered options

* Layers from the official guide (UI, Domain, Data) named `presentation`, `domain` and `data`
* MVP/MVC with Activities and Fragments
* A custom architecture with no external reference

## Decision outcome

Chosen option: Layers from the official guide named `presentation`, `domain` and `data`, because it aligns the project with Google's documentation (UDF, `StateFlow`, repositories, use cases), which makes onboarding and review easier.

### Consequences

* Good: Shared vocabulary with the official documentation and Now in Android
* Good: UDF: state flows down, events flow up
* Bad: More files per feature than layerless MVVM

### Confirmation

Konsist: `ViewModel subclasses end with ViewModel and live in presentation`, `presentation never imports the data layer`.
