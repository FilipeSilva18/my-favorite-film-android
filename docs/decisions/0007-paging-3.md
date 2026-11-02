---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0007 — Paging 3 para listas paginadas

## Contexto e problema

A API da TMDB é paginada e a lista tem rolagem infinita, busca e retry.

## Opções consideradas

* Paging 3 com `paging-common` (JVM) no domain
* Paginação manual no ViewModel

## Decisão

Opção escolhida: Paging 3, com `paging-common` no domain, porque é a solução oficial, trata loading/erro/retry por página e integra com Compose.

### Consequências

* Bom: `LoadState` pronto para skeleton, rodapé e retry
* Bom: `asSnapshot` para testes
* Ruim: `PagingData` atravessa o domain (aceito: `paging-common` é JVM puro)

### Confirmação

Testes `MoviesViewModelTest` (asSnapshot) e `MoviesRepositoryImplTest`.
