# AGENTS.md

Instruções para agentes de IA e para pessoas que trabalham neste repositório. Formato aberto [AGENTS.md](https://agents.md): vale para qualquer assistente. O `AGENTS.md` mais próximo do arquivo editado complementa este; um pedido explícito do usuário tem prioridade sobre os dois.

## Projeto

**MyFavoriteFilms** (`com.faas.myfavoritefilms`): app Android de filmes sobre a API da TMDB.

- Kotlin 2.4, Jetpack Compose (Material 3, single Activity), Navigation Compose type-safe.
- Clean Architecture sobre o guia oficial do Google: `presentation` → `domain` ← `data`.
- Monorepo com 27 módulos Gradle: `:app`, 14 em `core/`, 11 em `feature/`, `:architecture-test`.
- DI com **Dagger 2 puro** (sem Hilt), KSP. Paging 3, Room, DataStore, Retrofit 3, Coil 3.
- Firebase: Analytics, Crashlytics, AI Logic (Gemini) e App Check, sempre atrás de contratos em `core`.

## Mapa

| Caminho | O que é | Instruções |
|---|---|---|
| `app/` | Composition root: `AppComponent`, `ViewModelComponent`, `MoviesNavHost`, bottom bar | [app/AGENTS.md](app/AGENTS.md) |
| `core/` | Infra e regras transversais (rede, banco, DataStore, design system, SDUI, analytics, IA) | [core/AGENTS.md](core/AGENTS.md) |
| `feature/` | `movies`, `moviedetail`, `favorites` (com `api`), `settings` | [feature/AGENTS.md](feature/AGENTS.md) |
| `build-logic/` | Convention plugins `movies.*` | [build-logic/AGENTS.md](build-logic/AGENTS.md) |
| `architecture-test/` | Regras de arquitetura executáveis (Konsist) | [architecture-test/AGENTS.md](architecture-test/AGENTS.md) |
| `docs/` | Documentação (Diátaxis) e ADRs (MADR) | [docs/README.md](docs/README.md) |
| `.agents/skills/` | Skills de agente (padrão aberto Agent Skills) | [docs/how-to/use-ai-agents.md](docs/how-to/use-ai-agents.md) |
| `scripts/` | Verificação local e git hooks | [docs/reference/commands.md](docs/reference/commands.md) |
| `ARCHITECTURE.md` | Documento de arquitetura completo, com trechos de código | — |

## Comandos

```bash
./scripts/verify.sh                         # tudo que o CI de JVM roda: grafo, Konsist, testes, screenshots, lint
./scripts/verify-module.sh :feature:movies:presentation
./gradlew assertModuleGraph                 # regras do grafo de módulos
./gradlew :architecture-test:unitTest       # regras Konsist
./gradlew unitTest                          # todos os testes JVM (unit, integração, Compose, DI)
./scripts/record-screenshots.sh             # regrava referências Roborazzi (revise e versione os PNGs)
./gradlew :app:assembleDebug
```

Use JDK 21. Sem `TMDB_ACCESS_TOKEN` em `local.properties` o app compila, mas a TMDB responde 401.

## Regras inegociáveis

1. **Segredos nunca entram no Git.** O token da TMDB fica só em `local.properties` (ignorado) ou no secret `TMDB_ACCESS_TOKEN` do CI. `app/google-services.json` é ignorado e vem do secret `GOOGLE_SERVICES_JSON`. O token de debug do App Check nunca é versionado. Rode `./scripts/install-git-hooks.sh` uma vez.
2. **Camadas:** `domain` é Kotlin puro (JVM) e define as interfaces; `data` implementa; `presentation` só fala com use cases. ViewModel nunca recebe repositório.
3. **Feature não depende de feature.** A única exceção é o módulo `:api` de uma feature consumida por outras (hoje `:feature:favorites:api`). O que duas features usam sobe para `core`.
4. **Repositório depende de data source**, nunca de DAO ou de API Retrofit (`<Tipo>LocalDataSource`/`<Tipo>RemoteDataSource` em `data/datasource`, `internal`).
5. **Implementações são `internal`.** Módulos Dagger em par: `object XxxModule` público que inclui o `internal interface XxxBindsModule`.
6. **Firebase só em `core:analytics-firebase` e `core:ai-firebase`.** Features usam `Analytics`, `ErrorReporter` e `AiJsonGenerator`.
7. **Nenhum texto de UI fixo no código.** Tudo em `strings.xml` (`values/` pt-BR e `values-en/`), inclusive `contentDescription`.
8. **Código sem comentários explicativos.** O porquê vai para `docs/decisions/` ou `ARCHITECTURE.md`.
9. **Código, testes e CI em inglês; documentação em português.** Testes JVM com nomes entre crases; instrumentados em camelCase.
10. **Fakes, nunca mocks.** Fakes compartilhados ficam em `core:testing` ou em `testFixtures`.

As regras 2 a 7 são verificadas por `assertModuleGraph` e pelo Konsist. Se uma delas falhar, corrija o código; não afrouxe a regra sem uma ADR nova.

## Definição de pronto

- [ ] `./scripts/verify.sh` passa.
- [ ] Todo `UseCase`, `UseCaseImpl` e `ViewModel` novo tem `<Nome>Test` (o Konsist cobra).
- [ ] Tela nova tem `<Tela>ContentTest` e `<Tela>ScreenshotTest`; PNGs regravados e revisados.
- [ ] ViewModel novo registrado no `DependencyGraphTest` do `:app`.
- [ ] Strings novas em `values/` e `values-en/`.
- [ ] Decisão nova ou alterada registrada em `docs/decisions/` e refletida no `ARCHITECTURE.md`.
- [ ] Nenhum segredo, `local.properties` ou `google-services.json` no diff.

## Como trabalhar aqui

- Antes de criar UI, procure em `core:ui` e `core:designsystem` (`MovieCard`, `MovieGrid`, `LoadingContent`, `ErrorContent`, `EmptyContent`, `SearchField`, `sduiItems`).
- Copie o padrão de uma feature existente em vez de inventar um novo. `movies` é a referência completa; `favorites` é a referência de módulo `api`.
- Tarefas recorrentes têm skills em `.agents/skills/`: `feature-builder`, `data-integrator`, `compose-ui`, `sdui-designer`, `test-engineer`, `architecture-guardian`, `docs-keeper`.
- Quando o código e a documentação divergirem, o código é a fonte da verdade; atualize a documentação no mesmo PR.
