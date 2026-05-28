# PayFlowApp

Aplicativo Android (MVP) para gerenciamento de assinaturas e gastos recorrentes.

O projeto ajuda o usuario a controlar servicos assinados, acompanhar vencimentos e identificar assinaturas pouco utilizadas. A proposta central e oferecer uma visao clara das assinaturas ativas, facilitar cadastro/edicao/cancelamento e manter um historico confiavel das assinaturas passadas.

Este README segue a estrutura recomendada e os requisitos do desafio final, com foco em arquitetura MVVM, Jetpack Compose, consumo de API fake e persistencia local.

## Badges

![Status](https://img.shields.io/badge/status-em_desenvolvimento-yellow)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-purple)
![Android](https://img.shields.io/badge/Android-API_24%2B-green)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-blue)
![Arquitetura](https://img.shields.io/badge/Arquitetura-MVVM-orange)
![Licenca](https://img.shields.io/badge/Licenca-MIT-lightgrey)

## Indice

- [Funcionalidades](#funcionalidades)
- [Demonstracao da Aplicacao](#demonstracao-da-aplicacao)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Estrutura de Diretorio](#arquitetura-e-estrutura-de-diretorio)
- [Mock API Local](#mock-api-local)
- [Pre-requisitos e Como Rodar](#pre-requisitos-e-como-rodar)
- [Como Contribuir](#como-contribuir)
- [Licenca](#licenca)

## Funcionalidades

- [x] Login/Autenticacao (fluxo fake)
- [x] Home/Dashboard com assinaturas ativas
- [x] Destaque para assinaturas pouco utilizadas (indicadores/graficos)
- [x] Navegacao para Perfil e Notificacoes
- [x] Cadastro e edicao de assinatura
- [x] Tela de detalhe de assinatura
- [x] Historico de assinaturas passadas (canceladas/expiradas)
- [x] Persistencia local para manter dados apos reabrir o app
- [x] Consumo de API fake para dados de servicos/precos

## Demonstracao da Aplicacao

Adicione aqui uma captura de tela, GIF ou video curto do fluxo principal.

Exemplo:

```markdown
![Demo do PayFlow](./docs/demo.gif)
```

Ou, se preferir imagens por tela:

```markdown
![Tela Login](./docs/screens/login.png)
![Tela Home](./docs/screens/home.png)
![Tela Historico](./docs/screens/historico.png)
```

## Tecnologias Utilizadas

### Stack principal

- **Linguagem**: Kotlin (Java 17+ compativel)
- **UI Framework**: Jetpack Compose (Material Design 3)
- **IDE**: Android Studio
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **Persistencia**: SQLite com Room ORM
- **Networking**: Retrofit + OkHttp
- **Mock API local**: Node.js + lowdb + tinyhttp
- **Injecao de Dependencia**: Hilt (Dagger 2)
- **Estado e ViewModel**: AndroidX ViewModel + StateFlow/LiveData
- **Navegacao**: Jetpack Navigation Compose

### Stack frontend (Compose)

- UI declarativa, moderna e reativa com Jetpack Compose
- Design system com Material Design 3
- suporte a tema claro/escuro
- componentes type-safe com seguranca em tempo de compilacao

## Arquitetura e Estrutura de Diretorio

### Arquitetura sugerida

- MVVM com separacao entre UI, ViewModel, dominio e dados
- persistencia local com Room/SQLite
- integracao com API fake local baseada em arquivo JSON
- gerenciamento de rotas com Navigation Compose
- controle de estado com StateFlow/ViewModel

### Estrutura de diretorio (atual do projeto)

```text
PayFlowApp/
├── app/
│   ├── src/main/
│   │   ├── java/br/com/payflowapplication/
│   │   │   ├── MainActivity.kt
│   │   │   ├── PayFlowApp.kt
│   │   │   ├── data/
│   │   │   │   ├── db/
│   │   │   │   └── repository/
│   │   │   ├── di/
│   │   │   ├── model/
│   │   │   ├── navigation/
│   │   │   ├── ui/
│   │   │   │   └── theme/
│   │   │   ├── view/
│   │   │   │   ├── components/
│   │   │   │   └── screens/
│   │   │   └── viewmodels/
│   │   ├── AndroidManifest.xml
│   │   └── res/
│   └── build.gradle.kts
├── mocks/
│   ├── db.json
│   ├── package.json
│   ├── server.js
│   └── README.md
├── build.gradle.kts
├── gradle/
├── settings.gradle.kts
└── README.md
```

### Legendas da estrutura

- `PayFlowApp/`: raiz do projeto Android multi-modulo (neste caso, com modulo principal `app`).
- `app/`: modulo de aplicativo Android (codigo-fonte, recursos e configuracao do modulo).
- `app/src/main/java/br/com/payflowapplication/`: pacote base do app.
- `MainActivity.kt`: Activity de entrada que hospeda a UI Compose.
- `PayFlowApp.kt`: classe `Application` para inicializacao global (ex.: Hilt).
- `data/`: camada de dados (fontes locais/remotas e acesso a dados).
- `data/db/`: classes de banco local com Room (DAO, database, converters).
- `data/repository/`: repositorios que centralizam acesso aos dados para os ViewModels.
- `di/`: modulos de injeção de dependencia (providers/bindings do Hilt).
- `model/`: modelos de dominio/entidades usados pela aplicacao.
- `navigation/`: definicao de rotas e grafo de navegacao (Navigation Compose).
- `ui/theme/`: tema visual do app (cores, tipografia e estilos do Compose).
- `view/`: camada de apresentacao em Compose.
- `view/components/`: componentes reutilizaveis de UI.
- `view/screens/`: telas completas do app (fluxos de interface).
- `viewmodels/`: ViewModels e estado de tela (StateFlow/LiveData).
- `app/src/main/res/`: recursos Android (drawables, values, icones, xml etc.).
- `app/src/main/AndroidManifest.xml`: manifesto com componentes e configuracoes do app.
- `mocks/`: servidor mock local usado para simular endpoints REST consumidos pelo app.
- `mocks/db.json`: base de dados fake com catalogo de servicos e consumo mensal.
- `mocks/package.json`: dependencias e script de inicializacao da mock API.
- `mocks/server.js`: servidor HTTP local que expoe os endpoints fake.
- `mocks/README.md`: documentacao detalhada de uso da API mock.
- `build.gradle.kts` (raiz e modulo): scripts Gradle de build/dependencias.
- `gradle/`: configuracoes do wrapper/versionamento de plugins e libs.
- `settings.gradle.kts`: declaracao de modulos e repositorios do projeto.

## Mock API Local

O projeto possui uma API fake local no diretorio `mocks/`, usada para apoiar o desenvolvimento e os testes de integracao do app Android.

Ela atende cenarios como:

- busca de servicos no cadastro de assinatura;
- listagem de streamings disponiveis;
- consulta de consumo mensal para identificar assinaturas pouco utilizadas.

### Endpoints principais

```text
GET /api/v1/streamings
GET /api/v1/streamings/search?nome=net
GET /api/v1/streamings/:username/consumo_mensal?nome=netflix&anomes=2026-05
```

### Documentacao detalhada

Consulte o arquivo `mocks/README.md` para:

- modo de uso;
- estrutura do diretorio;
- legenda dos arquivos;
- exemplos de chamadas.

## Pre-requisitos e Como Rodar

### Pre-requisitos

- Android Studio atualizado
- JDK 17+
- Android SDK configurado
- Emulador Android ou dispositivo fisico
- Node.js + npm (para subir a mock API local)

### Como rodar (Windows PowerShell)

```powershell
git clone <URL_DO_REPOSITORIO>
cd PayFlowApp
.\gradlew.bat tasks
cd .\mocks
npm install
npm start
```

Em outro terminal:

```powershell
cd PayFlowApp
.\gradlew.bat tasks
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

### Como rodar (Linux/macOS)

```bash
git clone <URL_DO_REPOSITORIO>
cd PayFlowApp
./gradlew tasks
cd ./mocks
npm install
npm start
```

Em outro terminal:

```bash
cd PayFlowApp
./gradlew tasks
./gradlew assembleDebug
./gradlew test
```

> Observacao: a mock API sobe por padrao em `http://localhost:3000`. Para emulador Android, a integracao normalmente usa `http://10.0.2.2:3000/`.

## Como Contribuir

1. Faca um fork do repositorio.
2. Crie uma branch para sua feature/correcao:
   - `git checkout -b feature/minha-feature`
3. Commit suas alteracoes:
   - `git commit -m "feat: adiciona minha feature"`
4. Envie para seu fork:
   - `git push origin feature/minha-feature`
5. Abra um Pull Request descrevendo objetivo, mudancas e evidencias (prints/GIF).

## Licenca

Este projeto esta sob a licenca **MIT**.

Se quiser formalizar, adicione o arquivo `LICENSE` na raiz do projeto.

## Integrantes

- Nome 1 - RM/RA
