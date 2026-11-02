# core/ — infraestrutura e regras transversais

Complementa o [AGENTS.md da raiz](../AGENTS.md). Referência completa dos módulos: [docs/reference/modules.md](../docs/reference/modules.md).

**`core` nunca depende de `feature`.** Algo entra aqui quando duas ou mais features precisam dele, ou quando é infraestrutura (rede, banco, SDK).

## Módulos

| Módulo | Tipo | Conteúdo |
|---|---|---|
| `common` | JVM | `AppResult`, `DomainError`, `DomainException`, modelo SDUI (`sdui/SduiComponent`). Não depende de nenhum outro módulo |
| `domain` | JVM | Regras transversais: preferências (tema, idioma) |
| `data` | Android | Implementação dos repositórios do `core:domain` |
| `network` | Android | Retrofit, OkHttp, interceptors, `safeApiCall`, `TmdbImages`, `SduiParser` |
| `database` | Android | Room: `MoviesDatabase`, entities e DAOs (inclusive os de feature) |
| `datastore` | Android | DataStore Preferences: `UserPreferencesDataSource` |
| `designsystem` | Compose | Tema, cores, tipografia, `MoviePoster`, `SearchField`, `LabelChips` |
| `ui` | Compose | `LoadingContent`/`ErrorContent`/`EmptyContent`, `core:ui/movie` (`MovieCard`, `MovieGrid`), renderer SDUI (`sduiItems`) |
| `di` | Compose | `ViewModelKey`, `DaggerViewModelFactory`, `daggerViewModel()` |
| `analytics` | JVM | `Analytics`, `ErrorReporter`, eventos, composites assíncronos com `Set<Tracker>` |
| `analytics-firebase` | Android | Único lugar com Firebase Analytics e Crashlytics |
| `ai` | JVM | `AiJsonGenerator`, `AiSchema`, `AiException` |
| `ai-firebase` | Android | Único lugar com Firebase AI Logic (Gemini) e App Check |
| `testing` | JVM | `MainDispatcherRule`, fakes dos contratos de core (`TestAnalytics`, repositórios fake) |

## Regras

- Módulos JVM (`common`, `domain`, `analytics`, `ai`, `testing`) não importam Android, Compose, Room, Retrofit nem Dagger além de `javax.inject`. O Konsist cobra.
- `network`, `database` e `datastore` não dependem de `domain` nem de `data`.
- SDK de terceiro novo entra atrás de um contrato JVM aqui, com a implementação num módulo `-<fornecedor>` (padrão `analytics`/`analytics-firebase`, `ai`/`ai-firebase`).
- Todo módulo com Dagger usa o par público + `internal` (ADR-0013).
- Contrato novo em `core` ganha fake em `core:testing`.

## Tarefas comuns

- Bloco SDUI novo → skill `sdui-designer` e [docs/how-to/add-sdui-block.md](../docs/how-to/add-sdui-block.md).
- Evento de analytics → [docs/how-to/add-analytics-event.md](../docs/how-to/add-analytics-event.md).
- Componente visual compartilhado → `designsystem` (sem conhecer modelo de domínio) ou `ui` (conhece `DomainError`, `MovieCardUi`).
- Entity/DAO para uma feature → `core:database` (o `@Database` central precisa listar a entity); o acesso passa por um `LocalDataSource` no `data` da feature.
