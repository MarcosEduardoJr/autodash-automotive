# 09 · White-label, dynamic theming & responsivo

## 🧒 Em miúdos

Pense numa fábrica de camisetas. A camiseta lisa é sempre a mesma; o que muda é a **estampa** que você prende nela — um time hoje, outro amanhã. Aqui o **app é a camiseta lisa** e a "marca" (as cores, o logo, o nome da montadora) é a **estampa**. Você escreve o programa **uma vez** e, na hora de montar, escolhe qual estampa entra. Nenhum time nem marca fica costurado dentro do pano.

A segunda ideia do capítulo é parecida com redimensionar uma janela no computador: a tela do carro vem em formatos bem diferentes (umas largas e deitadas, outras altas e em pé), então o mesmo app precisa se **ajeitar ao espaço** que recebe, do jeito que um texto reflui quando você aumenta a janela.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md).

## White-label: a marca é DADO, não código

**White-label** ("marca branca" — um app que vira várias marcas só trocando cor/logo, como a mesma camiseta lisa que ganha a estampa de cada time) significa que **o mesmo binário** (o mesmo app já compilado) **vira qualquer marca**. A identidade — o "rosto" da montadora — não está espalhada pelo código; ela mora num único lugar, o `BrandTokens`
([`core/designsystem/Brand.kt`](../core/designsystem)): cor primária/secundária, fundo,
superfície, nome, logo. Esses são os **design tokens** (valores de estilo com nome guardados como **dado**, não cor crua espalhada pela tela — como uma paleta de tintas rotulada: troca a paleta, troca o visual inteiro).

O ponto-chave: **nenhuma tela** conhece uma **OEM** (Original Equipment Manufacturer — jargão para **a montadora**, tipo Volvo ou Toyota; sempre que ler "OEM", leia "a montadora"). Em vez de saber "sou o app da Volvo", cada tela só lê `MaterialTheme.colorScheme`
+ `LocalBrandTokens` — ou seja, ela pega a cor "primária" da paleta atual, sem nunca perguntar de quem é a marca. Por isso as marcas de exemplo têm nomes **neutros** de propósito (Slate/Aurora/Ember/Nord): são estampas fictícias, nenhuma montadora real fica "chumbada" no código.

- **Padrão por build:** `BuildConfig.DEFAULT_BRAND` (uma constante que o compilador gera dizendo **qual marca é este build** — o app lê esse valor para escolher a paleta). Em produção a conta é simples: **1 OEM = 1 flavor**. Um **flavor** (product flavor — uma variação do mesmo app que o **Gradle**, a ferramenta que monta o app, gera; aqui, 1 por marca, como sabores do mesmo sorvete-base) define esse valor e traz os recursos/**RRO** daquela marca. É o "multi-flavor/multi-brand build" — repare que **não existe `if (marca)`** espalhado pelas telas; a marca é escolhida no momento de montar, não no meio da lógica.
- **Dynamic theming:** **tema dinâmico** é **re-pintar a tela toda** trocando o conjunto de tokens, sem mexer no layout. Trocar o `BrandTokens` re-tematiza tudo (`AutoDashTheme`, o tema que aplica esses tokens) — **sem recompilar telas**. Como o app é white-label, ele **entrega UMA marca por build** (`BuildConfig.DEFAULT_BRAND`); não há seletor de marca para o usuário final — o motorista não escolhe a estampa, ela já vem escolhida de fábrica. O poder multi-brand (provar que o mesmo código serve a várias marcas) é demonstrado pelos **snapshots** das 4 marcas (as fotos automáticas da tela, explicadas mais abaixo).
- **RRO** (Runtime Resource Overlay — um jeito de a montadora **trocar cores/imagens/estilos enquanto o app roda, sem recompilar**; "runtime" = enquanto executa, "overlay" = uma camada por cima, como um filtro/capa que muda a cara do app pronto) da OEM ([docs/06](06-multi-brand-rro.md)) sobrepõe por cima, também em runtime.

## Responsivo: portrait × landscape

"Portrait" é a tela **em pé** (mais alta que larga) e "landscape" é a tela **deitada** (mais larga que alta) — os mesmos termos de foto de celular. O
`DashboardScreen` (a tela do painel) decide o layout por **medida real** — ele **mede o espaço que recebeu** em vez de chutar — usando `BoxWithConstraints` (uma ferramenta do **Compose**, o mesmo kit de telas que você já usa no Android de celular, que informa quanto espaço há disponível) + a função `isWide()` (um ajudante que responde "essa tela é larga?"):

- **landscape/wide (deitada/larga):** velocidade grande à esquerda, tiles (os quadradinhos de informação) em coluna à direita.
- **portrait (em pé):** velocidade em cima, tiles empilhados abaixo.

**Head units** (o head unit é a **tela + computador do painel** do carro — o "celular embutido no carro") são heterogêneos: uns têm um **cluster** (o **painel de instrumentos** na frente do motorista, com velocímetro e marcha) largo e deitado, outros têm telas centrais em retrato. Como você não controla qual formato vai encontrar, **responsivo não é opcional** — o app tem que caber em todos.

## Testes (sem CI/CD)

Aqui não há **CI/CD** (Continuous Integration / Continuous Delivery — robôs que testam e publicam o app a cada mudança); **este projeto não usa** por escolha, então os testes rodam na mão com o comando `./gradlew`.

- **Unit (JVM):** teste **de unidade** rodando na **JVM** (Java Virtual Machine — o "motor" que executa Kotlin **no seu computador**, sem celular nem carro; por isso é rápido). São `ResponsiveTest` (verifica a função `isWide`) e `BrandTest` (verifica o registro/ciclo de marcas — se as estampas estão todas cadastradas e válidas).
- **Snapshot multi-brand (Paparazzi):** um **snapshot test** tira uma **foto** da tela e compara com a foto aprovada (o **golden**, a "foto-gabarito"); se um pixel muda sem querer, o teste falha e pega a **regressão visual** (um estrago visual acidental) — é um "achou o erro entre as duas figuras" automático. **Paparazzi** é a ferramenta que desenha a tela na JVM e salva essa foto. O `DashboardSnapshotTest` renderiza o dashboard em
  **cada marca** (landscape) + **portrait**, na JVM, sem device. Os goldens (fotos-gabarito) ficam em
  `feature/dashboard/src/test/snapshots/`. O comando `./gradlew :feature:dashboard:verifyPaparazziDebug`
  barra regressão visual entre marcas — exatamente o "snapshot testing for multi-brand UI
  consistency" (garantir que todas as marcas continuam com a cara certa). Para regenerar as fotos-gabarito depois de uma mudança proposital: `recordPaparazziDebug`.

### Palavras novas deste capítulo

- [White-label](00-glossario.md) — um app, várias marcas trocadas
- [OEM](00-glossario.md) — a montadora / fabricante do carro
- [design tokens](00-glossario.md) — valores de estilo guardados como dado
- [BuildConfig.DEFAULT_BRAND](00-glossario.md) — constante que diz a marca do build
- [flavor](00-glossario.md) — variação do app por marca
- [RRO](00-glossario.md) — troca visual em runtime, sem recompilar
- [dynamic theming](00-glossario.md) — re-pintar tudo trocando os tokens
- [head unit](00-glossario.md) — tela e computador do painel
- [cluster](00-glossario.md) — painel de instrumentos do motorista
- [JVM](00-glossario.md) — motor que roda Kotlin no computador
- [Paparazzi](00-glossario.md) — ferramenta que fotografa a tela
- [golden / snapshot](00-glossario.md) — foto-gabarito comparada contra regressão
- [regressão visual](00-glossario.md) — pixel mudou sem querer
- [CI/CD](00-glossario.md) — robôs que testam e publicam
