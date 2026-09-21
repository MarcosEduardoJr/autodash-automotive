# 08 · Testar AAOS & distribuir

## 🧒 Em miúdos

Você fez um app que roda dentro de um carro — mas não tem um carro em cima da sua mesa. Então como saber se ele funciona? A ideia central deste capítulo é simples: dá pra testar quase tudo **sem o carro de verdade**, usando "dublês" e um carrinho de brinquedo dentro do seu computador, do mesmo jeito que um cozinheiro prova o prato na cozinha antes de mandar pro salão. Só bem no fim você confere no carro real. E, quando o app estiver pronto, você o coloca na "loja de aplicativos do carro" pra ele chegar nas pessoas.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md).

## Testar sem (e com) o veículo

Testar app de carro parece assustador porque o carro é caro e difícil de conseguir. O truque é pensar nos testes como uma **pirâmide**: embaixo, muitos testes **rápidos e baratos** que rodam na sua própria máquina; no topo, poucos testes **lentos e caros** que precisam de hardware. Você passa quase todo o tempo na base e raramente precisa do carro real.

| Nível | Como |
|---|---|
| Unit (JVM) | **Fake** do `CarRepository` — rápido, sem device. Ver `domain/` |
| Integração | Robolectric / test doubles da car-lib |
| Sistema | **Emulador automotivo** (system image Automotive) injeta velocidade/marcha |
| Hardware | O veículo real: validação final |

Lendo a tabela de cima pra baixo, do mais barato ao mais caro:

- **Unit (JVM):** o teste mais barato de todos. **JVM** (Java Virtual Machine — o "motor" que roda Kotlin/Java direto no seu computador, sem celular nem carro) executa o teste em segundos. Aqui você usa um **Fake** (dublê — uma implementação "de mentira" que inventa dados, como um manequim pra provar a roupa sem precisar da pessoa) no lugar do **`CarRepository`** (o contrato do projeto que diz "é assim que se pega os dados do carro", sem dizer de onde vêm). Trocar o carro de verdade por esse dublê é o que deixa o teste rodar sem nenhum aparelho — por isso "Ver `domain/`", a camada de regras que nem conhece o Android.

- **Integração:** um degrau acima. Alguns testes precisam de pedaços do Android, não só de Kotlin puro. Aí entra o **Robolectric** (um "Android de brinquedo" que roda dentro da própria JVM, sem precisar de emulador — continua rápido mesmo dependendo do Android) e os **test doubles** (dublês de teste) da **car-lib** (a biblioteca que fala com o carro).

- **Sistema:** agora sim um "carro de mentira" inteiro. O **Emulador automotivo**, também chamado **AVD** (Android Virtual Device — um head unit, ou seja, o computador do painel, simulado dentro da sua máquina), inicia a partir de uma **system image Automotive** (a "imagem" pronta do sistema do carro que você baixa pra dentro do emulador). Nele você consegue **injetar** velocidade e marcha na mão pra ver o app reagir.

- **Hardware:** o carro real. Caro e lento, então fica pro fim: serve só como **validação final**, a última prova antes de servir.

No emulador, injete **velocidade > 0** pelos *extended controls* (o painelzinho de controles extras do emulador, onde você "finge" que o carro está andando) para exercitar `CarUxRestrictions` — as **CarUxRestrictions** (Car UX Restrictions — as regras de segurança que o carro impõe à sua tela **enquanto anda**: menos texto, sem teclado, sem vídeo, pra não distrair o motorista). Para lógica pura, esconda a **Car API** (o conjunto de comandos que o Android dá pro seu app falar com o carro) atrás de `CarRepository` e use fake.

## Mapa de testes deste projeto

A tabela abaixo é o mapa dos testes reais deste repositório: cada linha é **um arquivo de teste**, qual **tipo** de teste ele é, e qual conceito de app-de-carro ele prova. Antes de ler, um mini-dicionário dos termos que aparecem nela:

- **AAOS** (Android Automotive OS) — o Android que **é** o próprio carro, instalado no painel.
- **MVVM** (Model–View–ViewModel) — a tela (View) só mostra, o ViewModel prepara o estado, o Model são os dados.
- **VM** — apelido de **ViewModel**, a peça que prepara o estado da tela.
- **StateFlow** — um valor que "avisa" quando muda; a tela assina e se redesenha sozinha.
- **coroutines / corrotinas** — o jeito do Kotlin de rodar tarefas que "esperam" (ler um sensor, baixar algo) **sem travar a tela**. Nos testes, o "tempo" delas vira um **relógio de mentira** que você adianta na mão, em vez de esperar segundos de verdade.
- **coroutines-test** — o pacotinho de teste que dá esse relógio controlável (`StandardTestDispatcher`, `runCurrent()`…), pra o teste rodar num piscar de olhos.
- **HVAC** (Heating, Ventilation, Air Conditioning) — o ar-condicionado / climatização do carro.
- **snapshot / golden** — teste de "foto": desenha a tela, tira uma foto e compara com a foto-gabarito aprovada (o **golden**); se um pixel muda sem querer, o teste falha.
- **Paparazzi** — a ferramenta que desenha a tela na JVM e salva essa foto.
- **gating** — "travar/desligar" um controle; aqui os testes provam que o botão fica apagado quando deve ficar.
- **clamp (grampear/limitar)** — **prender** um número dentro de uma faixa. Ex.: a ventilação só vai de 0 a 6 — pediu 9, o clamp corta pra 6; a temperatura fica entre 16 e 28.
- **Template** — um molde de tela pronto (lista, painel, mapa) que o **carro** desenha por você.
- **host** — o programa do carro que desenha esses templates e impõe as regras de segurança.
- **POI** (Point of Interest / Ponto de Interesse) — um lugar no mapa (posto, restaurante, carregador).
- **i18n** — internacionalização: o app se adapta a idioma/região (ex.: **km/h** no Brasil, **mph** nos EUA) sem `if` no código.
- **locale** — o **idioma/região** configurado no aparelho (pt-BR, en-US). É ele que decide mostrar **km/h** ou **mph** — é a "chavinha" que o i18n lê.
- **landscape / portrait** — tela **deitada** (larga) / **em pé** (alta). O painel do carro é quase sempre landscape; o teste de foto confere os dois.
- **imperial** — o **sistema de medidas dos EUA** (milhas, mph), oposto do **métrico** (km, km/h). "inglês/imperial" na tabela = idioma inglês somado às medidas americanas.

| Arquivo | Tipo | O que prova (e o conceito AAOS) |
|---|---|---|
| `core/model/.../UnitsTest`, `ClimateTest` | Unit puro (JVM) | i18n (km/h×mph por locale) e o modelo `Climate`/`temp(seat)` — anel interno, testável sem device |
| `domain/.../ObserveVehicleSpeedTest` | Unit | use case sobre o `CarRepository` (sem `android.car`) |
| `feature/dashboard/.../DashboardViewModelTest` | Unit (coroutines-test) | **MVVM**: o VM mapeia sinais do repo → `StateFlow`. Usa `runCurrent()` (não `advanceUntilIdle`) porque os fluxos do fake são `while(true){emit;delay}` (infinitos) |
| `feature/climate/.../ClimateViewModelTest` | Unit (coroutines-test) | HVAC por zona: `togglePower/toggleAc/fan/delta`, clamps (fan 0..6, temp 16..28), zona independente. `WhileSubscribed` exige coletor ativo no teste |
| `feature/dashboard/.../DashboardSnapshotTest` | **Snapshot** (Paparazzi) | regressão visual: 4 marcas landscape + portrait + inglês/imperial + tema claro |
| `feature/climate/.../ClimateSnapshotTest` | **Snapshot** (Paparazzi) | clima por marca + **gating**: `climate_power_off` e `climate_ac_off` congelam o estado desabilitado/apagado |
| `feature/carapp/.../PoiScreenTest` | **Template** (Robolectric + `androidx.car.app:app-testing`) | `ListTemplate`→`PaneTemplate`→`NavigationTemplate` sem host |

Padrão de VM em teste: `Dispatchers.setMain(StandardTestDispatcher())` no `@Before`, `resetMain()`
no `@After`. Rodar tudo: `./gradlew test`; regravar goldens: `./gradlew recordPaparazziDebug`.

Traduzindo esse padrão pro português: todo teste de **VM** (ViewModel) troca o "relógio" das corrotinas por um relógio de teste que **você** controla (`Dispatchers.setMain(...)` no começo, com `@Before`; `resetMain()` no fim, com `@After`). Aliás, `@Before` e `@After` são marcações do **JUnit** (a biblioteca padrão de testes) que dizem "rode isto **antes**" e "rode isto **depois**" de cada teste. Assim o teste não fica parado esperando tempo real passar — ele adianta o relógio na mão. Já os comandos do final são atalhos do dia a dia: `./gradlew test` roda todos os testes de uma vez, e `./gradlew recordPaparazziDebug` **regrava os goldens** (as fotos-gabarito) quando a mudança visual foi de propósito e você quer aprovar o novo visual.

Dois detalhes finos que aparecem na tabela, em linguagem simples:

- Os fluxos do fake são **infinitos** — `while(true){ emit; delay }` quer dizer "emite um valor, espera um tempinho e repete pra sempre", imitando um sensor do carro que nunca para. Por isso os testes usam `runCurrent()` (que roda só o que já está pronto **agora**) em vez de `advanceUntilIdle()` (que tentaria esperar tudo "terminar" — e um fluxo infinito nunca termina, então o teste travaria).
- `WhileSubscribed` significa que aquele `StateFlow` só "liga" enquanto **alguém está ouvindo**. No teste, isso exige um **coletor ativo** (alguém coletando o fluxo de verdade), senão nenhum valor chega e o teste parece "vazio".

## Distribuição

Testado, falta **publicar**. Um app de carro precisa avisar em dois lugares que ele é "de carro" — senão a loja nem o mostra no veículo.

- Manifest: `<uses-feature android:name="android.hardware.type.automotive" android:required="true"/>`.
  - Traduzindo: no **AndroidManifest.xml** (o "documento de identidade" do app, onde ele declara o que é e o que pede), essa linha `uses-feature` diz "eu **exijo** um aparelho do tipo automotivo". É o que faz a loja entender que o destino dele é o carro.
- **`automotive_app_desc.xml`** declara a **categoria** (`template`/`media`/…), referenciado
  por `<meta-data android:name="com.android.automotive">`.
  - Traduzindo: esse arquivo é o **descriptor** ("descritor"). Ele diz **que tipo** de app de carro ele é — de **template** (telas-molde), de **media** (música/áudio), etc. O `<meta-data>` é só o "aviso" dentro do manifesto apontando pra esse arquivo.
- A Play tem um **track de form factor automotivo**, com revisão de distração mais rígida.
  - Traduzindo: na **Play** (a Play Store) existe uma "faixa" separada (**track**) só pro formato carro (**form factor** = tipo de aparelho: celular, tablet, carro…). A revisão dela é mais dura porque checa se o app **distrai** o motorista.

Sem o descriptor + uses-feature, a Play **não distribui** no carro. Ver `app/src/main/README.md`.

### Palavras novas deste capítulo

- [AAOS](00-glossario.md) — o Android que é o próprio carro.
- [JVM](00-glossario.md) — motor que roda código no computador.
- [JUnit](00-glossario.md) — biblioteca padrão pra escrever testes.
- [Fake](00-glossario.md) — dublê de mentira pra testar.
- [CarRepository](00-glossario.md) — contrato pra pegar dados do carro.
- [Robolectric](00-glossario.md) — Android de brinquedo na JVM.
- [AVD / emulador](00-glossario.md) — carro de mentira no computador.
- [Car API](00-glossario.md) — comandos pro app falar com o carro.
- [CarUxRestrictions](00-glossario.md) — regras de segurança da tela andando.
- [MVVM](00-glossario.md) — tela, preparador de estado e dados.
- [StateFlow](00-glossario.md) — valor que avisa quando muda.
- [HVAC](00-glossario.md) — ar-condicionado e climatização do carro.
- [Paparazzi](00-glossario.md) — tira foto da tela na JVM.
- [snapshot / golden](00-glossario.md) — compara tela com foto-gabarito aprovada.
- [Template](00-glossario.md) — molde de tela pronto do carro.
- [host](00-glossario.md) — programa do carro que desenha templates.
- [POI](00-glossario.md) — lugar no mapa (posto, restaurante).
- [i18n / l10n](00-glossario.md) — adaptar app a idioma/região.
