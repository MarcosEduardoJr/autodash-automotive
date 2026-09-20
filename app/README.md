# `app/` — o módulo de aplicação Android Automotive

**Micro:** monta tudo (depende dos `feature/`, `data/`, `core/`), define o `AndroidManifest`,
os **flavors de marca** e o *host* Activity do dashboard.

**Macro:** é aqui que um app vira **automotivo** de verdade. Três coisas o distinguem de
um app de celular:
1. `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`
2. o **`automotive_app_desc.xml`** que declara a **categoria** (ver `src/main/README.md`)
3. permissões `android.car.permission.*` (ver [`docs/04`](../docs/04-permissions-privileged.md))

## Flavors (multi-brand)
```
productFlavors { create("volvo"){…}; create("scania"){…} }   // dimension "brand"
```
Cada marca empacota recursos próprios; a identidade visual vem de tema/RRO
(ver [`docs/06`](../docs/06-multi-brand-rro.md)), não de `if (marca)` nas telas.

## Duas superfícies
- **Dashboard** (parado, Compose) → `feature/dashboard`.
- **POI** (dirigindo, templates) → `feature/carapp` (um `CarAppService`).

> Um app automotivo pode ter **ambos**: uma Activity (parked-optimized) e um
> `CarAppService` (drivable). Eles convivem no mesmo módulo `app/`.
