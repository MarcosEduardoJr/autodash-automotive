# implementação

- `CarPropertyRepository.kt` — o wrapper. Observe o padrão `callbackFlow` + `awaitClose`:
  registra o callback ao coletar, **desregistra** ao cancelar. Zero leak.
- `VehicleProps.kt` — mapeadores de valor cru do VHAL → modelos de domínio.
