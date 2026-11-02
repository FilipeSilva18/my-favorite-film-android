---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0012 — Módulo core:di para ViewModels

## Contexto e problema

Sem Hilt, as `presentation` precisam obter ViewModels do Dagger sem conhecer o `:app`.

## Opções consideradas

* `core:di` com `ViewModelKey`, `DaggerViewModelFactory` e `daggerViewModel()`
* Factory por feature

## Decisão

Opção escolhida: Módulo `core:di` com a infraestrutura de ViewModel, porque é o equivalente ao que o Hilt daria pronto, sem duplicação.

### Consequências

* Bom: Uma linha por tela: `viewModel: XViewModel = daggerViewModel()`
* Ruim: Mais um módulo core

### Confirmação

`DependencyGraphTest`.
