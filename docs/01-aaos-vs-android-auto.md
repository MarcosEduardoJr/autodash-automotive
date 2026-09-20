# 01 · AAOS × Android Auto (e o que é AAOS por dentro)

## A distinção que cai em toda entrevista
- **Android Automotive OS (AAOS):** um Android que **É o carro** — roda no *head unit*.
  O app é instalado no veículo e tem acesso à **Car API / VHAL**.
- **Android Auto:** o **celular projetando** a tela no painel. O app roda no telefone e
  **não** fala com o hardware do veículo.

| | AAOS | Android Auto |
|---|---|---|
| Onde roda | Head unit do carro | Celular (projetado) |
| Dados do veículo | Sim (Car API/VHAL) | Não |
| Precisa de celular | Não | Sim |

## Por dentro do AAOS
- Base **AOSP**; a montadora (OEM) customiza. Com **GAS** (Google Automotive Services) vêm
  Play, Maps, Assistant. Sem GAS, a OEM traz a própria loja/serviços.
- **CarService** é o serviço de sistema que hospeda os *managers* (property, power, ux…).
  Seu app é **cliente** dele via a classe `Car` (ver `data/car`).
- **car-ui-lib** (`com.android.car.ui`): componentes de sistema que a OEM sobrepõe via
  **RRO** — por isso cada marca fica diferente (ver `docs/06`).
- **Multi-user *headless*:** o *system user* (headless) roda os serviços; o motorista é
  outro user. Assumir "um usuário só" (como no celular) causa bug de dados só no carro.

**No projeto:** este app inteiro assume AAOS — `data/car` só existe porque há Car API.
