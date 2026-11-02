# Architecture decision records (ADRs)

Every relevant decision lives in its own file, in the [MADR 4](https://adr.github.io/madr/) format. A summary of all of them is in the table in section 2 of [ARCHITECTURE.md](../../ARCHITECTURE.md); the details are here.

## Index

| ADR | Decision | Status |
|---|---|---|
| [0001](0001-google-architecture-layers.md) | Layers from Google's architecture guide | accepted |
| [0002](0002-pure-kotlin-domain.md) | Mandatory pure Kotlin domain | accepted |
| [0003](0003-three-modules-per-feature.md) | Three Gradle modules per feature | accepted |
| [0004](0004-viewmodel-talks-to-use-cases-only.md) | ViewModels only talk to use cases | accepted |
| [0005](0005-no-feature-to-feature-dependency.md) | Features never depend on other features | accepted |
| [0006](0006-favorites-in-core.md) | Favorites in core:domain and core:data | superseded by [ADR-0017](0017-feature-api-module.md) |
| [0007](0007-paging-3.md) | Paging 3 for paged lists | accepted |
| [0008](0008-type-safe-navigation-compose.md) | Navigation Compose with type-safe routes | accepted |
| [0009](0009-pure-dagger.md) | Pure Dagger 2, no Hilt | accepted |
| [0010](0010-online-movies-offline-favorites.md) | Online movies and offline favorites | accepted |
| [0011](0011-tmdb-token-outside-vcs.md) | TMDB token outside version control | accepted |
| [0012](0012-core-di-module.md) | core:di module for ViewModels | accepted |
| [0013](0013-public-internal-dagger-module-pair.md) | Dagger modules as a public + internal pair | accepted |
| [0014](0014-generic-server-driven-ui.md) | Generic server-driven UI in core | accepted |
| [0015](0015-shared-list-components-in-core-ui.md) | Shared list components in core:ui | accepted |
| [0016](0016-tracking-by-multibinding.md) | Tracking through multibinding | accepted |
| [0017](0017-feature-api-module.md) | api module for features consumed by other features | accepted |
| [0018](0018-repositories-depend-on-data-sources.md) | Repositories depend on data sources | accepted |

## Rules

- File name: `NNNN-title-with-dashes.md`, a sequential four-digit number. Numbers are never reused.
- Status: `proposed`, `accepted`, `rejected`, `deprecated` or `superseded by [ADR-NNNN](file.md)`.
- An accepted decision is not rewritten: when it changes, create a new ADR and mark the old one `superseded by`.
- Every ADR says how it is **confirmed** (Konsist test, module-graph-assert rule, specific test). A decision without automatic verification must say so explicitly.
- Update the table above and the table in section 2 of `ARCHITECTURE.md` in the same PR.

Template: [adr-template.md](adr-template.md). Step by step: [docs/how-to/write-adr.md](../how-to/write-adr.md).
