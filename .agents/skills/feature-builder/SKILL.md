---
name: feature-builder
description: Creates a new feature module set (domain, data, presentation, optional api) in the MyFavoriteFilms Android app, wiring Gradle, Dagger, navigation, analytics and tests following the project's Clean Architecture. Use when asked to add a new feature, screen with its own data, tab, or a feature that other features consume.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Feature builder

Você cria features de ponta a ponta seguindo o padrão do projeto. Documentação em português; código em inglês.

## Entradas

- Nome da feature (minúsculo, sem hífen: `watchlist`).
- O que o usuário faz na tela e de onde vêm os dados (endpoint da TMDB, Room, regras do `core:domain`).
- Se outra feature vai consumir algo dela.

Se faltar uma dessas, pergunte antes de criar módulos.

## Passos

1. **Formato:** decida os módulos pela tabela da seção 0 de [add-feature.md](../../../docs/how-to/add-feature.md). Feature consumida por outras ganha `api` (ADR-0017); feature só de UI sobre o core fica só com `presentation`.
2. **Referência:** abra a feature mais parecida e copie a estrutura. `movies` é a referência completa; `favorites`, a de `api`; `settings`, a de só presentation.
3. **Módulos e Gradle:** `build.gradle.kts` com os plugins `movies.*`, `include` em `settings.gradle.kts`, `api(projects.feature.<nome>.domain)` em `data` e `presentation`.
4. **Regras de grafo:** acrescente a feature às regras `-X>` do `moduleGraphAssert` no build raiz e a `FEATURES` em `architecture-test/.../KonsistScopes.kt`.
5. **domain:** modelos, interface de repositório, use cases.
6. **data:** delegue o detalhe à skill `data-integrator` ([add-endpoint.md](../../../docs/how-to/add-endpoint.md)).
7. **presentation:** delegue o detalhe à skill `compose-ui`.
8. **app:** dependências, `<Nome>DataModule` no `AppComponent`, `<Nome>PresentationModule` no `ViewModelComponent`, rota no `MoviesNavHost`, aba em `TopLevelDestination` se for o caso, ViewModel no `DependencyGraphTest`.
9. **Testes:** delegue à skill `test-engineer`. Mínimo: use cases, ViewModel, repositório, `ContentTest`, `ScreenshotTest`.
10. **Docs:** delegue à skill `docs-keeper`.

## Regras

- Nenhuma dependência de outra feature, exceto o módulo `:api` dela.
- Implementações `internal`; módulos Dagger em par público + `internal`.
- Repositório depende de data sources.
- ViewModel recebe só use cases (mais `Analytics` e `SavedStateHandle`).
- Nenhum texto fixo em Composable; nenhum comentário explicativo no código.

## Pronto quando

- [ ] `./scripts/verify.sh` passa.
- [ ] A feature aparece em `ARCHITECTURE.md`, `feature/AGENTS.md` e `docs/reference/modules.md`.
- [ ] Nenhum arquivo de segredo no diff.

## Verificar

```bash
./scripts/verify.sh --quick
./scripts/verify-module.sh :feature:<nome>:presentation
./scripts/verify.sh
```
