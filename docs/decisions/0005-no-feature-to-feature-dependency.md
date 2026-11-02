---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0005 — Feature nunca depende de outra feature

## Contexto e problema

Dependências laterais entre features criam ciclos e acoplamento que impedem evoluir uma feature isoladamente.

## Opções consideradas

* Proibir dependência entre features; o compartilhado sobe para `core`
* Permitir dependências pontuais

## Decisão

Opção escolhida: Feature não depende de feature; o que é compartilhado sobe para `core` (com a exceção controlada da ADR-0017), porque evita acoplamento lateral e ciclos.

### Consequências

* Bom: Features evoluem e são removidas isoladamente
* Ruim: `core` pode crescer demais se tudo subir sem critério (ver ADR-0017)

### Confirmação

module-graph-assert: regras `:feature:x:.* -X> :feature:y:.*`; Konsist: `a feature only reaches another feature through its public api module (ADR-05)`.
