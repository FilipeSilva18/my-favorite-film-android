# Record an architecture decision (ADR)

## When to write one

- A choice between alternatives that affects more than one module or that someone will question later.
- Changing or relaxing a Konsist or `moduleGraphAssert` rule.
- Adopting or replacing a structural library (DI, navigation, persistence, network).

No ADR needed: internal details of a class, refactors that keep the rules.

## Steps

1. Copy [`docs/decisions/adr-template.md`](../decisions/adr-template.md) to `docs/decisions/NNNN-title-with-dashes.md`, with the next free number.
2. Fill in: context, considered options (at least two), the decision with its reason, good and bad consequences, **confirmation** (which test or rule enforces the decision).
3. Status `proposed` while under discussion; `accepted` on merge.
4. If it supersedes another ADR: in the old one, change only the status to `superseded by [ADR-NNNN](NNNN-....md)`.
5. Update:
   - the index in `docs/decisions/README.md`;
   - the table in section 2 of `ARCHITECTURE.md` (one summary row);
   - the Konsist or graph rule, if the decision is verifiable;
   - `docs/reference/architecture-rules.md` and the affected `AGENTS.md`.

## Good practices

- One decision per ADR.
- Write the technical reason, not the preference ("enforces X at compile time", not "it looks nicer").
- Record the discarded alternative and why; it is the most useful part six months later.
