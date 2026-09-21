# 07 · Power, boot & garage mode

## 🧒 Em miúdos

O computador do painel do carro não é um celular. Um celular você desliga e ele apaga de vez; o carro é mais parecido com uma geladeira, que nunca desliga totalmente e ainda aproveita a madrugada pra fazer tarefas sozinho. Quando o motorista gira a chave pra desligar, esse computador não "morre" na hora — ele guarda o que estava fazendo, cochila e, às vezes, acorda escondido com o carro parado só pra baixar uma atualização ou mandar relatórios. Este capítulo é sobre como o seu app se comporta nesse vai-e-vem de ligar, dormir e acordar sozinho — sem gastar energia à toa nem perder o que o usuário estava vendo na tela.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md).

## Head unit não é celular

O **head unit** (a tela + computador do painel do carro — pense nele como "o celular embutido no carro") tem um ciclo de vida bem diferente do telefone que você já conhece. No celular, o usuário liga e desliga a tela o dia inteiro e o sistema pode matar o seu app quando quiser. No carro, quem manda no liga/desliga é a **ignição** (girar a chave ou apertar o botão de partida): quando o motorista desliga o carro, o head unit **não apaga na hora** — ele **suspende** (entra num sono leve, pra voltar rápido depois) e pode até **acordar sozinho** mais tarde, com o carro parado, só pra fazer tarefas de manutenção.

Ou seja: aquele resumo seco de "suspende/acorda com a ignição e roda tarefas com o carro desligado" quer dizer exatamente isto — o computador do painel dorme e acorda no ritmo do carro, e não no ritmo de um botão de "power" de celular.

## `CarPowerManager` — o carro te avisa antes de dormir

Assim como no celular você pede ao sistema um "gerente" pra cada assunto (localização, notificações), no carro cada assunto tem o seu **manager** (o "gerente" do sistema para aquele tema). O **`CarPowerManager`** é o gerente de **energia**: é ele que sabe se o carro está ligado, prestes a dormir ou já suspenso — e, o mais importante, é ele que **avisa o seu app** quando a energia vai mudar.

Por que isso importa? No celular, se o sistema fecha o seu app, tudo bem: o usuário reabre. No carro, quando o motorista desliga a ignição, o seu app precisa **salvar o que estava fazendo** antes de o computador cochilar, pra não perder o estado. Por isso você **assina** as mudanças de energia — ou seja, deixa um recado dizendo "me avise quando mudar", em vez de ficar perguntando o tempo todo.

Os estados de energia que interessam:

| Estado | O que significa | O que o seu app faz |
|---|---|---|
| `ON` | Head unit ligado, funcionando normalmente | Roda como sempre |
| `SHUTDOWN_PREPARE` | "Vou desligar/dormir já já — se preparem" | **Salva o estado agora** (o momento-chave) |
| `SUSPEND` | Device entrando em suspensão (sono; a tela apaga) | Já deve ter salvado; encerra o que gasta energia |

A regra de ouro: **assine o `CarPowerManager` e, ao receber o aviso `SHUTDOWN_PREPARE`, salve o estado antes de o carro dormir.** Essa é a sua única janela garantida pra guardar as coisas.

## Garage Mode — o carro fazendo tarefas de madrugada

**Garage Mode** (modo garagem) é uma janela de tempo em que o carro, **desligado e parado**, **acorda sozinho** pra fazer manutenção que não pode atrapalhar quem está dirigindo: baixar uma atualização de sistema, subir **logs** (os registros do que aconteceu) pra montadora, fazer uma sincronização pesada. A analogia é literal: é o carro fazendo **tarefas de madrugada**, quietinho na garagem, enquanto ninguém está usando.

Você **não** roda esse trabalho pesado enquanto o motorista dirige (ia distrair e gastar recurso na hora errada). Em vez disso, você **agenda** a tarefa e deixa o sistema rodá-la durante o garage mode. Pra agendar, use as mesmas ferramentas que você já conhece do Android de celular:

- **`JobScheduler`** — a **API** (Application Programming Interface — o conjunto de comandos pronto que o sistema oferece pro seu app) padrão do Android pra dizer "rode esta tarefa quando estas condições forem verdade" (por exemplo: só com energia, sem pressa).
- **`WorkManager`** — a biblioteca moderna do Android pra trabalho em segundo plano que **precisa acontecer mesmo se o app fechar**. É a recomendada hoje em dia.

Agendou por uma dessas → a tarefa **roda no garage mode**, **sem** atrapalhar a direção.

## Boot, cold start e "always-on"

Como o **boot** (a inicialização do sistema) está **acoplado à ignição**, cada vez que o carro liga pode ser uma partida começando quase do zero. Por isso o **cold start** (a "partida fria" — o tempo que o app leva pra abrir quando **nada** dele estava carregado ainda) importa muito aqui: o motorista gira a chave e quer ver o painel **na hora**, não uma tela de "carregando".

Por outro lado, o head unit é **always-on** ("sempre ligado" — na prática, ligado ou cochilando por muitas horas seguidas, e não reiniciado o tempo todo como talvez você esperasse). Isso muda o jogo em duas frentes:

- **Memória e leak (vazamento).** Um **leak** acontece quando o seu app "assina" um aviso (um callback) e **nunca cancela** — aí ele fica ouvindo pra sempre e comendo memória. No celular isso às vezes passa batido, porque o app fecha logo. No carro, que fica ligado por horas, um vazamentinho vira um problemão. Regra: **desregistre** (cancele o recado) todo callback quando a tela some. (Como diz o glossário: é fechar a torneira depois de usar.)
- **Nada de wakelock à toa.** Um **wakelock** ("trava-sono") é um pedido pro sistema **não** deixar o device dormir, mantendo o processador acordado. Segurar um wakelock enquanto o carro anda só pra "sincronizar agora" é desperdício de energia — deixe o sync pro garage mode. Só mantenha o device acordado quando for de fato necessário.

## No projeto

**No projeto:** a telemetria (os dados que o painel mostra em tempo real) **não persiste nada crítico** — se o carro dormir, não há um estado precioso pra se perder. Mesmo assim, o README de [`data/car`](../data/car) mostra **onde entraria** o tratamento de `SHUTDOWN_PREPARE`: seria ali, num listener (o "ouvinte" — o objeto que recebe o callback do carro) do `CarPowerManager`, que você salvaria o estado antes de suspender, caso um dia isso fosse preciso.

### Palavras novas deste capítulo

- [Head unit](00-glossario.md) — o computador e a tela do painel.
- [CarPowerManager](00-glossario.md) — gerente de energia que avisa o app.
- [garage mode](00-glossario.md) — carro acorda sozinho pra manutenção.
- [manager](00-glossario.md) — o "gerente" do sistema por assunto.
- [callback / assinar](00-glossario.md) — deixar o recado "me avise quando mudar".
- [leak](00-glossario.md) — assinatura nunca cancelada, que vaza memória.
