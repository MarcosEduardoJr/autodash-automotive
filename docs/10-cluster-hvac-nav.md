# 10 · Cluster polido, HVAC por zona e navegação

## Cluster (feature/dashboard)
- **Speedometer** com **needle** e arco desenhados no `Canvas`, **animação** de aceleração
  (`animateIntAsState` → o número e o ponteiro deslizam). Anel de bateria, seletor P-R-N-D,
  cards de autonomia/temperatura/modo. Fonte HUD (Chakra Petch).
- **Tema dia/noite:** `AutoDashTheme(tokens, dark)` — escuro usa os tokens da marca; claro
  usa neutros claros + accent. Em produção seguiria a propriedade **NIGHT_MODE** do veículo.

## Clima (feature/climate)
HVAC **por zona** via Car API: `HVAC_TEMPERATURE_SET` por `area` (assento). +/- escreve com
`setProperty` (exige `CONTROL_CAR_CLIMATE`). Ver [`feature/climate`](../feature/climate).

## Navegação (chrome no app)
O `app/` tem a barra (marca + tabs **CLUSTER / CLIMA**); as telas são **headerless** (só conteúdo).
Duas telas, dois ViewModels, um repositório. White-label: sem seletor de marca.

## Testes (sem CI/CD)
- Unit JVM: `UnitsTest`, `ResponsiveTest`, `BrandTest`, `PoiTest`, `ObserveVehicleSpeedTest`.
- **Snapshot (Paparazzi):** dashboard 4 marcas landscape + portrait + **inglês/imperial** +
  **tema claro**; clima 2 marcas.
- **Template POI (Robolectric + androidx.car.app:app-testing):** `PoiScreenTest` valida que
  `PoiScreen` devolve `ListTemplate` com N itens e o detalhe é `PaneTemplate` — testa a UI
  dirigível sem host de carro. 21 testes no total, verdes.
