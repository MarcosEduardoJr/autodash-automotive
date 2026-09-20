# `data/car/` — a ponte com o veículo (CarPropertyManager)

**Micro:** `CarPropertyRepository` implementa `domain.CarRepository` observando
propriedades via `CarPropertyManager` e traduzindo para modelos de `core:model`.

**Macro — é aqui que a teoria da Car API vira código (ver [`docs/02`](../../docs/02-car-api-and-vhal.md)):**
- `registerCallback(cb, PERF_VEHICLE_SPEED, SENSOR_RATE_NORMAL)` → stream de velocidade.
- `PERF_VEHICLE_SPEED` é **CONTINUOUS** (VHAL) → chega rápido; usamos `callbackFlow` +
  `conflate` para não afogar a UI.
- **Sempre** checamos `CarPropertyValue.status` (AVAILABLE/UNAVAILABLE/ERROR): hardware é
  heterogêneo entre marcas — nem todo carro tem toda propriedade.
- **`awaitClose { unregisterCallback }`** → sem isso, callback vivo em carro (ligado horas)
  = **leak**. Este é o bug automotivo clássico.

**Build:** `android.car` é da **plataforma** → entra como `compileOnly` (existe no head
unit, não empacotamos no APK). Ver `build.gradle.kts`.

> Onde entraria o power: um listener de `CarPowerManager` (SHUTDOWN_PREPARE) para
> persistir estado antes de suspender viria aqui (ver [`docs/07`](../../docs/07-power-and-garage-mode.md)).
