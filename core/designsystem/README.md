# `core/designsystem/` — design system WHITE-LABEL

**Micro:** `Brand.kt` define `BrandTokens` (identidade = dado) + `Brands` (registro de marcas
neutras) + `LocalBrandTokens`. `Theme.kt` mapeia os tokens para o Material3 e publica os tokens.

**Macro — o coração do multi-brand (o que a vaga pede: "design systems for multi-brand UX",
"dynamic theming"):**
- A marca é **token**, não código. Nenhuma tela conhece uma OEM específica — ela lê
  `MaterialTheme.colorScheme` + `LocalBrandTokens`. Por isso o projeto é **white-label**:
  o mesmo binário vira qualquer marca só trocando o `BrandTokens`.
- **Dynamic theming:** `AutoDashTheme(tokens)` — mudar `tokens` re-tematiza tudo em runtime
  (o app tem um seletor de marca para demonstrar).
- **RRO** da OEM (docs/06) sobrepõe por cima, também em runtime, sem recompilar.
- As marcas de exemplo (`Slate/Aurora/Ember/Nord`) têm nomes **neutros de propósito** — não
  são OEMs reais. Em produção, cada OEM = um flavor que fornece seus tokens (+ recursos/RRO).

Cubra com **snapshot testing** por marca (`feature/dashboard` tem os testes Paparazzi) —
o CI barra regressão visual entre marcas sem abrir cada carro.
