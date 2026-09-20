# 06 · Multi-brand, theming & RRO

Um app, várias montadoras. A cola:

- **car-ui-lib** expõe componentes de sistema; a OEM aplica um **RRO
  (Runtime Resource Overlay)** que troca cores/drawables/estilos **em runtime**,
  **sem recompilar** o app. É como cada marca fica diferente sobre a mesma base.
- Seu app precisa ser **overlay-friendly**: use **atributos de tema / design tokens**,
  **nunca hex hardcoded** (um `0xFF0066CC` fixo ignora o overlay → componente "fora da marca").
- **Product flavors** por marca empacotam recursos/config específicos.

## Snapshot testing
A vaga pede **snapshot testing** p/ consistência multi-marca. **Paparazzi**/**Roborazzi**
renderizam a tela por marca/tema (na JVM) e o **CI falha** se um pixel muda sem querer —
pega regressão visual sem abrir cada carro. Cubra RTL e fontes ampliadas também.

**No projeto:** [`core/designsystem`](../core/designsystem) só expõe tokens; o `app/`
declara flavors `volvo`/`scania`.
