---
status: superseded by [ADR-0017](0017-feature-api-module.md)
date: 2026-11-01
decision-makers: Filipe
---

# 0006 — Favoritos em core:domain e core:data

## Contexto e problema

`movies` e `moviedetail` precisavam favoritar e observar favoritos sem depender uma da outra.

## Opções consideradas

* Favoritos em `core:domain` + `core:data`
* Feature `favorites` com módulo público

## Decisão

Opção escolhida: Favoritos em `core:domain` + `core:data`, porque era a forma mais simples de compartilhar a regra respeitando a ADR-0005.

### Consequências

* Bom: Simples
* Ruim: `core` passou a ter regra de uma feature específica
* Ruim: A tela de favoritos ficava separada da própria regra

### Confirmação

Substituída: o código atual segue a ADR-0017.
