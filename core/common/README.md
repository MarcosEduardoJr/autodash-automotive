# `core/common/` — utilidades transversais

**Micro:** `Dispatchers.kt` expõe os dispatchers como uma abstração **injetável**.

**Macro:** por que isso importa no carro? Testabilidade. Se o ViewModel/UseCase usar
`Dispatchers.IO` fixo, o teste vira instrumentado. Injetando, o teste troca por um
`TestDispatcher` e roda na JVM (ver [`docs/08`](../../docs/08-testing-and-distribution.md)).
