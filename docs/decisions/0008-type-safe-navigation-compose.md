---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0008 — Navigation Compose com rotas type-safe

## Contexto e problema

As features precisam ser navegáveis sem conhecer umas às outras.

## Opções consideradas

* Navigation Compose com rotas `@Serializable`; só o `:app` conhece todas
* Navigation 3
* Rotas em string

## Decisão

Opção escolhida: Navigation Compose type-safe; cada feature expõe extensões de `NavGraphBuilder` e `NavController`, porque recomendação atual do Google e argumentos verificados em compilação.

### Consequências

* Bom: Features desacopladas: o `:app` liga os callbacks
* Ruim: Migração futura para Navigation 3 toca os pacotes `navigation/`

### Confirmação

`MoviesNavHost` no `:app`; rotas em `presentation/navigation/`.
