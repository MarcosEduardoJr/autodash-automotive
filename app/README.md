# `app/` — o módulo de aplicação Android Automotive

**Micro:** monta tudo (depende dos `feature/`, `data/`, `core/`), define o `AndroidManifest`,
a **marca padrão** (via `BuildConfig.DEFAULT_BRAND`) e o *host* Activity do dashboard.

**Macro:** é aqui que um app vira **automotivo** de verdade:
1. `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`
2. o **`automotive_app_desc.xml`** que declara a **categoria** (ver `src/main/README.md`)
3. permissões `android.car.permission.*` (ver [`docs/04`](../docs/04-permissions-privileged.md))

## White-label (design system multi-brand)
O app **não é** de nenhuma OEM. A identidade é um `BrandTokens` do
[`core/designsystem`](../core/designsystem) (marcas neutras: Slate/Aurora/Ember/Nord).
- **Padrão do build:** `BuildConfig.DEFAULT_BRAND` (aqui, `slate`). Em produção, cada OEM =
  um **flavor** que muda esse valor e fornece seus recursos/RRO — sem tocar nas telas.
- **Uma marca por build** (white-label): sem seletor para o usuário. `AutoDashTheme` re-tematiza a partir do `BrandTokens` — o poder multi-brand é provado por snapshot (docs/06).

## Responsivo (portrait × landscape)
`DashboardScreen` usa `BoxWithConstraints` + `isWide()` para adaptar: **landscape** = velocidade
à esquerda, tiles à direita; **portrait** = empilhado. Head units existem nas duas orientações
(cluster largo, telas centrais retrato). Provado por **snapshot testing** (Paparazzi) em
`feature/dashboard` — ver [`docs/09`](../docs/09-whitelabel-responsive.md).

## Duas superfícies
- **Dashboard** (parado, Compose) → `feature/dashboard`.
- **POI** (dirigindo, templates) → `feature/carapp` (um `CarAppService`).
