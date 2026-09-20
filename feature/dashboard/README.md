# `feature/dashboard/` — telemetria em Compose (parked)

**Micro:** `DashboardViewModel` coleta os use cases (`ObserveVehicleSpeed`, gear, energy)
e expõe um `StateFlow` de estado; `DashboardScreen` renderiza velocímetro/marcha/energia.

**Macro:**
- **MVVM + UDF**: a UI só lê estado e não conhece o `CarPropertyManager` (fala com o use case).
- **CarUxRestrictions** (ver [`docs/05`](../../docs/05-ux-restrictions.md)): esta tela é rica
  **parado**; em movimento o sistema manda enxugar. O gancho de restrição entraria aqui.
- Colete com `collectAsStateWithLifecycle` — a tela do carro vive muito tempo; coletar sem
  lifecycle desperdiça energia (head unit sempre ligado).

## i18n / l10n
- **Unidade por região:** `res/values/bools.xml` (métrico) e `res/values-en-rUS|GB/bools.xml`
  (imperial) → o app mostra **km/h** ou **mph** conforme o locale, sem `if` no código.
  Regra espelhada em `core/model/unitSystemForLocale()` (com teste `UnitsTest`).
- **Rótulos:** `res/values/strings.xml` (pt) + `res/values-en/strings.xml` (en). A UI usa
  `stringResource(...)`. Ver [`docs/09`](../../docs/09-whitelabel-responsive.md).
- Provado em device (emulador en-US → 36 mph / English) e por snapshot Paparazzi
  (`english_imperial_slate`). `res/` não tem README (aapt exige só XML) — descrito aqui.
