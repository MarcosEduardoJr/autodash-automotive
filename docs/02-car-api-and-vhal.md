# 02 · Car API, CarPropertyManager e VHAL

## A porta para os dados do veículo
Tudo começa em `Car.createCar(context)` → `getCarManager(Car.PROPERTY_SERVICE)`.
O **CarPropertyManager** lê/escreve/assina **propriedades** identificadas por
`VehiclePropertyIds` (`PERF_VEHICLE_SPEED`, `GEAR_SELECTION`, `HVAC_TEMPERATURE_SET`…).

| Chamada | Quando |
|---|---|
| `registerCallback` | Valor muda no tempo (velocidade, marcha) — você observa |
| `getProperty` | Leitura pontual (nível atual de bateria) |
| `setProperty` | Escrever/controlar (temperatura) — **exige permissão** (ver `docs/04`) |

## Zonas (áreas)
Propriedade pode ser **global** (areaId 0) ou **por zona** (assento, roda, janela).
HVAC é por assento → passe o `areaId`. Ler global uma prop zoneada é erro comum.

## VHAL (Vehicle HAL) — o contrato de hardware
A OEM implementa o VHAL; cada propriedade tem um *config*:
- **access:** `READ` / `WRITE` / `READ_WRITE` (`setProperty` em READ = erro).
- **changeMode:** `STATIC` (VIN), `ON_CHANGE` (marcha), `CONTINUOUS` (velocidade, com sample rate).
- **vendor properties** (grupo `0x2000_0000`): específicas da OEM (ex.: modo caminhão, PTO).

## Sempre
- Cheque `CarPropertyValue.getStatus()` (`AVAILABLE/UNAVAILABLE/ERROR`) — hardware é heterogêneo.
- **Desregistre** o callback no fim: carro fica ligado horas → callback vivo = leak.

**No projeto:** implementado em [`data/car`](../data/car) com `callbackFlow` + `awaitClose`.
