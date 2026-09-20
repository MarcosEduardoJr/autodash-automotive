# `domain/` — regra de negócio PURA (sem android.car)

**Micro:** define a interface `CarRepository` (o **contrato** com o veículo) e use cases
como `ObserveVehicleSpeed`. Nenhum import de `android.car` aqui.

**Macro:** este é o pulo da Clean Architecture no automotivo. O domínio **declara** o que
precisa do carro (uma interface `Flow<VehicleSpeed>`), e `data/car` **implementa** usando o
`CarPropertyManager`. Inversão de dependência (o "D" de SOLID). Resultado: a regra roda em
**teste unitário puro na JVM** com um fake — sem emulador automotivo (ver [`docs/08`](../docs/08-testing-and-distribution.md)).

> Se você importar `CarPropertyManager` aqui, quebrou a camada. O teste vira instrumentado.
