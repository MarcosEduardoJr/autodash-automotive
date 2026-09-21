# 00 · Glossário — toda sigla explicada do zero

> Você sabe fazer app de **celular** Android. Aqui é app de **carro**, que tem um mundo
> de siglas próprias. Este glossário explica cada uma **em uma frase simples + uma analogia**.
> Sempre que um doc usar uma sigla, ela está aqui. Não precisa decorar — volte quando precisar.

## O básico do "Android no carro"

- **Carro moderno tem um computador.** A tela grande no painel (onde ficam rádio, mapa, ar
  condicionado) é um computador rodando um sistema. Em muitos carros novos, esse sistema é
  **Android** — mas uma versão feita pra carro, não a do celular.

- **Head unit** — é essa **tela+computador do painel** do carro. Pense nela como o "celular
  embutido no carro". Quando falamos "roda no head unit", é "roda nesse computador do painel".

- **AAOS (Android Automotive OS)** — o Android que **É o carro**: está instalado no head unit
  e enxerga os dados do veículo (velocidade, marcha, ar). Analogia: é o **sistema operacional do
  carro**, do mesmo jeito que o Android do seu celular é o sistema do telefone. ⚠️ Não confunda
  com o de baixo:

- **Android Auto** — é o seu **celular projetando** a tela no painel (por cabo/Wi-Fi). O app roda
  no **telefone**, o carro só mostra. Analogia: é como **espelhar o celular na TV** — a TV mostra,
  mas quem processa é o telefone. Ele **não** enxerga os dados do carro. (AAOS ≠ Android Auto: a
  diferença nº1 do curso — ver [`01`](01-aaos-vs-android-auto.md).)

- **AOSP (Android Open Source Project)** — o Android "puro", de graça e aberto, que qualquer um
  pode pegar. Analogia: a **receita base do bolo**. Cada montadora parte dele e customiza.

- **OEM (Original Equipment Manufacturer)** — jargão para **a montadora / fabricante do carro**
  (Volvo, Toyota, etc.). Sempre que ler "OEM", leia "a montadora". É ela que monta o AAOS do
  carro dela e decide o que seu app pode fazer.

- **GAS (Google Automotive Services)** — o **pacote do Google** (Play Store, Maps, Assistente)
  que a montadora *pode* licenciar. Analogia: o AOSP é o Android sem os apps do Google; **GAS** é
  "instalar o combo Google por cima". Sem GAS, a montadora traz a loja/serviços dela.

- **SDK (Software Development Kit)** — o **kit de ferramentas** pra programar pra uma plataforma
  (bibliotecas, o emulador, etc.). "SDK do Android" = as peças pra construir apps Android.

## Como o app conversa com o carro

- **Car API** — o **conjunto de comandos** que o Android dá pro seu app **falar com o carro**
  ("me diz a velocidade", "liga o ar"). Analogia: é o **balcão de atendimento** do carro — você
  faz pedidos por ele em vez de mexer no hardware direto. (Só existe no AAOS.)

- **CarService** — o **serviço do sistema** (sempre rodando no carro) que fica atrás desse balcão
  e coordena tudo. Seu app é **cliente** dele.

- **`Car` (a classe)** — o **objeto de conexão**: `Car.createCar(context)` "liga" seu app ao
  CarService. Analogia: **pegar o telefone e discar** pro balcão de atendimento.

- **manager** — cada assunto do carro tem um "gerente" (manager). Ex.: `CarPropertyManager`
  (dados/sensores), `CarPowerManager` (energia), `CarUxRestrictionsManager` (segurança).
  Você pede o gerente certo pela classe `Car`.

- **CarPropertyManager** — o gerente que **lê, escreve e assina** os dados do veículo. É por ele
  que o app pega velocidade ou muda a temperatura.

- **Propriedade (property)** — **um dado/controle do carro**, com um nome fixo. Ex.:
  `PERF_VEHICLE_SPEED` (velocidade), `GEAR_SELECTION` (marcha), `HVAC_TEMPERATURE_SET`
  (temperatura do ar). Analogia: cada propriedade é **um mostrador ou um botão** do painel.

- **`VehiclePropertyIds`** — a **lista de nomes** oficiais dessas propriedades (o "cardápio" do
  que dá pra pedir).

- **Zona / `areaId`** — algumas propriedades existem **por lugar** do carro: a temperatura do
  **motorista** é diferente da do **passageiro**; a pressão é **por pneu**. A "zona" (areaId) diz
  **qual lugar**. Analogia: "aumenta o volume" (global) vs. "aumenta a temperatura **do lado do
  motorista**" (por zona). Pedir global uma coisa que é por zona = erro clássico.

- **HAL (Hardware Abstraction Layer)** — a **camada que padroniza o hardware**. O Android fala uma
  língua só; a HAL "traduz" pro hardware específico. Analogia: uma **tomada padrão** — o aparelho
  não precisa saber como a usina gera energia.

- **VHAL (Vehicle HAL)** — a HAL **do veículo**: o "contrato" que a montadora implementa dizendo
  quais propriedades existem, se são de **ler/escrever**, e como mudam. Analogia: a **ficha
  técnica de cada mostrador/botão** do carro. Seu app confia nesse contrato.

- **changeMode** — como uma propriedade **muda**: `STATIC` (nunca muda, ex.: número do chassi),
  `ON_CHANGE` (muda de vez em quando, ex.: marcha), `CONTINUOUS` (muda o tempo todo, ex.:
  velocidade — vem num "fluxo" com uma taxa de amostragem).

- **VIN (Vehicle Identification Number)** — o **número do chassi**, a "impressão digital" única do
  carro. É um exemplo de dado `STATIC` (não muda).

- **PTO (Power Take-Off)** — uma **saída de força** do motor usada em veículos de trabalho
  (ligar uma betoneira, um guincho). Aparece como exemplo de propriedade **específica da
  montadora**. Você não precisa saber usar — é só um exemplo de "dado que só alguns carros têm".

- **EV (Electric Vehicle)** — **carro elétrico**. Ex.: `EV_BATTERY_LEVEL` = nível da bateria.

- **callback / assinar (subscribe)** — em vez de perguntar "qual a velocidade?" toda hora, você
  **deixa um recado**: "me avise quando mudar". O carro te chama de volta (callback). Analogia:
  **assinar uma notificação** em vez de checar o app o tempo todo.

- **leak (vazamento) / desregistrar** — se você "assina" e **nunca cancela**, o app continua
  ouvindo pra sempre e gasta memória/bateria — um **vazamento**. No carro isso é grave (fica
  ligado horas). Sempre **desregistrar** o callback quando a tela some. Analogia: **desligar a
  torneira** depois de usar.

## Telas e segurança dirigindo

- **Car App Library** — biblioteca pra fazer apps de carro **sem desenhar pixel**: você descreve
  **o quê** mostrar (uma lista, um mapa) e o **carro desenha**. Analogia: você entrega um
  **formulário preenchido** e o carro imprime no padrão dele. Vantagem: o mesmo app roda no AAOS
  **e** no Android Auto, e já sai seguro pra dirigir.

- **Template (modelo)** — um **molde de tela pronto** (lista, painel, navegação). Você escolhe o
  molde e preenche; não inventa layout do zero. Ex.: `ListTemplate` (lista), `NavigationTemplate`
  (mapa+rota), `PaneTemplate` (um painel de detalhes).

- **host** — o **programa do carro que desenha** seus templates na tela. Analogia: a **gráfica**
  que recebe seu formulário e imprime. Ele também **impõe as regras de segurança**.

- **`CarAppService` / `Session` / `Screen` / `ScreenManager`** — as peças da Car App Library:
  `CarAppService` é a **porta de entrada** do app; `Session` é a **conversa** com o host; `Screen`
  é **uma tela** (que devolve um Template); `ScreenManager` é a **pilha** de telas (empilha/volta,
  como as telas que você abre e fecha).

- **POI (Point of Interest) / Ponto de Interesse** — um **lugar no mapa** (posto, restaurante,
  carregador). "Lista de POIs" = lista de lugares. No app, tocar num POI abre detalhe e navega.

- **CarUxRestrictions (Car UX Restrictions)** — as **regras de segurança** que o carro impõe à sua
  tela **enquanto anda** (menos texto, sem teclado, sem vídeo) pra não distrair o motorista. "UX" =
  *User Experience* (experiência do usuário). Analogia: um **"modo dirigindo"** que simplifica tudo.

- **driving state (estado de direção)** — em que situação o carro está: `PARKED` (estacionado),
  `IDLING` (parado com motor ligado), `MOVING` (andando). A tela muda conforme o estado.

- **NHTSA** — a **agência de trânsito dos EUA** (National Highway Traffic Safety Administration).
  Ela publica as regras de "quanto a tela pode distrair" (olhar de ~2 segundos). É a **origem legal**
  das CarUxRestrictions. Você não interage com ela; é o "porquê" das regras.

## Cara da marca (white-label)

- **White-label ("marca branca")** — **um app, várias marcas**. O mesmo código vira o app da
  montadora A ou da B só trocando cores/logo. Analogia: a **mesma camiseta lisa** que ganha a
  estampa de cada time. Nenhuma marca fica "chumbada" no código.

- **Design tokens** — os **valores de estilo com nome** (cor primária, fundo, fonte) guardados como
  **dado**, não espalhados no código. Analogia: uma **paleta de tintas rotulada**; troca a paleta,
  troca o visual inteiro.

- **hex hardcoded (cor "chumbada")** — escrever a cor crua no meio da tela (ex.: `0xFF0066CC`) em
  vez de usar um token. É ruim porque **ignora a troca de marca**. Regra: **nunca** cor crua; sempre
  o token.

- **car-ui-lib** — biblioteca de **componentes de tela do sistema** do carro (listas, botões no
  jeitão do carro) que a montadora consegue **repintar**.

- **RRO (Runtime Resource Overlay)** — um jeito de a montadora **trocar cores/imagens/estilos em
  tempo de execução, sem recompilar** o app. "Runtime" = enquanto roda; "overlay" = uma **camada por
  cima**. Analogia: **um filtro/capa** que você põe sobre o app pronto e ele muda de cara.

- **flavor (product flavor)** — uma **variação do mesmo app** que o Gradle gera. Aqui, **1 flavor por
  marca** (slate/aurora/ember/nord). Analogia: **sabores** do mesmo sorvete-base. No Android Studio
  aparecem em "Build Variants".

- **`BuildConfig.DEFAULT_BRAND`** — uma **constante gerada na hora de compilar** que diz **qual marca**
  este build é. Cada flavor seta um valor. O app lê isso pra escolher a paleta.

- **dynamic theming (tema dinâmico)** — **re-pintar a tela toda** trocando o conjunto de tokens, sem
  mexer no layout. Troca o token → muda cor/fonte em tudo.

## Arquitetura e testes

- **Clean Architecture (arquitetura limpa)** — organizar o código em **camadas** onde a de dentro
  (as regras) **não conhece** a de fora (Android, o carro). Analogia: a **cozinha** (regras) não
  precisa saber qual **garçom** (tela) ou **fornecedor** (carro) está usando. Deixa testar sem o carro.

- **MVVM (Model–View–ViewModel)** — um padrão de tela: a **View** (tela) só mostra; o **ViewModel**
  prepara o estado; o **Model** são os dados. A tela não fala direto com o carro — fala com o
  ViewModel. Analogia: **garçom (View)** ↔ **cozinheiro que monta o prato (ViewModel)** ↔
  **ingredientes (Model)**.

- **StateFlow** — um **valor que "avisa" quando muda** (fluxo de estado). A tela "assina" e se
  redesenha sozinha quando o valor novo chega. Analogia: um **placar** que todo mundo vê atualizar.

- **CarRepository (repositório)** — no projeto, a **interface** (contrato) que diz "é assim que se
  pega os dados do carro", sem dizer *como*. Tem duas implementações: a **real** (`CarPropertyRepository`,
  fala com o carro) e a **falsa** (`FakeCarRepository`, inventa dados). Trocar uma pela outra **não
  muda a tela** — é o poder da Clean Architecture.

- **Fake (dublê)** — uma implementação **de mentira** usada pra rodar/testar **sem o carro**.
  Analogia: um **manequim** pra provar a roupa sem precisar da pessoa.

- **JVM (Java Virtual Machine)** — o "motor" que roda código Java/Kotlin **no computador**, sem
  celular nem carro. "Teste na JVM" = teste **rápido, na sua máquina**.

- **JUnit** — a biblioteca padrão pra **escrever testes** em Java/Kotlin.

- **Robolectric** — roda testes que **precisam do Android** direto na JVM (sem emulador). Analogia:
  um **Android de brinquedo** só pra o teste.

- **Paparazzi** — ferramenta de **snapshot de tela**: desenha a tela na JVM e salva uma **foto**.

- **snapshot test (teste de foto) / golden** — tira uma **foto** da tela e compara com a foto
  aprovada (o **golden**, a "foto-gabarito"). Se um pixel mudar sem querer, o teste falha → pega
  **regressão visual**. Analogia: um **"achou o erro entre as duas figuras"** automático.

- **i18n / l10n** — **internacionalização** (i-18-letras-n) e **localização** (l-10-letras-n):
  fazer o app se adaptar a idioma/região (ex.: **km/h** no Brasil, **mph** nos EUA) sem `if` no
  código. Analogia: o mesmo cardápio **traduzido** conforme o país.

- **RTL (Right-To-Left)** — idiomas que se **leem da direita pra esquerda** (árabe, hebraico). A tela
  precisa **espelhar** o layout. Bom testar isso também.

- **CI/CD (Continuous Integration / Continuous Delivery)** — robôs que **testam e publicam** o app
  automaticamente a cada mudança. **Este projeto não usa** CI/CD (por escolha) — os testes rodam com
  `./gradlew test` na mão.

- **cluster** — o **painel de instrumentos** (velocímetro, marcha, bateria) na frente do motorista.
  A tela `dashboard` do app é um cluster.

- **HUD (Head-Up Display)** — visual estilo painel/projeção (números grandes, alto contraste). Aqui
  é só o **estilo** da tela do cluster, não um projetor de verdade.

- **HVAC (Heating, Ventilation, Air Conditioning)** — o **ar-condicionado / climatização** do carro
  (aquecer, ventilar, refrigerar). A tela "Clima" controla o HVAC.

- **garage mode (modo garagem)** — uma janela em que o carro, **desligado**, **acorda sozinho** pra
  fazer manutenção (baixar update, subir logs). Analogia: o carro fazendo **tarefas de madrugada**,
  sem atrapalhar quem dirige.

- **CarPowerManager** — o gerente de **energia** do carro (ligando, "vou dormir", suspenso). Avisa o
  app pra **salvar o estado** antes de desligar.

- **AVD (Android Virtual Device) / emulador** — um **carro/celular de mentira** que roda no seu
  computador pra testar o app. "AVD Automotive" = um head unit simulado.

- **ADB (Android Debug Bridge)** — a **ferramenta de linha de comando** pra conversar com o
  emulador/aparelho (instalar app, tirar print). É o "controle remoto" do device pelo terminal.

## Permissões (o que o app pode tocar)

- **Permissão** — autorização pra o app fazer algo sensível (ler velocidade, mexer no ar).
  No carro, **declarar no manifesto não basta** — algumas exigem privilégio.

- **AndroidManifest.xml** — o **"documento de identidade" do app**: declara nome, permissões,
  telas. O carro lê isso pra saber o que o app pede.

- **signature (permissão por assinatura)** — permissão que só é dada a apps **assinados com a mesma
  chave** do sistema. Analogia: só entra quem tem o **crachá emitido pela fábrica**.

- **platform key (chave da plataforma)** — a **chave secreta** com que a montadora assina o sistema.
  App assinado com ela é "de dentro".

- **privileged / privapp-allowlist (app privilegiado / lista de permitidos)** — apps de sistema numa
  **lista aprovada** pela montadora, instalados numa pasta especial, que podem tocar coisas sensíveis
  (controlar o ar, portas). Analogia: a **lista VIP** na portaria. `signature|privileged` = precisa da
  chave **ou** estar nessa lista.

- **3rd-party (app de terceiro)** — app de fora, feito por quem **não é** a montadora. Costuma ter
  acesso só a leituras básicas; controle sensível depende de acordo com a OEM.

- **`SecurityException`** — o erro que estoura quando o app tenta algo **sem permissão**. No carro,
  quase sempre significa **falta de assinatura/lista** — **não** bug no seu código.

---

*Não decore. Cada doc do curso (01–10) manda você de volta aqui quando aparece uma sigla nova.*
