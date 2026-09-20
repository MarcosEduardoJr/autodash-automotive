# 07 · Power, boot & garage mode

Head unit **não é celular**: suspende/acorda com a ignição e roda tarefas com o carro
**desligado**.

- **`CarPowerManager`** → estados de energia (`ON`, `SHUTDOWN_PREPARE`, `SUSPEND`).
  Assine para **salvar estado** ao receber `SHUTDOWN_PREPARE`, antes de dormir.
- **Garage Mode:** janela em que o sistema **acorda/mantém** o device (parado/desligado)
  para **manutenção** — updates, upload de logs, sync pesado. Agende via
  **JobScheduler/WorkManager** → roda no garage mode, **sem** atrapalhar a direção.
- Boot acoplado à ignição → **cold start** importa. Sendo **always-on**, cuide de
  memória/leak. Nada de wakelock à toa dirigindo para "sincronizar agora".

**No projeto:** telemetria não persiste nada crítico, mas o README de `data/car`
mostra onde entraria o `SHUTDOWN_PREPARE`.
