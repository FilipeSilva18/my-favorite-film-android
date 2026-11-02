---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0009 — Dagger 2 puro, sem Hilt

## Contexto e problema

Queremos o grafo de dependências explícito e didático, com escopos e subcomponentes visíveis.

## Opções consideradas

* Dagger 2 puro com KSP
* Hilt
* Koin

## Decisão

Opção escolhida: Dagger 2 puro: `AppComponent` raiz + `ViewModelComponent` por ViewModel com `SavedStateHandle`, porque controle total do grafo, verificado em compilação, sem geração agregada escondida.

### Consequências

* Bom: Erros de grafo em tempo de compilação
* Bom: Grafo legível em dois arquivos do `:app`
* Ruim: `api(...)` extras para o KSP do `:app` enxergar tipos (ARCHITECTURE.md 12.8)
* Ruim: Mais código que Hilt

### Confirmação

`DependencyGraphTest` instancia todos os ViewModels pelo grafo real.
