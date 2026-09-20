# `core/model/` — modelos de domínio (puros)

**Micro:** `Vehicle.kt` define `VehicleSpeed`, `Gear`, `Energy` — os tipos que fluem da
camada de dados até a UI.

**Macro:** aqui a Car API vira **domínio limpo**. O `CarPropertyManager` fala em `Float`
cru e `VehiclePropertyIds`; nós traduzimos para tipos com significado (`Gear.DRIVE`).
Assim a regra de negócio e a UI **não** dependem do SDK do carro — só destes modelos.

Módulo **kotlin-jvm puro** (sem Android): roda em teste unitário instantâneo.
