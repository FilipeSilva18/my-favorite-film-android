---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0013 — Módulos Dagger em par público + interno

## Contexto e problema

Com Dagger puro o `AppComponent` referencia os módulos de cada camada, mas implementações e DTOs devem ser `internal`.

## Opções consideradas

* `object XxxModule` público com `includes` de um `internal interface XxxBindsModule`
* Tudo público

## Decisão

Opção escolhida: Par público + interno, porque o `:app` vê só o agregador e as implementações continuam `internal`.

### Consequências

* Bom: Encapsulamento preservado com Dagger puro
* Ruim: Dois tipos por módulo Dagger

### Confirmação

Konsist: `repository implementations are internal...`, `data sources are internal...`, `DTOs are internal...`.
