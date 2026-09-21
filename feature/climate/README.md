# `feature/climate/` — HVAC por ZONA (Car API na prática)

**Micro:** `ClimateViewModel` lê `car.climate()` (StateFlow) e manda deltas; `ClimateScreen`
mostra duas zonas (motorista/passageiro) com +/- de temperatura.

**Macro — a Car API "por zona" (ver [`docs/02`](../../docs/02-car-api-and-vhal.md)):**
- `HVAC_TEMPERATURE_SET` é uma propriedade **por assento** (`area`). Cada zona usa um
  `VehicleAreaSeat` (SEAT_ROW_1_LEFT/RIGHT) — mudar o motorista não mexe no passageiro.
- **Escrever** (o +/-) chama `setProperty`, que exige a permissão **`CONTROL_CAR_CLIMATE`**
  (`signature|privileged`) — ver [`docs/04`](../../docs/04-permissions-privileged.md). No fake/emulador
  funciona sem isso; num carro real depende do acordo com a OEM.
- Fluxo completo: botão → `ViewModel.delta` → `CarRepository.setSeatTemp(seat, temp)` →
  `climate()` reemite → UI. A UI **não** conhece `CarPropertyManager` (Clean Arch).

Responsivo (BoxWithConstraints) e white-label (cor/tipografia dos tokens).

**UX (padrões automotivos):** POWER/A-C são `Switch` Material3 com **status On/Off** + ícone
(feedback claro do estado); +/− de temperatura e FAN usam `FilledIconButton` (alvo ≥64dp, ripple,
`contentDescription` p/ TalkBack); **POWER off desabilita e apaga** zonas/A-C/FAN (gating).

**Testes:** `ClimateViewModelTest` (unit, coroutines-test) cobre `togglePower/toggleAc/fan/delta`,
os clamps (fan 0..6, temp 16..28) e a independência por zona; `ClimateSnapshotTest` (Paparazzi)
tem clima por marca + `climate_power_off`/`climate_ac_off` (prova visual do gating).
