# 08 · Testar AAOS & distribuir

## Testar sem (e com) o caminhão
| Nível | Como |
|---|---|
| Unit (JVM) | **Fake** do `CarRepository` — rápido, sem device. Ver `domain/` |
| Integração | Robolectric / test doubles da car-lib |
| Sistema | **Emulador automotivo** (system image Automotive) injeta velocidade/marcha |
| Hardware | O caminhão: validação final |

No emulador, injete **velocidade > 0** pelos *extended controls* para exercitar
`CarUxRestrictions`. Para lógica pura, esconda a Car API atrás de `CarRepository` e use fake.

## Distribuição
- Manifest: `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`.
- **`automotive_app_desc.xml`** declara a **categoria** (`template`/`media`/…), referenciado
  por `<meta-data android:name="com.android.automotive">`.
- A Play tem um **track de form factor automotivo**, com revisão de distração mais rígida.

Sem o descriptor + uses-feature, a Play **não distribui** no carro. Ver `app/src/main/README.md`.
