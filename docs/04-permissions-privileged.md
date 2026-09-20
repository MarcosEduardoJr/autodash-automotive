# 04 · Permissões & apps privilegiados

Permissões vivem em `android.car.permission.*`. **Declarar no manifest não basta.**

| Nível | Quem consegue |
|---|---|
| normal / dangerous | App comum (dangerous pede runtime) |
| signature | App assinado com a **platform key** |
| signature\|privileged | App de sistema **privilegiado**, na **allowlist** da OEM |

- Leituras básicas (velocidade, energia, info) podem ser concedidas a apps comuns.
- **Controle** (HVAC, powertrain, portas) e dados sensíveis são tipicamente
  **`signature|privileged`** → precisa da platform key **ou** estar numa
  `privapp-permissions` (XML em `/etc/permissions`) fornecida pela OEM.

**`SecurityException`** ao ler/escrever prop sensível = quase sempre falta de
assinatura/allowlist, **não** bug de código. A **OEM decide** o que um 3rd-party toca.

**No projeto:** o `AndroidManifest.xml` do `app/` declara `android.car.permission.CAR_SPEED`
(leitura). Controlar clima exigiria privilégio — comentado no manifest.
