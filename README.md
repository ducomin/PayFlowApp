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
- integracao com API fake (ex.: JSON Server)
- gerenciamento de rotas com Navigation Compose
- controle de estado com StateFlow/ViewModel

### Estrutura de diretorio

```text
PayFlow/
├── app/
│   ├── src/main/java/com/payflow/
│   │   ├── MainActivity.kt
│   │   ├── navigation/
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   ├── components/
│   │   │   ├── screens/
│   │   ├── data/
│   │   ├── domain/
│   │   ├── viewmodel/
│   ├── res/
│   ├── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
```

## Pre-requisitos e Como Rodar

### Pre-requisitos

- Android Studio atualizado
- JDK 17+
- Android SDK configurado
- Emulador Android ou dispositivo fisico

### Como rodar (Windows PowerShell)

```powershell
git clone <URL_DO_REPOSITORIO>
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
./gradlew assembleDebug
./gradlew test
```

> Observacao: os comandos acima sao o fluxo padrao para projetos Android com Gradle Wrapper. Ajuste o nome do repositorio/pasta conforme seu remoto.

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
