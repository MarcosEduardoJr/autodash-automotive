# 08 · Testar AAOS & distribuir

## Testar sem (e com) o veículo
| Nível | Como |
|---|---|
| Unit (JVM) | **Fake** do `CarRepository` — rápido, sem device. Ver `domain/` |
| Integração | Robolectric / test doubles da car-lib |
| Sistema | **Emulador automotivo** (system image Automotive) injeta velocidade/marcha |
| Hardware | O veículo real: validação final |

No emulador, injete **velocidade > 0** pelos *extended controls* para exercitar
`CarUxRestrictions`. Para lógica pura, esconda a Car API atrás de `CarRepository` e use fake.

## Mapa de testes deste projeto
| Arquivo | Tipo | O que prova (e o conceito AAOS) |
|---|---|---|
| `core/model/.../UnitsTest`, `ClimateTest` | Unit puro (JVM) | i18n (km/h×mph por locale) e o modelo `Climate`/`temp(seat)` — anel interno, testável sem device |
| `domain/.../ObserveVehicleSpeedTest` | Unit | use case sobre o `CarRepository` (sem `android.car`) |
| `feature/dashboard/.../DashboardViewModelTest` | Unit (coroutines-test) | **MVVM**: o VM mapeia sinais do repo → `StateFlow`. Usa `runCurrent()` (não `advanceUntilIdle`) porque os fluxos do fake são `while(true){emit;delay}` (infinitos) |
| `feature/climate/.../ClimateViewModelTest` | Unit (coroutines-test) | HVAC por zona: `togglePower/toggleAc/fan/delta`, clamps (fan 0..6, temp 16..28), zona independente. `WhileSubscribed` exige coletor ativo no teste |
| `feature/dashboard/.../DashboardSnapshotTest` | **Snapshot** (Paparazzi) | regressão visual: 4 marcas landscape + portrait + inglês/imperial + tema claro |
| `feature/climate/.../ClimateSnapshotTest` | **Snapshot** (Paparazzi) | clima por marca + **gating**: `climate_power_off` e `climate_ac_off` congelam o estado desabilitado/apagado |
| `feature/carapp/.../PoiScreenTest` | **Template** (Robolectric + `androidx.car.app:app-testing`) | `ListTemplate`→`PaneTemplate`→`NavigationTemplate` sem host |

Padrão de VM em teste: `Dispatchers.setMain(StandardTestDispatcher())` no `@Before`, `resetMain()`
no `@After`. Rodar tudo: `./gradlew test`; regravar goldens: `./gradlew recordPaparazziDebug`.

## Distribuição
- Manifest: `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`.
- **`automotive_app_desc.xml`** declara a **categoria** (`template`/`media`/…), referenciado
  por `<meta-data android:name="com.android.automotive">`.
- A Play tem um **track de form factor automotivo**, com revisão de distração mais rígida.

Sem o descriptor + uses-feature, a Play **não distribui** no carro. Ver `app/src/main/README.md`.
