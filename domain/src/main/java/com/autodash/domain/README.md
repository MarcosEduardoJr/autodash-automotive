# `domain/.../domain/` — contrato + use cases

- `CarRepository.kt` — a **interface** que abstrai o veículo. O domínio depende disto,
  não do `CarPropertyManager`. Implementada em `data/car`.
- `ObserveVehicleSpeed.kt` — use case: expõe a velocidade como `Flow<VehicleSpeed>`.
  A UI consome o use case, não o SDK do carro.

Isto é o **D** (Dependency Inversion) do SOLID aplicado a AAOS.
