# `feature/` — as duas superfícies do app

**Macro:** um app automotivo costuma ter experiências **parked** (ricas, com Activity/Compose)
e **drivable** (templates seguros, via Car App Library). Aqui elas ficam separadas por módulo.

- [`dashboard/`](dashboard) — telemetria em Compose (parado). Consome os use cases.
- [`carapp/`](carapp) — `CarAppService` + `Screen` de POI (dirigindo). O host renderiza.

Ambos dependem de `domain` (nunca de `data/car` direto).
