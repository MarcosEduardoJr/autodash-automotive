# 03 · Car App Library (apps dirigíveis por templates)

Você **não desenha pixels**: descreve **templates** e o **host** do carro renderiza —
consistência + segurança. O mesmo app roda em **AAOS e Android Auto**.

| Peça | Papel |
|---|---|
| `CarAppService` | Ponto de entrada (declarado no manifest com a categoria) |
| `Session` | Conexão com o host; entrega a `Screen` inicial |
| `Screen` | Produz um `Template` em `onGetTemplate()` |
| `ScreenManager` | Pilha de telas (`push`/`pop`) |

Templates: `ListTemplate`, `GridTemplate`, `PaneTemplate`, `MessageTemplate`,
`NavigationTemplate`, `MapTemplate`… → escolha pela tarefa.

## Restrições (distração)
- Profundidade máxima da pilha (**limite de passos**).
- Nº máx de itens por lista → consulte o `ConstraintManager`.
- **Refresh restrito**: só certas mudanças não contam como novo passo. Use `invalidate()`.

Fluxos **rasos** e listas **curtas** são obrigatórios. Estourar o limite = host recusa a UI.

**No projeto:** [`feature/carapp`](../feature/carapp) traz um `CarAppService` + `PoiScreen`
com `ListTemplate`.
