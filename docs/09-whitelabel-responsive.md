# 09 · White-label, dynamic theming & responsivo

## White-label: a marca é DADO, não código
O mesmo binário vira qualquer marca. A identidade vive em `BrandTokens`
([`core/designsystem/Brand.kt`](../core/designsystem)): cor primária/secundária, fundo,
superfície, nome, logo. **Nenhuma tela** conhece uma OEM — ela lê `MaterialTheme.colorScheme`
+ `LocalBrandTokens`. Marcas de exemplo têm nomes **neutros** de propósito (Slate/Aurora/Ember/Nord).

- **Padrão por build:** `BuildConfig.DEFAULT_BRAND`. Produção: 1 OEM = 1 flavor que define o
  valor e traz recursos/RRO. É o "multi-flavor/multi-brand build" — sem `if (marca)`.
- **Dynamic theming:** trocar o `BrandTokens` re-tematiza tudo (`AutoDashTheme`) — sem recompilar telas. Como o app é white-label, ele **entrega UMA marca por build** (`BuildConfig.DEFAULT_BRAND`); não há seletor de marca para o usuário final. O poder multi-brand é provado pelos **snapshots** das 4 marcas.
- **RRO** da OEM (docs/06) sobrepõe por cima, também em runtime.

## Responsivo: portrait × landscape
`DashboardScreen` decide o layout por **medida real** (`BoxWithConstraints` + `isWide()`):
- **landscape/wide:** velocidade grande à esquerda, tiles em coluna à direita.
- **portrait:** velocidade em cima, tiles empilhados abaixo.

Head units são heterogêneos (cluster largo, telas centrais em retrato) — responsivo não é opcional.

## Testes (sem CI/CD)
- **Unit (JVM):** `ResponsiveTest` (a função `isWide`) e `BrandTest` (registro/ciclo de marcas).
- **Snapshot multi-brand (Paparazzi):** `DashboardSnapshotTest` renderiza o dashboard em
  **cada marca** (landscape) + **portrait**, na JVM, sem device. Goldens em
  `feature/dashboard/src/test/snapshots/`. `./gradlew :feature:dashboard:verifyPaparazziDebug`
  barra regressão visual entre marcas — exatamente o "snapshot testing for multi-brand UI
  consistency". Regenerar: `recordPaparazziDebug`.
