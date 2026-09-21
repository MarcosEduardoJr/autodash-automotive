# 01 · AAOS × Android Auto (e o que é AAOS por dentro)

## 🧒 Em miúdos

Imagine duas formas de ver Netflix na sala. Na primeira, você tem uma **smart TV** que já vem com o Netflix instalado por dentro — ela é o aparelho, funciona sozinha. Na segunda, você tem uma TV comum e **espelha o celular** nela: quem faz todo o trabalho é o telefone; a TV só mostra a imagem.

No carro é igualzinho. Ou o **sistema do carro é o próprio Android** (a tela do painel é o "computador que manda"), ou o carro só **espelha o seu celular** naquela tela. São duas coisas bem diferentes, e o curso inteiro gira em torno dessa diferença. Aqui a gente deixa ela cristalina — e depois abre o "sistema-que-é-o-carro" pra ver o que tem dentro.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md). Não precisa decorar nada: quando bater uma dúvida numa sigla, é só voltar lá.

## A distinção fundamental

Antes de qualquer sigla, guarde uma imagem: a tela grande do painel (onde ficam rádio, mapa e ar-condicionado) é um **computador rodando um sistema**. Esse conjunto tela+computador tem um nome — **head unit** (o "celular embutido no carro"; sempre que ler "roda no head unit", pense "roda nesse computador do painel"). A pergunta do capítulo é: *quem manda nesse computador?*

- **Android Automotive OS (AAOS — o Android que _é_ o carro; *OS* = *Operating System*, ou seja, "sistema operacional"):** um Android instalado **no próprio veículo**, rodando no *head unit*. Como a smart TV que já tem tudo dentro, ele **é** o sistema do carro. Por isso o app fica instalado no veículo e consegue enxergar os dados dele (velocidade, marcha, ar) através de duas peças:
  - a **Car API** (*API* = *Application Programming Interface*, "interface de programação" — aqui, o "balcão de atendimento" por onde o app pede coisas ao carro, em vez de mexer no hardware direto), e
  - a **VHAL** (*Vehicle HAL*; *HAL* = *Hardware Abstraction Layer*, a "camada que traduz o hardware" pro Android — a "ficha técnica" que a montadora escreve dizendo quais dados o carro tem e se dá pra ler/escrever).
- **Android Auto (o celular _projetando_ a tela):** aqui o app roda no **seu telefone** e o carro só exibe a imagem no painel, por cabo ou Wi-Fi — exatamente como espelhar o celular na TV. Justamente por rodar no telefone, ele **não** fala com o hardware do veículo: não lê a velocidade, não mexe no ar.

⚠️ Não confunda os dois — essa é a diferença nº 1 do curso. Os nomes se parecem, mas a ideia é oposta: **AAOS = o carro é o computador**; **Android Auto = o celular é o computador, e o carro é só a tela**.

| | AAOS | Android Auto |
|---|---|---|
| Onde roda | Head unit do carro | Celular (projetado) |
| Dados do veículo | Sim (Car API/VHAL) | Não |
| Precisa de celular | Não | Sim |

Lendo a tabela devagar: no **AAOS**, o app mora e roda **dentro do carro**, tem acesso aos dados do veículo e não depende de nenhum celular. No **Android Auto**, o app mora **no celular**, precisa do celular presente e fica sem acesso aos dados do carro. Este curso é todo sobre o **AAOS** — o Android que é o carro.

## Por dentro do AAOS

Agora que sabemos que o AAOS é "o Android que é o carro", vamos abrir a caixa e olhar as peças lá dentro.

- Todo AAOS parte do **AOSP** (Android Open Source Project — o Android "puro", aberto e de graça, a "receita base do bolo"). A montadora — o **OEM** (Original Equipment Manufacturer, que é só o jargão para *a fabricante do carro*: Volvo, Toyota, etc.) — pega essa base e customiza. Se ela licenciar o **GAS** (Google Automotive Services — o "combo do Google" instalado por cima: **Play** Store, **Maps**, **Assistente**), o carro já vem com os apps do Google. Sem GAS, a montadora traz a própria loja e os próprios serviços.
- **CarService** (o serviço de sistema que fica **sempre rodando** no carro e coordena tudo) é quem hospeda os *managers* — os "gerentes", cada um cuidando de um assunto do carro: `CarPropertyManager` para dados/sensores, `CarPowerManager` para energia, `CarUxRestrictionsManager` para as regras de segurança na direção, e por aí vai. Seu app é **cliente** desse serviço: ele se conecta pela classe `Car` (o "objeto de conexão" que liga o app ao CarService — ver `data/car`).
- **car-ui-lib** (`com.android.car.ui` — a biblioteca de componentes de tela do próprio sistema do carro: listas, botões no jeitão do carro) foi feita para a montadora **repintar**. Ela troca cores e estilos por cima usando **RRO** (Runtime Resource Overlay — uma "capa/filtro" que muda cores, imagens e estilos *enquanto o app roda*, sem precisar recompilar). É por isso que o mesmo sistema-base fica com a cara de cada marca (ver `docs/06`).
- **Multi-usuário _headless_:** no celular existe um usuário só; no carro, não. Existe um **system user** (o usuário "headless" — *headless* quer dizer "sem cabeça", ou seja, *sem tela própria*) que roda os serviços do sistema nos bastidores, e o motorista é **outro** usuário, por cima desse. Se você programar assumindo "tem um usuário só" (como no celular), aparece um bug chato que **só dá no carro** — dados que somem ou vão parar no usuário errado.

**No projeto:** este app inteiro assume AAOS — `data/car` só existe porque há Car API.

### Palavras novas deste capítulo

- [AAOS](00-glossario.md) — o Android que é o carro
- [head unit](00-glossario.md) — tela e computador do painel
- [Android Auto](00-glossario.md) — celular projetando a tela no painel
- [Car API](00-glossario.md) — comandos do app pro carro
- [HAL](00-glossario.md) — camada que traduz o hardware pro Android
- [VHAL](00-glossario.md) — ficha técnica das propriedades do veículo
- [AOSP](00-glossario.md) — Android puro, aberto e gratuito
- [OEM](00-glossario.md) — a montadora, fabricante do carro
- [GAS](00-glossario.md) — combo Google: Play, Maps, Assistente
- [CarService](00-glossario.md) — serviço do sistema que coordena tudo
- [manager](00-glossario.md) — "gerente" de um assunto do carro
- [car-ui-lib](00-glossario.md) — componentes de tela do sistema, repintáveis
- [RRO](00-glossario.md) — troca cores e estilos sem recompilar
- [usuário headless](00-glossario.md) — usuário de sistema sem tela
