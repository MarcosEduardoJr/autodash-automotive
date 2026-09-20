# `feature/carapp/` — POI dirigível (Car App Library)

**Micro:** `AutoDashCarAppService` é o entry point registrado no manifest (categoria POI);
`PoiScreen` devolve um `ListTemplate` com pontos de interesse.

**Macro — templates dirigíveis (ver [`docs/03`](../../docs/03-car-app-library.md)):**
- Você **não desenha pixels**: descreve o template, o **host** do carro renderiza.
- O mesmo `CarAppService` roda em **AAOS e Android Auto**.
- **Restrições**: lista curta (o host limita itens em movimento via `ConstraintManager`),
  navegação rasa. Aqui a UI já nasce segura para dirigir.

> Contraste com o `dashboard`: aquele é Compose/Activity (parked); este é template
> (drivable). Mesma app, duas superfícies.

## Navegável (ListTemplate → PaneTemplate)
- `PoiScreen` (ListTemplate): lista de POIs; clicar empilha o detalhe (`ScreenManager.push`).
- `PoiDetailScreen` (PaneTemplate): endereço/distância + ação **"Navegar"** (em produção,
  entregaria um `Trip` ao `NavigationManager`). BACK volta (`pop`).
- `Poi.kt`: o dado (com teste `PoiTest`). Mostra dois templates + navegação entre telas —
  fluxo raso e lista curta por distração (docs/03 + docs/05).
