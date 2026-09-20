# `gradle/`

**Micro:** guarda o `libs.versions.toml` — o **Version Catalog** (fonte única de versões).
(O wrapper `gradle-wrapper.properties` também moraria aqui num projeto gerado pelo Studio.)

**Macro:** num app **multi-marca**, dezenas de módulos compartilham libs. Centralizar as
versões evita *dependency hell* (duas marcas puxando versões conflitantes). Ver `docs/06`.
