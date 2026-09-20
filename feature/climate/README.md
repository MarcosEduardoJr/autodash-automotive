# `feature/climate/` — HVAC por ZONA (Car API na prática)

**Micro:** `ClimateViewModel` lê `car.climate()` (StateFlow) e manda deltas; `ClimateScreen`
mostra duas zonas (motorista/passageiro) com +/- de temperatura.

**Macro — é a Car API "por zona" que a vaga valoriza (ver [`docs/02`](../../docs/02-car-api-and-vhal.md)):**
- `HVAC_TEMPERATURE_SET` é uma propriedade **por assento** (`area`). Cada zona usa um
  `VehicleAreaSeat` (SEAT_ROW_1_LEFT/RIGHT) — mudar o motorista não mexe no passageiro.
- **Escrever** (o +/-) chama `setProperty`, que exige a permissão **`CONTROL_CAR_CLIMATE`**
  (`signature|privileged`) — ver [`docs/04`](../../docs/04-permissions-privileged.md). No fake/emulador
  funciona sem isso; num carro real depende do acordo com a OEM.
- Fluxo completo: botão → `ViewModel.delta` → `CarRepository.setSeatTemp(seat, temp)` →
  `climate()` reemite → UI. A UI **não** conhece `CarPropertyManager` (Clean Arch).

Responsivo (BoxWithConstraints) e white-label (cor/tipografia dos tokens). Snapshot em
`ClimateSnapshotTest`.
