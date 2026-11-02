---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0017 — Módulo api para features consumidas por outras

## Contexto e problema

Favoritos no core (ADR-0006) misturava regra de feature com regra transversal. `movies` e `moviedetail` ainda precisam favoritar sem depender da feature inteira.

## Opções consideradas

* `:feature:favorites:api` (JVM) com modelo e interfaces finas + `domain`/`data`/`presentation`
* Manter no core
* Um único `:impl` com camadas em pacotes
* `api` em todas as features

## Decisão

Opção escolhida: Módulo `api` só para features consumidas por outras; a feature mantém os três módulos de camada, porque Dependency Inversion e Interface Segregation entre módulos; o `api` resolve visibilidade e as camadas resolvem organização interna.

### Consequências

* Bom: Core só com regra transversal
* Bom: Compile avoidance: consumidores não recompilam quando a implementação muda
* Bom: Fakes em `testFixtures`
* Ruim: `*UseCaseImpl` públicos, porque o binding fica no `data` (protegidos pelo grafo)

### Confirmação

module-graph-assert: `:feature:(movies|moviedetail|settings):.* -X> :feature:favorites:(domain|data|presentation)`; Konsist: importação de outra feature só por `.api.`.
