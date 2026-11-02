# Add a server-driven UI block

When the "server" needs to show a block type the app doesn't know yet. Context: ADR-0014 and ARCHITECTURE.md section 7.10. Skill: [`sdui-designer`](../../.agents/skills/sdui-designer/SKILL.md).

## Before you start

- Is the block generic (useful to any screen)? If it is specific to one feature, it probably isn't SDUI.
- Can an existing block do it (`section_title`, `key_value_list`, `chip_group`, `text`, `link`, `divider`, `person_carousel`, `video_carousel`)?
- Texts arrive ready from the server. The app neither formats nor translates block content.

## Steps

1. **Model** — `core/common/.../sdui/SduiComponent.kt`: a new `data class` (or `data object`) implementing `SduiComponent`. Optional fields as `String?`; list items in their own `data class`.
2. **Parser** — `core/network/.../sdui/SduiParser.kt`:
   - a `TYPE_<NAME>` constant in `snake_case`;
   - a branch in the `when` of `toComponent()`;
   - missing required field → `null` (the block is dropped); invalid list item → dropped; list empty after filtering → block dropped.
3. **Renderer** — `core/ui/.../sdui/SduiRenderer.kt`: a branch in the `when` of `SduiComponentView`. Its own `private` composable if it is more than a few lines. `contentDescription` and fixed texts (if any) come from `core:ui`'s `strings.xml`.
4. **Simulated backend** — if a screen should already use the block, generate it in the feature's layout service (today `MovieDetailLayoutService` in `feature:moviedetail:data/sdui`). Labels come from `MovieDetailLayoutStrings`.
5. **Tests:**
   - `SduiParserTest`: valid JSON, missing required field, invalid items, empty list.
   - `SduiRendererTest`: the block shows up and callbacks (e.g. opening a URL) fire.
   - `CoreUiScreenshotTest` (or the screen's screenshot): light and dark; `./scripts/record-screenshots.sh :core:ui`.
   - `MovieDetailLayoutServiceTest`, if the simulated backend changed.
6. **Documentation** — a new row in the types table in section 7.10 of `ARCHITECTURE.md`.

## Verify

```bash
./scripts/verify-module.sh :core:network
./scripts/verify-module.sh :core:ui
```

## Common mistakes

- Throwing in the parser for an unknown type. The contract is to ignore it: old app versions must survive new blocks.
- Putting the model in `core:ui` or in the feature. The model is JVM (`core:common`) so the domain can carry it.
- Formatting dates or numbers in the renderer. That is the server's job.
