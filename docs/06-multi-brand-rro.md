# 06 · Multi-brand, theming & RRO

## 🧒 Em miúdos

Imagine uma fábrica que faz **uma única camiseta lisa** e, no fim da linha, carimba o escudo de cada time. A camiseta é sempre a mesma — só a estampa muda. Este capítulo é sobre fazer **um app só** que vira o app de montadoras diferentes trocando cor, logo e estilo por cima, sem reescrever nada. Pense também numa **capa transparente** posta sobre uma foto pronta: a foto continua a mesma, mas ganha outra cara. O segredo é nunca "grudar" a cor de uma marca dentro do código — a cor mora à parte, como uma paleta de tintas que dá pra trocar inteira.

> 📖 Toda sigla deste capítulo está explicada no [glossário](00-glossario.md). Não precisa decorar — volte lá quando aparecer uma sigla nova.

## Um app, várias montadoras

Como o mesmo código veste a marca de cada montadora? A cola é esta:

- A **car-ui-lib** (biblioteca de componentes de tela do próprio sistema do carro — listas, botões etc. — feitos pra montadora conseguir **repintar**) expõe esses componentes prontos. Sobre eles, a **OEM** (Original Equipment Manufacturer — jargão para **a montadora**, tipo Volvo ou Toyota; sempre que ler "OEM", leia "a montadora") aplica um **RRO** (Runtime Resource Overlay — uma **camada por cima** que troca cores, drawables/imagens e estilos **enquanto o app roda**, **sem recompilar**). "Runtime" = enquanto roda; "overlay" = camada sobreposta. É assim que cada marca fica diferente sobre a mesma base — como aquela capa por cima da foto.
- Pra essa capa "colar", seu app precisa ser **overlay-friendly** (amigável ao overlay): ele deve pintar a tela usando **atributos de tema / design tokens** (os valores de estilo — cor primária, fundo, fonte — guardados **com nome, como dado**, e não espalhados no meio do código), e **nunca hex hardcoded**. Um **hex hardcoded** é uma cor "chumbada", escrita crua no código (ex.: `0xFF0066CC`); como ela não é um token, o overlay **não consegue trocá-la** → aquele componente sai "fora da marca". Regra de ouro: sempre o token, nunca a cor crua.
- **Product flavors** por marca (cada **flavor** é uma variação do mesmo app que o **Gradle** — a ferramenta de *build* do Android, ou seja, o programa que **monta e compila** o app — gera; aqui é **1 flavor por marca**) empacotam os recursos e a configuração específicos de cada montadora.

## White-label na prática (este projeto)
**White-label** ("marca branca") é exatamente isto: **um app, várias marcas**, o mesmo código virando o app da montadora A ou da B só trocando cor e logo (a mesma camiseta lisa que ganha a estampa de cada time). Neste projeto, a identidade visual é um `BrandTokens` (o conjunto de **design tokens** da marca — o **design system**, isto é, o catálogo de estilo com nome dessa marca), **não** código espalhado. As marcas são **neutras** de propósito (Slate/Aurora/Ember/Nord) — nenhuma OEM real fica hardcoded ("chumbada"). Ver [`docs/09`](09-whitelabel-responsive.md).

```kotlin
// core/designsystem: a marca é DADO
data class BrandTokens(val id: String, val name: String, val primary: Color, /* … */)

// a tela lê o token, nunca o hex → RRO/tema conseguem sobrepor
Text("...", color = MaterialTheme.colorScheme.primary)

// o app entrega UMA marca por build (white-label). Sem seletor pro usuario:
AutoDashTheme(tokens = Brands.byId(BuildConfig.DEFAULT_BRAND)) { DashboardScreen(state) }
```

Repare no código: a tela pede `MaterialTheme.colorScheme.primary` (o **token** "cor primária"), nunca um número de cor. É isso que dá ao RRO — e ao **dynamic theming** (re-pintar tudo só trocando o conjunto de tokens, sem mexer no layout) — por onde entrar e repintar.

**Produção:** cada OEM = um **flavor** que define a marca padrão via **`BuildConfig.DEFAULT_BRAND`** (uma **constante gerada na hora de compilar** que diz **qual marca** é este build; cada flavor seta um valor, e o app lê essa constante pra escolher a paleta) e traz recursos/RRO daquela montadora. Esse é o mecanismo "multi-flavor/multi-brand build" — o app veste a marca certa **sem nenhum `if (marca)`** espalhado nas telas.

## Snapshot testing
**Snapshot testing** (teste de "foto") é como a gente garante que as várias marcas continuam certas sem abrir cada carro na mão. A ideia: tirar uma **foto** da tela e compará-la com uma foto já aprovada. Quem tira a foto aqui é o **Paparazzi** (uma ferramenta que **desenha a tela na JVM e salva a imagem**); a **JVM** (Java Virtual Machine) é o motor que roda o código Kotlin **direto no seu computador**, sem celular nem carro — por isso é rápido. Ele renderiza a tela por marca/tema e o **CI** (Continuous Integration — o robô que roda os testes a cada mudança) **falha se um pixel muda sem querer**. **Neste projeto não há CI/CD** (por escolha): na prática, os testes rodam com um `./gradlew test` na mão. Isso pega **regressão visual** (a tela mudar de aparência sem ninguém pedir) de graça, sem abrir cada carro. Cubra **RTL** (Right-To-Left — idiomas lidos da direita pra esquerda, como o árabe, em que o layout precisa espelhar) e fontes ampliadas também.

**No projeto:** `feature/dashboard/src/test/DashboardSnapshotTest` gera goldens (as fotos-gabarito aprovadas) para as 4 marcas (landscape, ou seja, tela deitada) + portrait (tela em pé) em `src/test/snapshots/`.

### Palavras novas deste capítulo
- [car-ui-lib](00-glossario.md) — componentes de tela do sistema, repintáveis.
- [RRO](00-glossario.md) — camada que troca o visual em runtime.
- [OEM](00-glossario.md) — a montadora / fabricante do carro.
- [design tokens](00-glossario.md) — valores de estilo com nome, virados dado.
- [hex hardcoded](00-glossario.md) — cor crua chumbada no código (evite).
- [flavor](00-glossario.md) — variação do mesmo app, uma por marca.
- [white-label](00-glossario.md) — um app que vira várias marcas.
- [BuildConfig.DEFAULT_BRAND](00-glossario.md) — constante de build que diz a marca.
- [dynamic theming](00-glossario.md) — re-pintar tudo trocando os tokens.
- [Paparazzi](00-glossario.md) — tira foto da tela na JVM.
- [snapshot test / golden](00-glossario.md) — compara foto atual com a foto-gabarito.
- [JVM](00-glossario.md) — roda Kotlin no computador, sem carro.
- [CI/CD](00-glossario.md) — robôs que testam e publicam sozinhos.
- [RTL](00-glossario.md) — idiomas lidos da direita pra esquerda.
