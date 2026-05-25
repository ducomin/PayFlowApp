# PayFlow - AI Prompt para Geração de Templates UI/UX Android

---

## 📱 PORTUGUÊS (PT-BR)

### Versão Resumida (≤300 caracteres)

PayFlow: app de gerenciamento de assinaturas com 7 telas. Fluxo: Login → Dashboard → (Perfil/Notificações/Histórico/Cadastro/Detalhes). Recursos: busca, filtros, rastreador de gastos, indicadores de uso, histórico de pagamentos. Stack: Jetpack Compose + Material Design 3. Vibe: Professional, Functional, Universally Friendly. MVVM. Animated, responsive Android UI.

**Caracteres: 286**

---

### Versão Completa

**Projeto:** PayFlow

**Descrição Geral:**
PayFlow é um aplicativo mobile para Android focado em organizar assinaturas e gastos recorrentes do usuário. O objetivo é proporcionar uma experiência visual moderna, animada e intuitiva, facilitando o controle financeiro de assinaturas mensais ou anuais, destacando oportunidades de economia e uso consciente.

**Fluxo Principal e Telas:**
O MVP possui 7 telas principais, com navegação fluida e transições animadas:

1. **Login/Autenticação**
   - Tela inicial com logo, campos de email e senha, botão de login, opção "lembrar acesso" e mensagens de erro.
   - Após login bem-sucedido, navega para o Dashboard.

2. **Home/Dashboard**
   - Saudação personalizada, resumo financeiro do mês, busca e filtros por assinatura/categoria.
   - Cards animados para assinaturas pouco utilizadas, lista de assinaturas ativas, botões para adicionar nova assinatura e acessar histórico.
   - Ícones para acessar Perfil e Notificações.
   - Navegação inferior fixa para Home, Histórico, Notificações e Perfil.

3. **Perfil**
   - Exibe avatar, nome, email e plano do usuário.
   - Botão para sair (logout) e voltar para Home.

4. **Notificações**
   - Lista de notificações agrupadas por data (hoje, semana).
   - Cards de alerta (ex: vencimento próximo, promoções, baixo uso).
   - Botão para marcar todas como lidas.
   - Voltar para Home.

5. **Histórico**
   - Lista de assinaturas passadas (canceladas/expiradas) com filtros e busca.
   - Cards detalhados com datas e valores.
   - Ao clicar em um item, navega para Detalhe da Assinatura (modo leitura).

6. **Cadastro/Edição de Assinatura**
   - Formulário animado para criar ou editar assinatura: nome, valor, modalidade (mensal/anual), data de vencimento, categoria, URL.
   - Botões para salvar ou cancelar (com confirmação).
   - Validação de campos e feedback visual.

7. **Detalhe da Assinatura**
   - Exibe informações completas da assinatura, status, categoria, uso no mês, indicador de uso, extrato de pagamentos.
   - Botões para editar ou cancelar assinatura (com confirmação).
   - Retorno para tela anterior.

**Navegação:**
- Fluxo principal: Login → Home/Dashboard → (Perfil | Notificações | Histórico | Cadastro/Edição | Detalhe).
- Da Home, é possível acessar rapidamente Perfil, Notificações, Histórico, Detalhe de Assinatura e Cadastro.
- Da tela de Detalhe, pode-se editar ou cancelar a assinatura.
- Da tela de Histórico, acessa-se detalhes de assinaturas passadas (edição desabilitada).
- Todas as telas principais possuem navegação inferior fixa.

**Estados de Tela:**
- Carregando (skeleton ou animação)
- Sucesso (dados preenchidos)
- Vazio (mensagem "Nenhuma assinatura encontrada")
- Erro (mensagem com ação para tentar novamente)

**Requisitos de UI/UX:**
- **Vibe Design:** Professional, Functional, Universally Friendly
- Visual moderno, limpo e atrativo, com cores vibrantes e tipografia clara.
- Animações suaves em transições de tela, feedback de ações (ex: salvar, erro, loading).
- Cards e listas com microinterações (ex: swipe para marcar notificação como lida).
- Componentes responsivos e acessíveis.
- Ícones e ilustrações para reforçar a identidade visual.

**Stack Tecnológico:**
- **Linguagem**: Kotlin (Java 17+ compatible)
- **UI Framework**: Jetpack Compose (Material Design 3)
- **IDE**: Android Studio
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **Persistência**: SQLite com Room ORM
- **Networking**: Retrofit + OkHttp para APIs
- **Injeção de Dependência**: Hilt (Dagger 2)
- **Estado & ViewModel**: AndroidX ViewModel + StateFlow/LiveData
- **Navegação**: Jetpack Navigation Compose

**Stack Tecnológico (Frontend):**
- **Jetpack Compose** para UI declarativa, moderna e reativa
- **Material Design 3** para design system coeso, componentes pré-testados e consistência visual
- Temas adaptativos e suporte a dark/light mode
- Componentes seguros de tipos com segurança em tempo de compilação

**Arquitetura Sugerida:**
- MVVM (Model-View-ViewModel) com Jetpack Compose
- Persistência local (SQLite) e API fake (JSON Server) para dados de serviços/preços
- Compose Navigation para gerenciamento de rotas
- State Management com StateFlow/ViewModel

**Objetivo:**
Gerar templates UI/UX animados e atrativos para cada tela descrita, considerando navegação, estados e microinterações, prontos para uso em um app Android moderno.

---

## 🌐 ENGLISH (EN)

### Short Version (≤300 characters)

PayFlow: subscription management app with 7 screens. Flow: Login → Dashboard → (Profile/Notifications/History/Registration/Details). Features: search, filters, spending tracker, usage indicators, payment history. Stack: Jetpack Compose + Material Design 3. Vibe: Professional, Functional, Universally Friendly. MVVM. Animated, responsive Android UI.

**Characters: 283**

---

### Full Version

**Project:** PayFlow

**General Description:**
PayFlow is a mobile app for Android designed to help users organize their subscriptions and recurring expenses. The goal is to provide a modern, animated, and intuitive visual experience, making it easy to manage monthly or yearly subscriptions, highlight savings opportunities, and encourage conscious usage.

**Main Flow and Screens:**
The MVP consists of 7 main screens, with smooth navigation and animated transitions:

1. **Login/Authentication**
   - Initial screen with app logo, email and password fields, login button, "remember me" option, and error messages.
   - After successful login, navigates to the Dashboard.

2. **Home/Dashboard**
   - Personalized greeting, monthly financial summary, search and filters by subscription/category.
   - Animated cards for underused subscriptions, list of active subscriptions, buttons to add a new subscription and access history.
   - Icons to access Profile and Notifications.
   - Fixed bottom navigation for Home, History, Notifications, and Profile.

3. **Profile**
   - Displays user avatar, name, email, and plan.
   - Button to log out (return to Login) and back navigation to Home.

4. **Notifications**
   - List of notifications grouped by date (today, this week).
   - Alert cards (e.g., upcoming due date, promotions, low usage).
   - Button to mark all as read.
   - Back navigation to Home.

5. **History**
   - List of past subscriptions (canceled/expired) with filters and search.
   - Detailed cards with dates and values.
   - Clicking an item navigates to Subscription Details (read-only mode).

6. **Subscription Registration/Editing**
   - Animated form to create or edit a subscription: name, value, type (monthly/yearly), due date, category, URL.
   - Buttons to save or cancel (with confirmation).
   - Field validation and visual feedback.

7. **Subscription Details**
   - Shows complete subscription info, status, category, usage for the month, usage indicator, and payment history.
   - Buttons to edit or cancel the subscription (with confirmation).
   - Back navigation to the previous screen.

**Navigation:**
- Main flow: Login → Home/Dashboard → (Profile | Notifications | History | Registration/Editing | Details).
- From Home, users can quickly access Profile, Notifications, History, Subscription Details, and Registration.
- From Details, users can edit or cancel the subscription.
- From History, users access details of past subscriptions (editing disabled).
- All main screens have fixed bottom navigation.

**Screen States:**
- Loading (skeleton or animation)
- Success (data filled)
- Empty (message "No subscriptions found")
- Error (message with "Try again" action)

**UI/UX Requirements:**
- **Design Vibe:** Professional, Functional, Universally Friendly
- Modern, clean, and attractive visuals with vibrant colors and clear typography.
- Smooth animations for screen transitions, action feedback (e.g., save, error, loading).
- Cards and lists with micro-interactions (e.g., swipe to mark notification as read).
- Responsive and accessible components.
- Icons and illustrations to reinforce visual identity.

**Frontend Technology Stack:**
- **Jetpack Compose** for declarative, modern, and reactive UI
- **Material Design 3** for cohesive design system, pre-tested components, and visual consistency
- Adaptive themes with dark/light mode support
- Type-safe components with compile-time safety

**Suggested Architecture:**
- MVVM (Model-View-ViewModel) with Jetpack Compose
- Local persistence (SQLite) and fake API for service/price data
- Compose Navigation for route management
- State Management with StateFlow/ViewModel

**Objective:**
Generate animated and attractive UI/UX templates for each described screen, considering navigation, states, and micro-interactions, ready for use in a modern Android app.

---

**Created:** May 15, 2026  
**Project:** PayFlow MVP  
**Format:** Markdown (PT-BR / EN)
