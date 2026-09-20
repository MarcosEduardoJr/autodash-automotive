# `feature/dashboard/` — telemetria em Compose (parked)

**Micro:** `DashboardViewModel` coleta os use cases (`ObserveVehicleSpeed`, gear, energy)
e expõe um `StateFlow` de estado; `DashboardScreen` renderiza velocímetro/marcha/energia.

**Macro:**
- **MVVM + UDF**: a UI só lê estado e não conhece o `CarPropertyManager` (fala com o use case).
- **CarUxRestrictions** (ver [`docs/05`](../../docs/05-ux-restrictions.md)): esta tela é rica
  **parado**; em movimento o sistema manda enxugar. O gancho de restrição entraria aqui.
- Colete com `collectAsStateWithLifecycle` — a tela do carro vive muito tempo; coletar sem
  lifecycle desperdiça energia (head unit sempre ligado).
