# IA de agentes no repositório

Como o projeto se prepara para ser desenvolvido com agentes de IA de qualquer fornecedor, e o que pode evoluir.

## Princípio

Um agente erra menos quando três coisas existem no repositório:

1. **Contexto curto e sempre carregado:** o que é o projeto, como rodar, o que nunca fazer. → `AGENTS.md`.
2. **Procedimentos sob demanda:** como fazer cada tarefa recorrente do jeito do projeto. → Skills em `.agents/skills/`.
3. **Verificação automática:** o agente descobre sozinho se quebrou uma regra, sem depender de revisão humana. → `assertModuleGraph`, Konsist, testes, `scripts/verify.sh`.

O terceiro item é o mais importante. Documentação que não é verificada envelhece; regra que vira teste guia o agente pelo erro de compilação ou pelo teste vermelho.

## O que já existe

| Camada | Artefato | Padrão aberto |
|---|---|---|
| Contexto | `AGENTS.md` na raiz + em `app/`, `core/`, `feature/`, `build-logic/`, `architecture-test/` | [AGENTS.md](https://agents.md) (Agentic AI Foundation / Linux Foundation) |
| Procedimentos | 7 skills: `feature-builder`, `data-integrator`, `compose-ui`, `sdui-designer`, `test-engineer`, `architecture-guardian`, `docs-keeper` | [Agent Skills](https://agentskills.io) |
| Conhecimento | `docs/` (Diátaxis), ADRs em MADR, `ARCHITECTURE.md` | [MADR](https://adr.github.io/madr/), [Diátaxis](https://diataxis.fr) |
| Guardrails | module-graph-assert, 21 regras Konsist, cobertura obrigatória de use case e ViewModel, regra de strings | — |
| Segurança | `scripts/git-hooks/pre-commit` bloqueia `local.properties`, `google-services.json` e tokens no diff | — |
| Feedback | `scripts/verify.sh`, `scripts/verify-module.sh`, `scripts/check-docs.py` | — |
| Fluxo de PR | Template de PR com a definição de pronto; template de issue com especificação de feature | — |

**Por que esses padrões:** `AGENTS.md` é lido pelo Gemini do Android Studio, Codex, Copilot, Cursor, Gemini CLI, Junie e dezenas de outras ferramentas. `.agents/skills/` é o diretório que o Android Studio (a partir do Quail) e o Codex procuram, e o próprio Google publica skills oficiais de Android nesse formato. Nenhum arquivo é específico de um fornecedor.

## Papéis de agente como skills

Não existe padrão aberto para "subagentes": cada ferramenta tem o seu formato. Por isso os papéis são skills, que qualquer ferramenta compatível carrega quando a descrição bate com a tarefa, e que uma pessoa pode colar como prompt numa ferramenta sem suporte.

| Skill | Responsabilidade | Fronteira |
|---|---|---|
| `feature-builder` | Feature nova de ponta a ponta (módulos, DI, navegação, testes) | Orquestra; delega detalhes às outras skills |
| `data-integrator` | Endpoint da TMDB, DTO, data source, repositório, teste de contrato | Só camada `data` e contrato no `domain` |
| `compose-ui` | Telas e componentes Compose, estados, strings, previews, screenshots | Só `presentation`, `core:ui`, `core:designsystem` |
| `sdui-designer` | Bloco novo do vocabulário Server-Driven UI | `core:common/sdui`, `core:network/sdui`, `core:ui/sdui` |
| `test-engineer` | Testes em todas as camadas, fakes, cobertura exigida | Não muda código de produção sem pedir |
| `architecture-guardian` | Revisão de mudanças contra ADRs e regras; evolução das regras | Lê e aponta; muda regra só com ADR |
| `docs-keeper` | Mantém `AGENTS.md`, `docs/`, ADRs e `ARCHITECTURE.md` coerentes com o código | Só documentação |

## Roadmap: onde investir

Ideias ordenadas por relação entre valor e esforço.

### 1. Geradores determinísticos (scaffolding)

Script `scripts/new-feature.sh <nome> [--with-api]` que cria os módulos, `build.gradle.kts`, pacotes, módulos Dagger, rota, `include` e testes vazios a partir de templates. A skill `feature-builder` chama o script em vez de escrever o boilerplate.
**Por quê:** o agente gasta contexto e erra em código repetitivo; o gerador é determinístico e testável. O agente fica com a parte que exige julgamento (regra de negócio, UI).

### 2. Revisão de PR por agente no CI

Job que roda em todo PR, envia o diff e a skill `architecture-guardian` para um modelo (o fornecedor fica numa variável de ambiente) e comenta violações de ADR que os testes não pegam: nomes ruins, use case com duas responsabilidades, string fora do padrão de tom.
**Requisitos:** secret com a chave da API, limite de custo por PR, comentário só como sugestão (nunca bloqueia o merge sozinho).

### 3. Avaliação das skills (evals)

Conjunto de tarefas de referência ("adicione o endpoint `movie/{id}/reviews`", "crie a feature `watchlist` com `api`") com critério objetivo de sucesso: `./scripts/verify.sh` passa e o diff respeita as regras. Rodar periodicamente com mais de um modelo mostra quando uma skill ou um `AGENTS.md` ficou ambíguo.

### 4. Servidor MCP do projeto

Servidor [MCP](https://modelcontextprotocol.io) (outro padrão da Agentic AI Foundation) com ferramentas próprias do repositório: `module_graph` (grafo atual em JSON), `explain_rule` (de uma regra Konsist para a ADR correspondente), `run_verify` (resultado estruturado em vez de log). Qualquer cliente MCP passa a consultar o projeto sem ler arquivos inteiros.

### 5. Especificação antes do código

Template de issue com especificação estruturada: objetivo, telas, estados, endpoints, eventos de analytics, critérios de aceite. Um agente transforma a issue em plano e PR; a especificação vira o contrato de revisão.

### 6. Documentação verificada

Expandir `scripts/check-docs.py` para comparar fatos da documentação com o código: lista de módulos do `settings.gradle.kts`, lista de features do Konsist, plugins registrados no `build-logic`. Documentação divergente quebra o CI.

### 7. Telemetria de qualidade dos agentes

Marcar PRs feitos com agente (label ou trailer de commit) e medir taxa de CI verde na primeira tentativa, número de revisões e regras mais violadas. Os dados mostram qual skill ou regra precisa de ajuste.
