---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0001 — Camadas do guia de arquitetura do Google

## Contexto e problema

O app precisa de uma divisão de responsabilidades conhecida pelo mercado e pelos agentes, com fluxo de dados unidirecional e estado observável.

## Opções consideradas

* Camadas do guia oficial (UI, Domain, Data) com os nomes `presentation`, `domain` e `data`
* MVP/MVC com Activities e Fragments
* Arquitetura própria sem referência externa

## Decisão

Opção escolhida: Camadas do guia oficial com os nomes `presentation`, `domain` e `data`, porque alinha o projeto à documentação do Google (UDF, `StateFlow`, repositórios, use cases), o que facilita onboarding e revisão.

### Consequências

* Bom: Vocabulário comum com a documentação oficial e com o Now in Android
* Bom: UDF: estado desce, eventos sobem
* Ruim: Mais arquivos por funcionalidade do que um MVVM sem camadas

### Confirmação

Konsist: `ViewModel subclasses end with ViewModel and live in presentation`, `presentation never imports the data layer`.
