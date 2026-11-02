---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0010 — Filmes online e favoritos offline

## Contexto e problema

O escopo da v1 não exige cache de catálogo, mas favoritos precisam sobreviver sem rede.

## Opções consideradas

* Filmes online, favoritos em Room
* Offline-first completo desde o início

## Decisão

Opção escolhida: Filmes direto da TMDB, sem cache; favoritos persistidos em Room, porque entrega o essencial sem a complexidade de sincronização.

### Consequências

* Bom: Menos código na v1
* Ruim: Sem rede, a lista não carrega

### Confirmação

O cache futuro entra com `RemoteMediator` sem mudar `MoviesRepository` (ARCHITECTURE.md seção 16).
