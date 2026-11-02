# Comandos

Todos rodam na raiz do repositório, com JDK 21.

## Scripts

| Script | O que faz |
|---|---|
| `./scripts/verify.sh` | Grafo de módulos, Konsist, testes JVM, screenshots e lint, sem parar no primeiro erro. É o que o job `checks` do CI roda |
| `./scripts/verify.sh --quick` | Só grafo de módulos e Konsist (segundos) |
| `./scripts/verify-module.sh <módulo>` | Testes JVM de um módulo + Konsist + grafo. Ex.: `:feature:movies:presentation` |
| `./scripts/record-screenshots.sh [módulo]` | Regrava as referências Roborazzi (todas ou de um módulo) e lista os PNGs alterados |
| `./scripts/install-git-hooks.sh` | Instala o `pre-commit` que bloqueia segredos |
| `./scripts/check-docs.py` | Valida links relativos dos `.md` e o frontmatter das skills |

## Gradle

| Comando | O que faz |
|---|---|
| `./gradlew assertModuleGraph` | Regras do grafo de módulos (`moduleGraphAssert` no build raiz) |
| `./gradlew :architecture-test:unitTest` | Regras Konsist |
| `./gradlew unitTest` | Todos os testes JVM (unit, integração, Compose com Robolectric, DI, Konsist) |
| `./gradlew :<módulo>:unitTest` | Testes JVM de um módulo |
| `./gradlew verifyRoborazziDebug` | Falha se um screenshot mudar |
| `./gradlew recordRoborazziDebug` | Regrava screenshots |
| `./gradlew compareRoborazziDebug` | Gera imagens de diff para revisar |
| `./gradlew lintDebug` | Android Lint |
| `./gradlew koverHtmlReport` | Cobertura agregada em `build/reports/kover/html` |
| `./gradlew connectedDebugAndroidTest` | Testes instrumentados (emulador ou aparelho conectado) |
| `./gradlew :app:assembleDebug` | APK de debug |

## Configuração local

| Arquivo | Conteúdo | Versionado |
|---|---|---|
| `local.properties` | `sdk.dir`, `TMDB_ACCESS_TOKEN=<token de leitura da TMDB>` | Não |
| `app/google-services.json` | Configuração do Firebase (opcional; sem ele analytics e IA ficam desligados) | Não |

O token de debug do App Check aparece no Logcat (filtro `DebugAppCheckProvider`) e é cadastrado no console do Firebase. Ele muda quando o app é reinstalado ou os dados são apagados. Nunca é versionado.

## CI (`.github/workflows/ci.yml`)

| Job | Passos |
|---|---|
| `checks` | Validação dos docs → `assertModuleGraph` → Konsist → `unitTest` → `verifyRoborazziDebug` → `lintDebug` → Kover |
| `instrumented` | Emulador API 35 → `connectedDebugAndroidTest` |

Secrets: `TMDB_ACCESS_TOKEN`, `GOOGLE_SERVICES_JSON`.
