# 📚 docs/ — o curso de Android Automotive

Aqui mora a parte "macro": um arquivo por conceito. Os README dos módulos apontam
para cá quando o conceito aparece no código.

| # | Arquivo | Conceito |
|---|---------|----------|
| 01 | [aaos-vs-android-auto](01-aaos-vs-android-auto.md) | AAOS × Android Auto, AOSP/GAS, usuário headless |
| 02 | [car-api-and-vhal](02-car-api-and-vhal.md) | `Car`, `CarPropertyManager`, propriedades, zonas, VHAL |
| 03 | [car-app-library](03-car-app-library.md) | Templates, host, `CarAppService`/`Screen`, restrições |
| 04 | [permissions-privileged](04-permissions-privileged.md) | `signature\|privileged`, allowlist, `SecurityException` |
| 05 | [ux-restrictions](05-ux-restrictions.md) | `CarUxRestrictions`, driving state, distração |
| 06 | [multi-brand-rro](06-multi-brand-rro.md) | car-ui-lib, Runtime Resource Overlay, snapshot |
| 07 | [power-and-garage-mode](07-power-and-garage-mode.md) | `CarPowerManager`, garage mode, always-on |
| 08 | [testing-and-distribution](08-testing-and-distribution.md) | emulador, fakes, Play automotivo |

> Ordem sugerida: 01 → 02 → 03 → 04 → 05 → 06 → 07 → 08. Mas cada README de módulo
> te leva ao doc certo na hora certa.
