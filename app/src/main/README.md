# `app/src/main/` — manifest, categorias e a fronteira automotiva

**Macro — o que torna um APK "automotivo":**

### 1. `AndroidManifest.xml`
- `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`
  → sem isso, a Play **não** distribui no carro e o host não reconhece o app.
- Permissões do carro: aqui declaramos `android.car.permission.CAR_SPEED` (leitura).
  Controlar clima exigiria `signature|privileged` (ver [`docs/04`](../../docs/04-permissions-privileged.md)).
- Registramos **duas superfícies**: a `MainActivity` (dashboard parado) e o
  `AutoDashCarAppService` (POI dirigível, via `feature/carapp`).
- `<meta-data android:name="com.android.automotive" android:resource="@xml/automotive_app_desc"/>`.

### 2. `res/xml/automotive_app_desc.xml`
Declara a **categoria** do app. Usamos `template` (Car App Library). Poderia ser
`media` para um player. Sem esse descriptor, o carro não sabe "que tipo" de app é este.

> **Pegadinha:** esquecer `uses-feature` **ou** o `automotive_app_desc` é o erro clássico —
> o app "some" do carro sem erro óbvio. Ver [`docs/08`](../../docs/08-testing-and-distribution.md).
