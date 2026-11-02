---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0015 — Componentes de lista compartilhados em core:ui

## Contexto e problema

Lista principal e favoritos exibem o mesmo card e a mesma grade.

## Opções consideradas

* `MovieCardUi`, `MovieCard`, `MovieGrid` e skeletons em `core:ui/movie`
* Card por feature

## Decisão

Opção escolhida: Componentes em `core:ui/movie`, porque pela ADR-0005, o que duas features usam sobe para o core.

### Consequências

* Bom: Visual consistente e screenshots únicos
* Ruim: Mudança no card afeta duas telas (coberto por screenshots)

### Confirmação

Screenshots de `MovieGrid` e das telas que o usam.
