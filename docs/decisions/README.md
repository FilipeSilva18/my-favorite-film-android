# Decisões de arquitetura (ADRs)

Cada decisão relevante vive num arquivo próprio, no formato [MADR 4](https://adr.github.io/madr/) em português. O resumo de todas está na tabela da seção 2 do [ARCHITECTURE.md](../../ARCHITECTURE.md); o detalhe, aqui.

## Índice

| ADR | Decisão | Status |
|---|---|---|
| [0001](0001-google-architecture-layers.md) | Camadas do guia de arquitetura do Google | accepted |
| [0002](0002-pure-kotlin-domain.md) | Domain obrigatório e Kotlin puro | accepted |
| [0003](0003-three-modules-per-feature.md) | Três módulos Gradle por feature | accepted |
| [0004](0004-viewmodel-talks-to-use-cases-only.md) | ViewModel só conversa com use cases | accepted |
| [0005](0005-no-feature-to-feature-dependency.md) | Feature nunca depende de outra feature | accepted |
| [0006](0006-favorites-in-core.md) | Favoritos em core:domain e core:data | superseded by [ADR-0017](0017-feature-api-module.md) |
| [0007](0007-paging-3.md) | Paging 3 para listas paginadas | accepted |
| [0008](0008-type-safe-navigation-compose.md) | Navigation Compose com rotas type-safe | accepted |
| [0009](0009-pure-dagger.md) | Dagger 2 puro, sem Hilt | accepted |
| [0010](0010-online-movies-offline-favorites.md) | Filmes online e favoritos offline | accepted |
| [0011](0011-tmdb-token-outside-vcs.md) | Token da TMDB fora do controle de versão | accepted |
| [0012](0012-core-di-module.md) | Módulo core:di para ViewModels | accepted |
| [0013](0013-public-internal-dagger-module-pair.md) | Módulos Dagger em par público + interno | accepted |
| [0014](0014-generic-server-driven-ui.md) | Server-Driven UI genérico no core | accepted |
| [0015](0015-shared-list-components-in-core-ui.md) | Componentes de lista compartilhados em core:ui | accepted |
| [0016](0016-tracking-by-multibinding.md) | Tracking por multibinding | accepted |
| [0017](0017-feature-api-module.md) | Módulo api para features consumidas por outras | accepted |
| [0018](0018-repositories-depend-on-data-sources.md) | Repositórios dependem de data sources | accepted |

## Regras

- Nome do arquivo: `NNNN-titulo-em-ingles-com-hifens.md`, número sequencial de quatro dígitos. Números nunca são reaproveitados.
- Status: `proposed`, `accepted`, `rejected`, `deprecated` ou `superseded by [ADR-NNNN](arquivo.md)`.
- Uma decisão aceita não é reescrita: quando mudar, crie uma ADR nova e marque a antiga como `superseded by`.
- Toda ADR diz como é **confirmada** (teste Konsist, regra do module-graph-assert, teste específico). Decisão sem verificação automática deve dizer isso explicitamente.
- Atualize a tabela acima e a tabela da seção 2 do `ARCHITECTURE.md` no mesmo PR.

Modelo: [adr-template.md](adr-template.md). Passo a passo: [docs/how-to/write-adr.md](../how-to/write-adr.md).
