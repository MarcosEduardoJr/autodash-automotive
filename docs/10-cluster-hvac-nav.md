# 10 · Cluster polido, HVAC por zona e navegação

## 🧒 Em miúdos

Pense no painel de um carro como o painel de um avião: na frente do piloto ficam os **mostradores** (velocidade, combustível) e, ao lado, os **botõezinhos** que ligam o ar, o rádio, o mapa. Este capítulo é sobre construir esses dois pedaços na tela do carro: o "mostrador" (velocímetro, marcha, bateria) e os "botões do ar-condicionado".

Tem um detalhe importante de bom senso: no ar-condicionado de casa, o **motorista** pode querer 22°C e o **passageiro** 25°C ao mesmo tempo — o carro trata cada assento como um "cômodo" separado. E, como no rádio do carro, quando você **desliga no botão geral**, todos os controles ficam apagados e "mortos" — não adianta apertar. É isso que a gente vai montar aqui, começando do zero e explicando cada sigla.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md).

## Cluster (feature/dashboard)

Primeiro, uma palavra nova: **cluster** (o *painel de instrumentos* — aquele conjunto de mostradores bem na frente do motorista: velocímetro, marcha, bateria). No nosso app, quem faz esse papel é a tela `dashboard` (ver [`feature/dashboard`](../feature/dashboard)).

- **Speedometer** (o *velocímetro*) desenhado com **needle** (o *ponteiro* que gira) e um arco, feitos "na mão" dentro de um **`Canvas`** (uma *folha em branco* do Jetpack Compose onde você desenha formas pixel a pixel, em vez de usar componentes prontos). Ele tem **animação** de aceleração via **`animateIntAsState`** (uma função do Compose que, quando o número muda de 40 para 60, faz o valor *deslizar suavemente* entre os dois em vez de pular seco) — o número e o ponteiro escorregam juntos. Em volta há um anel de bateria, um seletor **P-R-N-D** (as marchas: *Park, Reverse, Neutral, Drive*) e cards de autonomia, temperatura e modo. A fonte é a Chakra Petch, escolhida para dar o clima de **HUD** (Head-Up Display — o *estilo* de painel/projeção: números grandes, alto contraste, aquela cara de cockpit; aqui é só a aparência da tela, não um projetor de verdade no para-brisa).
- **Tema dia/noite:** a função `AutoDashTheme(tokens, dark)` troca o visual entre claro e escuro. O modo escuro usa os **tokens** da marca (os **design tokens** são os *valores de estilo com nome* — cor primária, fundo, fonte — guardados como dado, não escritos soltos no meio da tela); o modo claro usa neutros claros + a cor de destaque (accent). Em produção, essa escolha claro/escuro seguiria a propriedade **NIGHT_MODE** do veículo (um *dado que o próprio carro informa* dizendo se está de dia ou de noite, para o painel não cegar o motorista à noite).

## Clima (feature/climate)

Agora os "botões do ar". A sigla oficial é **HVAC** (Heating, Ventilation, Air Conditioning — em bom português, a *climatização* do carro: aquecer, ventilar, refrigerar). A tela "Clima" controla o HVAC.

O ponto-chave é que aqui o ar é **por zona**. Lembra da analogia do motorista querendo 22°C e o passageiro 25°C? No carro, "temperatura" não é um valor só: cada assento é um **lugar** diferente. A gente fala com o carro através da **Car API** (o *"balcão de atendimento"* do carro — o conjunto de comandos por onde o app pede coisas ao veículo em vez de mexer no hardware direto; só existe no **AAOS**, o *Android Automotive OS*, a versão do Android que roda dentro do próprio carro). Pela Car API, lemos e escrevemos a propriedade **`HVAC_TEMPERATURE_SET`** (uma **propriedade** é *um dado ou controle do carro com nome fixo* — aqui, "a temperatura definida do ar"), e cada valor vem marcado por uma **`area`** (a **zona** / `areaId` diz *a qual lugar* aquele dado pertence: o lado do motorista, o do passageiro). Pedir global uma coisa que é por zona é o erro clássico de quem vem do celular.

Quando o usuário toca em +/-, o app **escreve** o novo valor com **`setProperty`** (o comando da Car API que *manda o carro mudar* aquele dado — o oposto de só ler). Escrever no ar exige a permissão **`CONTROL_CAR_CLIMATE`** (uma **permissão** é a *autorização para o app fazer algo sensível*; mexer no clima é sensível, então não basta querer — o carro precisa liberar). Ver [`feature/climate`](../feature/climate).

## Navegação (chrome no app)

Aqui "navegação" não é o mapa/GPS — é o **chrome** do app, gíria de interface para *as bordas fixas que emolduram o conteúdo* (a barra de cima, as abas, os cantos que não mudam quando você troca de tela).

O módulo `app/` (ver [`app`](../app)) monta essa moldura: uma barra com a marca + as abas **CLUSTER / CLIMA**. As telas em si são **headerless** (*sem cabeçalho próprio*: cada tela desenha só o seu conteúdo e deixa a barra/abas por conta da moldura, para não repetir cabeçalho em toda tela). São **duas telas, dois ViewModels, um repositório**. Traduzindo essa frase seca:

- **ViewModel** vem do padrão **MVVM** (Model–View–ViewModel): a *View* (a tela) só mostra; o **ViewModel** é o *cozinheiro que monta o prato* — prepara o estado pronto para exibir; e a tela nunca fala direto com o carro, fala com o ViewModel. Duas telas → dois ViewModels.
- O **repositório** (**CarRepository**) é o *contrato* que diz "é assim que se pegam os dados do carro", sem dizer *como*. Um só serve as duas telas.

E, sobre a marca: como o app é **white-label** (*"marca branca"*: um mesmo código que vira o app da montadora A ou da B só trocando cores e logo — como uma camiseta lisa que ganha a estampa de cada time), **não há seletor de marca** para o usuário final. Cada build já sai com uma marca só (mais sobre isso na última seção).

## Testes (sem CI/CD)

Uma nota antes: **CI/CD** (Continuous Integration / Continuous Delivery — os *robôs que testam e publicam* o app sozinhos a cada mudança) **não é usado aqui**, por escolha; os testes rodam na mão com `./gradlew test`.

- **Unit JVM** (testes *rápidos, na sua máquina* — rodam na **JVM**, a *Java Virtual Machine*, o "motor" que executa Kotlin no computador, sem precisar de celular nem carro): `UnitsTest`, `ResponsiveTest`, `BrandTest`, `PoiTest`, `ObserveVehicleSpeedTest`.
- **Snapshot (Paparazzi):** o **Paparazzi** é uma ferramenta que *desenha a tela na JVM e salva uma foto* dela; o **snapshot test** compara essa foto com uma foto-gabarito aprovada (o **golden**) e, se um pixel mudar sem querer, o teste falha — assim ele pega **regressão visual** (é o "achou o erro entre as duas figuras" automático). Aqui fotografamos: o dashboard das 4 marcas em landscape + portrait + **inglês/imperial** (o app traduzido, com **mph** em vez de km/h) + **tema claro**; e o clima em 2 marcas.
- **Template POI (Robolectric + androidx.car.app:app-testing):** o **Robolectric** *roda testes que precisam do Android direto na JVM*, sem emulador (um "Android de brinquedo" só para o teste). Ele valida a tela de **POI** (Point of Interest — um *ponto de interesse*, um lugar no mapa: posto, restaurante, carregador). O `PoiScreenTest` confere que a `PoiScreen` devolve um **`ListTemplate`** com N itens e que o detalhe é um **`PaneTemplate`** — aqui **Template** é um *molde de tela pronto* da Car App Library (você escolhe o molde e preenche; `ListTemplate` = lista, `PaneTemplate` = painel de detalhes). Ou seja: testa a UI dirigível **sem host de carro**. 21 testes no total, verdes.

## UX / usabilidade (padrões)

"UX" é *User Experience* (experiência do usuário) — como a tela é de usar. Estes são os padrões que deixam o app confortável e seguro na tela do carro:

- **Navegação:** usamos o **`NavigationRail`** (um componente pronto do **Material3**, o *sistema de design do Google* que já traz botões e barras com bom visual e acessibilidade). O `NavigationRail` é uma *barra vertical de navegação na lateral* — o padrão em **head unit** (a *tela+computador do painel* do carro) no formato landscape: dá alvos grandes, ícones + rótulo, e vem com foco/rotativo e **TalkBack** (o *leitor de tela* do Android, para quem não enxerga) de graça. (Antes eram "pills" caseiras — botõezinhos feitos na mão.)
- **Controles:** para POWER (o *botão geral de energia* do painel de clima) e A/C (o *ar-condicionado propriamente dito* — o compressor que gela o ar; dentro do HVAC, é a parte que resfria) usamos **`Switch`** (o *interruptor de liga/desliga*, aquele botãozinho que desliza) com rótulo + status **On/Off** e ícone — feedback claro do que está ligado. Para +/- de temperatura e ventilação usamos **`FilledIconButton`** (um *botão redondo preenchido com um ícone*) de **≥52–64dp** (**dp** = *density-independent pixels*, a unidade de tamanho do Android que fica igual em telas de qualquer densidade; um alvo grande assim é fácil de acertar com o carro em movimento), cada um com **`contentDescription`** (um *texto invisível que descreve o botão* para o leitor de tela ler em voz alta).
- **Estado dependente (o "gating"):** desligar o POWER **desabilita e apaga** as zonas, o A/C e o FAN (a *ventilação*, o quanto o ar sopra) — nada de controle "morto", cinza mas ainda clicável, confundindo o usuário. É exatamente a analogia do rádio: desligou no geral, tudo apaga junto. Os rótulos e o On/Off respeitam **i18n** (*internacionalização*: o app se adapta ao idioma/região sem `if` espalhado no código).

## White-label nos Build Variants

Fechando o assunto da marca única por build. Cada marca é um **product flavor** (**flavor** é uma *variação do mesmo app* que o Gradle gera — como sabores do mesmo sorvete-base; aqui, 1 flavor por marca: `slate`/`aurora`/`ember`/`nord`). Por isso cada marca aparece em **Build Variants** no Android Studio (a *lista, na IDE, de todas as combinações de build que dá para gerar e rodar*) — multiplicada por debug/release. Selecionar o flavor = escolher a marca do binário.

O valor **`BuildConfig.DEFAULT_BRAND`** (uma *constante gerada na hora de compilar* que grava, dentro do próprio app, qual marca este build é) vem do flavor; a UI lê os tokens do design system a partir disso (ver [`core/designsystem`](../core/designsystem) e, para a repintura por cima em runtime, [`docs/06`](06-multi-brand-rro.md)).

### Palavras novas deste capítulo

- [cluster](00-glossario.md) — painel de instrumentos na frente do motorista.
- [HUD](00-glossario.md) — estilo de painel/projeção, números grandes.
- [HVAC](00-glossario.md) — o ar-condicionado/climatização do carro.
- [AAOS](00-glossario.md) — o Android que é o próprio carro.
- [Car API](00-glossario.md) — comandos do app pro carro.
- [propriedade](00-glossario.md) — um dado ou controle do carro.
- [zona / areaId](00-glossario.md) — a qual lugar o dado pertence.
- [permissão](00-glossario.md) — autorização pra ação sensível.
- [head unit](00-glossario.md) — tela e computador do painel.
- [MVVM](00-glossario.md) — padrão View, ViewModel e Model.
- [CarRepository](00-glossario.md) — contrato de acesso aos dados do carro.
- [white-label](00-glossario.md) — um app, várias marcas.
- [design tokens](00-glossario.md) — valores de estilo guardados como dado.
- [POI](00-glossario.md) — ponto de interesse no mapa.
- [Template](00-glossario.md) — molde de tela pronto.
- [Robolectric](00-glossario.md) — Android de brinquedo pra testar na JVM.
- [Paparazzi](00-glossario.md) — tira foto da tela na JVM.
- [snapshot / golden](00-glossario.md) — foto de tela comparada com gabarito.
- [JVM](00-glossario.md) — motor que roda código na máquina.
- [i18n](00-glossario.md) — adaptar o app a idioma/região.
- [CI/CD](00-glossario.md) — robôs que testam e publicam.
- [flavor](00-glossario.md) — variação do mesmo app.
- [BuildConfig.DEFAULT_BRAND](00-glossario.md) — constante que grava a marca do build.
