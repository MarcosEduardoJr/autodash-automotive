# `core/designsystem/` — tema temável por marca (RRO-friendly)

**Micro:** `Theme.kt` expõe cores/tipografia como **tokens** de tema. As telas leem o
token, **nunca** um hex.

**Macro:** este é o coração do **multi-brand**. A OEM aplica um **RRO (Runtime Resource
Overlay)** que sobrepõe recursos em runtime, sem recompilar. Se uma tela usar
`Color(0xFF0066CC)` fixo, o overlay **não** a alcança e o componente fica "fora da marca".
Regra: sempre atributo de tema / token. Detalhe em [`docs/06`](../../docs/06-multi-brand-rro.md).

Cubra com **snapshot testing** (Paparazzi/Roborazzi) por marca no CI.
