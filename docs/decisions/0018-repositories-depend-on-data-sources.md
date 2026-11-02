---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0018 — Repositórios dependem de data sources

## Contexto e problema

Repositórios recebiam DAO ou API Retrofit direto, misturando acesso a fonte com orquestração.

## Opções consideradas

* Data sources `internal` em `data/datasource` (`<Tipo>LocalDataSource`, `<Tipo>RemoteDataSource`)
* DAO/API direto no repositório

## Decisão

Opção escolhida: Repositório depende de data sources; cada data source fala com uma única fonte e devolve modelos da camada de dados, porque formato do guia de data layer; repositório focado em política (mapeamento, cache, erros).

### Consequências

* Bom: Ponto de extensão para cache offline
* Bom: Consistência entre repositórios
* Ruim: Uma classe a mais por fonte

### Confirmação

Konsist: `repositories talk to data sources, never to a DAO or a Retrofit API directly`, `data sources are internal and live in data datasource`.
