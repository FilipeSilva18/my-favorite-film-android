---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0004 — ViewModel só conversa com use cases

## Contexto e problema

O guia permite que o ViewModel acesse o repositório quando não há lógica. Exceções tornam a revisão subjetiva.

## Opções consideradas

* ViewModel só com use cases, sempre
* Use case só quando houver lógica

## Decisão

Opção escolhida: ViewModel recebe apenas use cases no construtor, porque regra única, sem exceções, fácil de revisar e de verificar automaticamente.

### Consequências

* Bom: Revisão objetiva
* Bom: Use cases reutilizáveis entre telas
* Ruim: Use cases finos que só delegam ao repositório

### Confirmação

Konsist: `ViewModel depends on use cases, never on repositories (ADR-04)`.
