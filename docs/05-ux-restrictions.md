# 05 · CarUxRestrictions & distração do motorista

Segurança é **requisito**, não detalhe. O sistema limita sua UI enquanto o carro anda.

- **`CarUxRestrictionsManager`** → restrições ativas (assine e adapte).
- **`CarDrivingStateManager`** → estado `PARKED` / `IDLING` / `MOVING`.

| Restrição | Implica |
|---|---|
| `NO_KEYBOARD` | Sem digitação livre em movimento |
| `NO_VIDEO` | Sem vídeo dirigindo |
| `LIMIT_STRING_LENGTH` | Textos curtos (há máximo) |
| `LIMIT_CONTENT` | Menos itens / menos profundidade |

`getMaxRestrictedStringLength` / `getMaxCumulativeContentItems` / `getMaxContentDepth`
dizem **quanto** mostrar. **Parado** libera tudo → a mesma tela tem 2 versões.

Base legal: **NHTSA** (regra do olhar ~2s, tarefas curtas).

**No projeto:** o `dashboard` é rico **parado**; em movimento, telemetria só de leitura.
POI vai pela Car App Library, que já aplica as restrições no host.
