# `data/` — de onde vêm os dados

**Macro:** a camada que fala com o mundo externo. No automotivo, o "mundo externo" mais
importante é **o próprio veículo**. Aqui vive o único módulo que importa `android.car.*`.

- [`car/`](car) — wrapper do **CarPropertyManager**: implementa `CarRepository` do domínio.

> Isolar a Car API aqui é o que mantém `domain/` e `feature/` testáveis sem emulador.
