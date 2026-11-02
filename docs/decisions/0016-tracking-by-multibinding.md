---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0016 — Tracking por multibinding

## Contexto e problema

Queremos somar ou trocar ferramentas de analytics sem tocar nas features e sem `if (DEBUG)`.

## Opções consideradas

* `Analytics`/`ErrorReporter` (JVM) com destinos em `Set<Tracker>` via `@IntoSet`/`@ElementsIntoSet`
* SDK do Firebase direto nas features

## Decisão

Opção escolhida: Contratos em `core:analytics` e destinos registrados por multibinding, despachados numa fila serializada, porque Open/Closed: destino novo é um binding novo; features dependem só da abstração.

### Consequências

* Bom: Logcat só no debug via source set
* Bom: `TestAnalytics` nos testes
* Bom: Destino lento não trava a main thread
* Ruim: Mais indireção que chamar o SDK

### Confirmação

Konsist: `only analytics-firebase and ai-firebase know the Firebase SDK`; `CompositeAnalyticsTest`.
