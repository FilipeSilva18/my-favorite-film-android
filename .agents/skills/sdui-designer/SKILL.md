---
name: sdui-designer
description: Extends the generic Server-Driven UI vocabulary of the MyFavoriteFilms Android app - SduiComponent model in core common, SduiParser in core network, SduiComponentView renderer in core ui, the simulated backend layout service and their tests. Use when adding or changing a server-driven block type, its JSON contract, or the movie detail server-driven sections.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# SDUI designer

You evolve the server-driven block vocabulary (ADR-0014).

## Inputs

- Type name (`snake_case`) and an example JSON.
- Where it will appear first (today, the movie detail).

## Steps

Follow [add-sdui-block.md](../../../docs/how-to/add-sdui-block.md):

1. Model in `core/common/.../sdui/SduiComponent.kt`.
2. `TYPE_*` constant and branch in `SduiParser.toComponent()`.
3. Branch in `SduiComponentView` (`core/ui/.../sdui/SduiRenderer.kt`).
4. Generation in `MovieDetailLayoutService`, if the screen already uses it.
5. Tests: `SduiParserTest`, `SduiRendererTest`, screenshot.
6. Types table in section 7.10 of `ARCHITECTURE.md`.

## Contract rules

- Unknown types are ignored; never throw from the parser.
- A missing required field drops the block; an invalid list item is dropped; an empty list drops the block.
- Texts arrive ready to display; the app does not format server content.
- Icons are semantic keys; an unknown key means no icon.
- The model is pure JVM (`core:common`).

## Verify

```bash
./scripts/verify-module.sh :core:network
./scripts/verify-module.sh :core:ui
./scripts/verify-module.sh :feature:moviedetail:data
```
