# Registrar uma decisão de arquitetura (ADR)

## Quando escrever

- Escolha entre alternativas que afeta mais de um módulo ou que alguém vai questionar depois.
- Mudança ou relaxamento de uma regra do Konsist ou do `moduleGraphAssert`.
- Adoção ou troca de biblioteca estrutural (DI, navegação, persistência, rede).

Não precisa de ADR: detalhes internos de uma classe, refatorações que mantêm as regras.

## Passos

1. Copie [`docs/decisions/adr-template.md`](../decisions/adr-template.md) para `docs/decisions/NNNN-titulo-em-ingles.md`, com o próximo número livre.
2. Preencha em português: contexto, opções consideradas (pelo menos duas), decisão com o motivo, consequências boas e ruins, **confirmação** (qual teste ou regra garante a decisão).
3. Status `proposed` enquanto estiver em discussão; `accepted` no merge.
4. Se substitui outra ADR: na antiga, mude só o status para `superseded by [ADR-NNNN](NNNN-....md)`.
5. Atualize:
   - o índice em `docs/decisions/README.md`;
   - a tabela da seção 2 do `ARCHITECTURE.md` (uma linha resumida);
   - a regra Konsist ou do grafo, se a decisão é verificável;
   - `docs/reference/architecture-rules.md` e o `AGENTS.md` afetado.

## Boas práticas

- Uma decisão por ADR.
- Escreva o motivo técnico, não a preferência ("garante X em compilação", não "é mais bonito").
- Registre a alternativa descartada e por quê; é a parte mais útil seis meses depois.
