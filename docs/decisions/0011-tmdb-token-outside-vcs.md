---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0011 — TMDB token outside version control

## Context and problem statement

The TMDB read token is a secret and can't appear in the repository.

## Considered options

* `local.properties` → `BuildConfig` of `core:network`; secret in CI
* Hardcoded in the code
* Backend proxy

## Decision outcome

Chosen option: Token in `local.properties` (ignored) and in the `TMDB_ACCESS_TOKEN` CI secret, exposed through `BuildConfig`, because it keeps the secret out of Git with a simple setup.

### Consequences

* Good: No committed secret
* Bad: The token ships inside the APK (acceptable for a read token; a proxy would be required for a serious release)

### Confirmation

`.gitignore` and the `scripts/git-hooks/pre-commit` hook.
