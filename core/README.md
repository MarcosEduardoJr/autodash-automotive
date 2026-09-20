# `core/` — as fundações compartilhadas

**Macro:** modularizar em `core/*` (folhas do grafo) é o que permite **build por marca**
rápido e **camadas testáveis**. `feature` e `data` dependem de `core`, nunca o contrário.

- [`model/`](model) — os tipos de domínio (VehicleSpeed, Gear…). **Zero Android.**
- [`common/`](common) — utilidades (dispatchers injetáveis, Result).
- [`designsystem/`](designsystem) — tema/tokens **temáveis por marca** (RRO-friendly).

> Regra de ouro: `core:model` e `core:common` não importam Android. `designsystem`
> importa Compose (é UI), mas **não** conhece o carro.
