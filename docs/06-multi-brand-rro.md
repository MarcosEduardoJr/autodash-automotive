# 06 · Multi-brand, theming & RRO

Um app, várias montadoras. A cola:

- **car-ui-lib** expõe componentes de sistema; a OEM aplica um **RRO
  (Runtime Resource Overlay)** que troca cores/drawables/estilos **em runtime**,
  **sem recompilar** o app. É como cada marca fica diferente sobre a mesma base.
- Seu app precisa ser **overlay-friendly**: use **atributos de tema / design tokens**,
  **nunca hex hardcoded** (um `0xFF0066CC` fixo ignora o overlay → componente "fora da marca").
- **Product flavors** por marca empacotam recursos/config específicos.

## White-label na prática (este projeto)
A identidade é um `BrandTokens` (design system), não código. Marcas **neutras** de propósito
(Slate/Aurora/Ember/Nord) — nada de OEM real hardcoded. Ver [`docs/09`](09-whitelabel-responsive.md).

```kotlin
// core/designsystem: a marca é DADO
data class BrandTokens(val id: String, val name: String, val primary: Color, /* … */)

// a tela lê o token, nunca o hex → RRO/tema conseguem sobrepor
Text("...", color = MaterialTheme.colorScheme.primary)

// o app entrega UMA marca por build (white-label). Sem seletor pro usuario:
AutoDashTheme(tokens = Brands.byId(BuildConfig.DEFAULT_BRAND)) { DashboardScreen(state) }
```

**Produção:** cada OEM = um **flavor** que define a marca padrão (`BuildConfig.DEFAULT_BRAND`)
e traz recursos/RRO — o mecanismo "multi-flavor/multi-brand build", sem `if (marca)` nas telas.

## Snapshot testing
A vaga pede **snapshot testing** p/ consistência multi-marca. **Paparazzi** renderiza a tela
por marca/tema (na JVM) e o CI falha se um pixel muda sem querer — pega regressão visual sem
abrir cada carro. Cubra RTL e fontes ampliadas também.

**No projeto:** `feature/dashboard/src/test/DashboardSnapshotTest` gera goldens para as 4
marcas (landscape) + portrait em `src/test/snapshots/`.
