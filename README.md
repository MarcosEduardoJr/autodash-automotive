# 🚗 AutoDash — app Android Automotive que também é um curso

Este repositório é **duas coisas ao mesmo tempo**:

1. Um **app Android Automotive (AAOS)** real, multi-módulo, em Clean Architecture.
2. Um **curso**: **cada pasta tem um `README.md`** que explica o *micro* (o que aquele
   node faz no projeto) e o *macro* (o conceito de Android Automotive que ele ensina).

Navegue a árvore como se fosse a ementa. Comece pela raiz → `docs/` → e desça módulo a módulo.

> **AAOS ≠ Android Auto.** Este app roda **no head unit do carro** (instalado no veículo,
> com acesso à Car API/VHAL). Android Auto seria o celular projetando a tela. Tudo aqui
> assume AAOS. Detalhe em [`docs/01-aaos-vs-android-auto.md`](docs/01-aaos-vs-android-auto.md).

## O que o app faz
- **Dashboard (parado):** telemetria ao vivo — velocidade, marcha, energia — lida do veículo
  via `CarPropertyManager`. UI em Compose. É `parked-optimized` (rica quando parado).
- **POI (dirigindo):** uma lista de pontos de interesse pela **Car App Library** (templates),
  segura para dirigir — o *host* do carro renderiza.

## Mapa da árvore (cada node tem README)
```
autodash-automotive/
├── docs/            → o curso: um .md por conceito de AAOS
├── app/             → o módulo de APP automotivo (manifest, categorias, host)
├── core/
│   ├── model/       → modelos de domínio (VehicleSpeed, Gear…)
│   ├── common/      → utilidades (dispatchers, Result)
│   └── designsystem/→ tema/tokens "temáveis" por marca (RRO-friendly)
├── domain/          → use cases PUROS (sem android.car → testável na JVM)
├── data/
│   └── car/         → a ponte com o veículo: wrapper do CarPropertyManager
└── feature/
    ├── dashboard/   → tela Compose de telemetria (parado)
    └── carapp/      → CarAppService + Screen de POI (dirigindo)
```

## A regra da dependência
```
feature ─▶ domain ◀─ data        (todos apontam p/ o domínio)
   │                    │
   └────▶ core ◀────────┘        (model/common/designsystem são folhas)
```
O **domínio não conhece Android nem o carro**. Quem fala com `android.car.*` é só
`data/car`. Assim a regra de negócio roda em teste unitário puro, sem emulador automotivo.

## Como rodar (resumo)
- Abra no Android Studio (Koala+). O app é **white-label**: a marca é um `BrandTokens` (design system), fixa por build (white-label real: sem seletor pro usuário) — marcas de exemplo neutras, não OEMs reais.
- Rode numa **AVD com system image Automotive** (o emulador injeta velocidade/marcha).
- Detalhes e distribuição: [`docs/08-testing-and-distribution.md`](docs/08-testing-and-distribution.md).

## Índice do curso
Ver [`docs/README.md`](docs/README.md).
