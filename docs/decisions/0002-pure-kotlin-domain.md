---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0002 — Mandatory pure Kotlin domain

## Context and problem statement

In Google's guide the domain layer is optional and depends on data. We want business rules that are testable on the JVM and independent of Android, Retrofit and Room.

## Considered options

* Mandatory domain, JVM module, defines the repository interfaces (dependency inversion)
* Optional domain depending on data (the guide's model)
* No domain: the ViewModel talks to the repository

## Decision outcome

Chosen option: A mandatory domain as a JVM module that defines the interfaces, implemented by `data`, because it applies the Dependency Inversion Principle: the policy (domain) doesn't know the detail (data), and the tests run in milliseconds.

### Consequences

* Good: Domain tests without Robolectric
* Good: Replacing Retrofit or Room doesn't change the domain
* Bad: DTO → domain mapping in every repository

### Confirmation

Konsist: `domain, common and the AI contract are pure Kotlin...`; module-graph-assert: `.*:domain -X> :core:(data|network|database|datastore|ui|designsystem|di)`.
