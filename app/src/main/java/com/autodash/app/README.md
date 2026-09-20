# `app/.../app/` — Application e host Activity

**Micro:**
- `AutoDashApplication` — ponto de init do processo. **Mantenha leve** (`onCreate` enxuto):
  head unit tem cold start crítico. Init pesado vai lazy/async (ver [`docs/07`](../../../../../../docs/07-power-and-garage-mode.md)).
- `MainActivity` — hospeda o **dashboard** Compose (`feature/dashboard`). É
  *parked-optimized*: rica quando o carro está parado; enxuta em movimento (o
  `CarUxRestrictions` guia isso — ver [`docs/05`](../../../../../../docs/05-ux-restrictions.md)).

**Macro:** um app automotivo pode ter **Activity** (para experiências parked) **e**
`CarAppService` (para experiências drivable via template). Aqui os dois convivem.
