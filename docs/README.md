# 📚 docs/ — o curso de Android Automotive

## 🧒 Em miúdos

Este arquivo é o **índice** do curso: a lista de todos os assuntos e por onde começar.
Pense no mapa que fica na entrada de um shopping — antes de sair andando você olha o quadro
"você está aqui" e vê onde fica cada loja. Aqui é igual: cada linha da tabela é um assunto,
já na ordem boa pra estudar. Se aparecer uma palavra estranha, existe um "dicionário" só pra
ela (é o primeiro item da lista). Não precisa decorar nada — isto é só um guia pra você não
se perder.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md).

Aqui mora a parte **macro** do curso. Em uma frase: **macro é o conceito** (o assunto
explicado com calma e de uma vez só, um arquivo por assunto, aqui nesta pasta) e **micro é o
código** (esse mesmo assunto já virando código de verdade). Os "micro" são os README que moram
dentro de cada módulo do projeto — as pastas `app/`, `feature/…`, `core/…` — e cada um deles
aponta de volta pra cá quando o assunto aparece. Ou seja: leia a ideia aqui, veja ela virar
código lá.

Você vem do mundo dos apps de **celular** Android. O que muda aqui é que o app roda **dentro de
um carro** — e o carro tem um monte de siglas próprias que ninguém te contou ainda. Por isso a
**linha 00** vem antes de tudo: comece por ela, sem pressa.

## O que cada capítulo ensina (em palavras simples)

- **00 — Glossário:** o dicionário. Cada sigla nova do curso está explicada ali, com uma
  frase e uma analogia. **Comece por aqui.**
- **01 — O carro é Android?** A diferença nº 1 do curso: **AAOS** (Android Automotive OS — o
  Android que **é** o carro, instalado no painel e que enxerga velocidade, marcha, ar) contra
  **Android Auto** (o seu **celular projetando** a tela no painel, sem enxergar os dados do
  carro). Também: **AOSP** (Android Open Source Project — o Android "puro", aberto e grátis,
  a receita-base) e **GAS** (Google Automotive Services — o pacote do Google, tipo Play Store
  e Maps, que a montadora *pode* licenciar por cima).
- **02 — Como o app fala com o carro.** A **Car API** (o "balcão de atendimento" do carro,
  onde seu app faz pedidos tipo "me diz a velocidade"): a classe **`Car`** (o objeto que
  "liga" seu app ao carro), o gerente **`CarPropertyManager`** (que lê e escreve os dados),
  as **propriedades** (cada dado/controle do carro) e **zonas** (o mesmo dado por lugar —
  ex.: temperatura do motorista vs. do passageiro). Tudo apoiado no **VHAL** (Vehicle HAL, de
  *Vehicle Hardware Abstraction Layer* — a "camada que traduz o hardware" do carro; na prática,
  a **ficha técnica** que a montadora escreve dizendo quais dados existem e se dá pra
  ler/escrever).
- **03 — Telas prontas do carro.** A **Car App Library** deixa fazer telas **sem desenhar
  pixel**: você escolhe um **Template** (um molde de tela pronto — lista, mapa, painel), e o
  **host** (o programa do carro que desenha seus moldes na tela) imprime no padrão do carro.
  As peças: **`CarAppService`** (a porta de entrada do app) e **`Screen`** (uma tela). Já sai
  seguro pra dirigir.
- **04 — O que o app pode tocar (permissões).** No carro, declarar não basta: coisas
  sensíveis exigem **`signature|privileged`** (o app precisa estar assinado com a chave do
  sistema **ou** estar na **allowlist**, a "lista VIP" aprovada pela montadora). Se faltar,
  estoura **`SecurityException`** (o erro de "sem permissão" — quase sempre falta de
  assinatura/lista, **não** bug no seu código).
- **05 — Segurança dirigindo.** As **CarUxRestrictions** (Car UX Restrictions, onde *UX* =
  *User Experience*, a experiência de uso — as regras que o carro impõe à sua tela **enquanto
  anda**: menos texto, sem teclado, sem vídeo, pra não distrair). Elas mudam conforme o
  **driving state** (estado de direção: estacionado, parado ligado, ou andando).
- **06 — Uma marca por cima da outra.** O **car-ui-lib** (biblioteca de componentes de tela
  do sistema que a montadora consegue repintar) e o **RRO** (Runtime Resource Overlay — uma
  "capa" que troca cores/imagens **enquanto o app roda**, sem recompilar). Prova de que
  funcionou: **snapshot** (uma "foto" da tela comparada com a foto-gabarito).
- **07 — Energia e modo garagem.** O **CarPowerManager** (o gerente de energia: ligando,
  "vou dormir", suspenso) e o **garage mode** (a janela em que o carro **desligado acorda
  sozinho** pra fazer manutenção, tipo baixar update de madrugada).
- **08 — Testar e distribuir.** O **emulador** (**AVD**, de *Android Virtual Device* — um
  **head unit** de mentira, ou seja, a tela-computador do painel simulada no seu computador),
  os **fakes** (dublês que fingem ser o carro pra testar sem carro) e a publicação na loja
  automotiva.
- **09 — Uma base, várias marcas.** White-label ("um app, várias marcas"): a identidade vira
  **dado** (os **BrandTokens** / design tokens — cor, fundo, logo com nome), o **dynamic
  theming** re-pinta tudo sem mexer no layout, e o **responsivo** adapta a tela a painéis de
  tamanhos diferentes.
- **10 — Painel bonito, ar por zona e navegação.** O **cluster** (o painel de instrumentos na
  frente do motorista — velocímetro, marcha, bateria; aqui desenhado à mão com o **Canvas**, a
  ferramenta do Android pra pintar a tela ponto a ponto, num visual de **HUD** — Head-Up
  Display, aquele estilo de números grandes e alto contraste), o **HVAC** (Heating, Ventilation,
  Air Conditioning — o ar-condicionado/climatização) controlado **por zona** (por assento), e a
  navegação entre as telas do app.

## Tabela do curso

| # | Arquivo | Conceito |
|---|---------|----------|
| 00 | [00-glossario](00-glossario.md) | **Comece por aqui:** toda sigla explicada do zero, com analogia |
| 01 | [aaos-vs-android-auto](01-aaos-vs-android-auto.md) | AAOS × Android Auto, AOSP/GAS, usuário headless |
| 02 | [car-api-and-vhal](02-car-api-and-vhal.md) | `Car`, `CarPropertyManager`, propriedades, zonas, VHAL |
| 03 | [car-app-library](03-car-app-library.md) | Templates, host, `CarAppService`/`Screen`, restrições |
| 04 | [permissions-privileged](04-permissions-privileged.md) | `signature\|privileged`, allowlist, `SecurityException` |
| 05 | [ux-restrictions](05-ux-restrictions.md) | `CarUxRestrictions`, driving state, distração |
| 06 | [multi-brand-rro](06-multi-brand-rro.md) | car-ui-lib, Runtime Resource Overlay, snapshot |
| 07 | [power-and-garage-mode](07-power-and-garage-mode.md) | `CarPowerManager`, garage mode, always-on |
| 08 | [testing-and-distribution](08-testing-and-distribution.md) | emulador, fakes, Play automotivo |
| 09 | [whitelabel-responsive](09-whitelabel-responsive.md) | white-label, `BrandTokens`, dynamic theming, responsivo |
| 10 | [cluster-hvac-nav](10-cluster-hvac-nav.md) | cluster (Canvas/HUD), HVAC por zona, navegação |

> Ordem sugerida: 00 → 01 → 02 → 03 → 04 → 05 → 06 → 07 → 08 → 09 → 10. Comece sempre pelo
> **00 (glossário)** e volte a ele sempre que topar com uma sigla nova. Mas não precisa seguir
> em linha reta: cada README de módulo te leva ao doc certo na hora certa.

### Palavras novas deste capítulo

- [AAOS](00-glossario.md) — o Android que é o próprio carro.
- [Android Auto](00-glossario.md) — seu celular projetando a tela.
- [AOSP](00-glossario.md) — o Android puro, aberto e grátis.
- [GAS](00-glossario.md) — pacote do Google (Play, Maps) opcional.
- [head unit](00-glossario.md) — a tela-computador do painel do carro.
- [VHAL](00-glossario.md) — a "ficha técnica" dos dados do carro.
- [CarUxRestrictions](00-glossario.md) — regras de segurança da tela dirigindo.
- [RRO](00-glossario.md) — capa que troca cores em runtime.
- [HVAC](00-glossario.md) — o ar-condicionado/climatização do carro.
- [AVD](00-glossario.md) — carro de mentira no seu computador.
- [cluster](00-glossario.md) — o painel de instrumentos do motorista.
- [HUD](00-glossario.md) — visual de painel com números grandes e alto contraste.
- [macro × micro](00-glossario.md) — o conceito (aqui) vs. o código (no módulo).
