---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0002 — Domain obrigatório e Kotlin puro

## Contexto e problema

No guia do Google o domain é opcional e depende do data. Queremos regras de negócio testáveis na JVM e independentes de Android, Retrofit e Room.

## Opções consideradas

* Domain obrigatório, módulo JVM, define as interfaces de repositório (inversão de dependência)
* Domain opcional dependendo do data (modelo do guia)
* Sem domain: ViewModel fala com repositório

## Decisão

Opção escolhida: Domain obrigatório, módulo JVM que define as interfaces; o `data` implementa, porque aplica o Dependency Inversion Principle: a política (domain) não conhece o detalhe (data), e os testes rodam em milissegundos.

### Consequências

* Bom: Testes de domínio sem Robolectric
* Bom: Trocar Retrofit/Room não muda o domain
* Ruim: Mapeamento DTO → domínio em todo repositório

### Confirmação

Konsist: `domain, common and the AI contract are pure Kotlin...`; module-graph-assert: `.*:domain -X> :core:(data|network|database|datastore|ui|designsystem|di)`.
