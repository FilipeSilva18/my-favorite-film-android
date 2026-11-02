---
status: accepted
date: 2026-11-01
decision-makers: Filipe Silva
---

# 0014 — Generic server-driven UI in core

## Context and problem statement

The movie detail has blocks whose order and content are decided by the server, and other screens may want the same.

## Considered options

* Model in `core:common`, parser in `core:network`, renderer in `core:ui`
* SDUI inside the feature

## Decision outcome

Chosen option: Generic SDUI in the core; features only produce and consume `List<SduiComponent>`, because any screen reuses the parser and the renderer; unknown types are ignored.

### Consequences

* Good: The backend evolves ahead of the app without breaking old versions
* Bad: The block vocabulary needs governance (`add-sdui-block` guide)

### Confirmation

`SduiParserTest`, `SduiRendererTest`.
