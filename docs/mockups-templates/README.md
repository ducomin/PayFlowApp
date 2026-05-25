# 🎨 PayFlow UI/UX Mockups Templates

Dois conjuntos de mockups profissionais para o aplicativo **PayFlow** - um gerenciador de assinaturas e gastos recorrentes.

---

## 📋 Estrutura de Arquivos

```
mockups-templates/
├── 01-mockups-standard-bottomnav.html      # V1: Bottom Navigation · Light · Indigo+Cyan
├── 02-mockups-alternative-sidenav.html     # V2: Side Drawer + FAB · Light · Indigo+Cyan
├── 03-jetpack-compose-implementation.md    # Código Jetpack Compose
├── 04-comparacao-versoes.md                # Comparativo v1 vs v2
├── 05-md3-dark-teal-mockups.html           # V3: M3 Dark Theme · Teal+Amber ← NOVO
└── README.md (este arquivo)
```

---

## 🎯 Versão 1: Bottom Navigation (Material Design 3)

**Arquivo:** `01-mockups-standard-bottomnav.html`

### Características

✅ **Navegação Padrão Android (Material Design 3)**
- Bottom Navigation Bar com 4 itens principais
- Ícones + labels em cada item
- Badge de notificações no item "Notificações"

✅ **Layout Responsivo**
- Otimizado para dispositivos móveis (375x812px)
- Scrollable content area
- Status bar simulado no topo

✅ **7 Telas Completas**
1. **Login/Autenticação** - Tela de entrada com campos de email/senha
2. **Home/Dashboard** - Resumo financeiro e lista de assinaturas ativas
3. **Perfil** - Informações do usuário e configurações
4. **Notificações** - Alertas agrupados por data com ícones
5. **Histórico** - Assinaturas canceladas/expiradas
6. **Cadastro/Edição** - Formulário para novas assinaturas
7. **Detalhe** - Informações completas de uma assinatura

### Design System Aplicado

- **Paleta de Cores Material Design 3:**
  - Primary: #6366F1 (Indigo)
  - Secondary: #06B6D4 (Cyan)
  - Tertiary: #EC4899 (Pink)
  - Error: #EF4444 (Red)

- **Tipografia:** Roboto (Google Fonts)
- **Componentes:** Cards, Buttons, Input Fields, Badge
- **Estados:** Loading, Success, Empty, Error

### Como Usar

1. Abra `01-mockups-standard-bottomnav.html` em um navegador
2. Visualize as 7 telas em dispositivos móveis
3. Use como referência visual durante o desenvolvimento
4. Compartilhe com stakeholders para validação

---

## ✨ Versão 2: Side Navigation + FAB (Proposta Alternativa)

**Arquivo:** `02-mockups-alternative-sidenav.html`

### Características

✅ **Navegação Lateral (Navigation Drawer)**
- Menu drawer aberto no lado esquerdo
- 4 itens de navegação principais + Settings
- Item ativo destacado com fundo colorido
- Transição suave ao alternar screens

✅ **Floating Action Button (FAB)**
- Botão de ação flutuante no canto inferior direito
- Usado para criar novas assinaturas
- Design moderno e intuitivo
- Suporta hover e press states

✅ **Top App Bar**
- Menu icon (drawer toggle) no início
- Título da tela no centro
- Ações contextuais à direita
- Back navigation em telas de formulário

✅ **7 Telas Completas**
1. **Login/Autenticação** - Tela de entrada padrão
2. **Home/Dashboard** - Com drawer aberto + FAB visível
3. **Perfil** - Drawer com item ativo destacado
4. **Notificações** - Drawer com badge de contagem
5. **Histórico** - Drawer e conteúdo scrollable
6. **Cadastro/Edição** - Top bar com back navigation
7. **Detalhe** - Top bar com menu overflow (⋮)

### Design Diferenças

| Aspecto | Versão 1 (Bottom Nav) | Versão 2 (Drawer + FAB) |
|--------|---------------------|------------------------|
| **Navegação Principal** | Bottom Bar | Drawer Lateral |
| **Ação Primária** | Item no bottom nav | FAB (Floating Button) |
| **Top App Bar** | Simples (status bar) | Completa com actions |
| **Accessible Targets** | 80px altura | Melhor para telas menores |
| **Estilo** | Material Design 3 Standard | Material Design 3 Advanced |
| **Espaço de Conteúdo** | Máximo | Drawer overlay |

### Como Usar

1. Abra `02-mockups-alternative-sidenav.html` em um navegador
2. Visualize o drawer aberto em todas as telas de navegação
3. Observe a FAB flutuante na tela de Home
4. Compare com a Versão 1 para escolher a melhor abordagem
5. Use como base para implementação em Jetpack Compose

---

---

## 🌑 Versão 3: Material Design 3 · Dark Theme · Teal + Amber (Proposta Nova)

**Arquivo:** `05-md3-dark-teal-mockups.html`

### Por que é diferente das versões anteriores?

| Aspecto | V1 Bottom Nav | V2 Side Drawer | **V3 Dark M3** |
|---------|--------------|---------------|----------------|
| **Tema** | Light | Light | **Dark nativo** |
| **Paleta** | Indigo + Cyan | Indigo + Cyan | **Teal + Amber Gold** |
| **NavigationBar** | Ícone destacado | Drawer lateral | **Pill indicator M3** |
| **FAB** | — | FAB circular | **Extended FAB** |
| **Campos de formulário** | Custom | Custom | **M3 Filled TextField** |
| **Filtros** | — | — | **FilterChips M3 selecionáveis** |
| **Modalidade** | Select dropdown | Select dropdown | **SegmentedButton M3** |
| **Progresso de uso** | — | — | **LinearProgressIndicator por card** |
| **Cards** | Outlined | Outlined | **Elevated / Filled / Outlined hierarquia** |
| **Superfícies** | 1 nível | 1 nível | **Surface Container (5 níveis tonal)** |

### M3 Color Tokens usados

```kotlin
// MaterialTheme.colorScheme equivalente
primary              = Teal   #4DD0E1
onPrimary            = #00363D
primaryContainer     = #00525C
secondary            = Amber  #FFD54F
secondaryContainer   = #574400
tertiary             = Purple #CE93D8
background           = #191C1E
surface              = #191C1E
surfaceContainer     = #252A2C
surfaceContainerHigh = #2F3436
```

### Componentes M3 implementados

✅ **CenterAligned TopAppBar** com tonal elevation (primary tint overlay)  
✅ **NavigationBar** com `NavigationBarItem` + pill indicator ativo (secondary container)  
✅ **Badge** no ícone de notificações (M3 Badge)  
✅ **ExtendedFloatingActionButton** "Nova Assinatura"  
✅ **FilterChip** com check icon e estado selecionado  
✅ **SegmentedButton** para modalidade mensal/anual  
✅ **Filled TextField** com label flutuante e underline ativo  
✅ **Card Elevated / Card Filled / Card Outlined** hierarquia  
✅ **LinearProgressIndicator** (uso por assinatura: success/warn/error)  
✅ **Switch M3** no perfil (dark mode toggle)  
✅ **Status chips** semânticos (ativa / pouco usada / cancelada / expirada)  
✅ **Step indicator** no formulário de cadastro  
✅ **AssistChips** para seleção de categoria

### 7 Telas Completas

1. **Login/Autenticação** — Brand logo gradient, Filled TextFields, state layers
2. **Home/Dashboard** — Summary card tonal, SearchBar M3, FilterChips, subscription cards com usage bar, Extended FAB
3. **Perfil** — Hero gradient, stats grid 3-col, settings list, M3 Switch
4. **Notificações** — Agrupadas por data, border-left colorida semântica, action buttons
5. **Histórico** — FilterChips, summary row, history cards com status chips
6. **Cadastro/Edição** — Step indicator, SegmentedButton, category AssistChips
7. **Detalhe** — Hero service card, info grid 2×2, usage progress bar, payment history list

### Como abrir

```bash
start docs/mockups-templates/05-md3-dark-teal-mockups.html
```

---

## 🛠️ Stack Técnico do Projeto

Os três mockups foram criados seguindo a stack técnica especificada:

| Componente | Tecnologia |
|-----------|-----------|
| **Linguagem** | Kotlin |
| **UI Framework** | Jetpack Compose (Declarativo) |
| **IDE** | Android Studio |
| **Arquitetura** | MVVM / MVI |
| **Banco de Dados** | Room (SQLite) |
| **Networking** | Retrofit + OkHttp |
| **Injeção de Dependência** | Hilt |

---

## 🎯 Quando Usar Cada Versão?

### Versão 1 (Bottom Navigation) ✅ Use quando:
- Precisa de navegação simples e direta
- Quer seguir Material Design 3 padrão rigorosamente
- As telas não precisam de muita hierarquia
- Quer máxima acessibilidade com ícones + labels

### Versão 2 (Drawer + FAB) ✅ Use quando:
- Precisa de mais opções de navegação (Settings, Logout)
- Quer economia de espaço vertical
- Busca um design mais "premium" e moderno
- Telas de cadastro/edição beneficiam de FAB
- Quer diferenciação visual em relação a apps concorrentes

---

## 📱 Responsividade

Ambos os mockups são otimizados para:
- **Smartphones:** 360x800px até 414x896px
- **Tablets:** Escalado proporcionalmente
- **Desktop:** Grid responsivo com 1-3 mockups por linha

---

## 🎨 Elementos de Design

### Componentes Utilizados

✅ **Cards**
- Subscription Card (com icon, nome, categoria, valor)
- History Card (com status, datas, valor)
- Notification Item (com ícone, título, mensagem)

✅ **Inputs & Forms**
- Text Input com border focus
- Select/Dropdown
- Date Input
- Checkbox

✅ **Buttons**
- Primary Button (CTA principal)
- Secondary Button (ação secundária)
- Icon Buttons (in top bar)

✅ **States**
- Active/Inactive (navigation items)
- Hover (buttons, cards)
- Focus (inputs)
- Badge (notification count)

### Cores Utilizadas

```
Primary:      #6366F1 (Indigo 500) - Botões, links, ícones ativos
Secondary:    #06B6D4 (Cyan 400)   - Cards de categoria, complementar
Tertiary:     #EC4899 (Pink 500)   - Destaques, gradientes
Error:        #EF4444 (Red)        - Status cancelado, badges
Success:      #10B981 (Green)      - Status ativo
Surface:      #FFFFFF (Branco)     - Fundos de cards
On Surface:   #1F2937 (Gray 800)   - Texto principal
Outline:      #D1D5DB (Gray 300)   - Borders
```

---

## 📊 Resumo das Telas

### Tela 1: Login/Autenticação
- Logo do app (PF em gradiente)
- Campos: Email, Senha
- Checkbox "Lembrar acesso"
- Botão primário "Entrar"
- Validações visuais

### Tela 2: Home/Dashboard
- Greeting personalizado
- Summary Card (gastos do mês)
- List de assinaturas ativas
- Quick actions (histórico, notificações)
- Navigation para outras telas

### Tela 3: Perfil
- Avatar do usuário
- Informações: Nome, Email, Plano
- Estatísticas: Assinaturas, Gasto mensal
- Botão Logout

### Tela 4: Notificações
- Grouped by date (Hoje, Esta semana)
- Notification items com ícone e categoria
- Diferentes tipos: Vencimento, Uso baixo, Promoção, Sucesso

### Tela 5: Histórico
- Cards de assinaturas canceladas/expiradas
- Status badge (Cancelada/Expirada)
- Informações: Datas, duração, valor mensal

### Tela 6: Cadastro/Edição
- Form fields: Nome, Valor, Categoria, Modalidade, Vencimento
- Validação visual
- Buttons: Cancelar, Salvar
- Layout responsivo em mobile

### Tela 7: Detalhe
- Header com gradiente
- Info rows: Valor, Status, Vencimento, Uso, Início
- Buttons: Editar, Cancelar
- Design limpo e focado

---

## 🚀 Próximos Passos

### Para Designers
1. Revisar ambas as versões
2. Validar paleta de cores com stakeholders
3. Confirmar qual abordagem de navegação usar
4. Exportar assets (ícones, gradientes, shadows)

### Para Desenvolvedores
1. Estudar os componentes em cada versão
2. Implementar em Jetpack Compose
3. Seguir a estrutura MVVM indicada
4. Usar Room para persistência
5. Integrar Retrofit para APIs

### Para Product/PM
1. Testar usabilidade de ambas versões
2. Coletar feedback de usuários
3. Validar fluxo de navegação
4. Confirmar requisitos funcionais

---

## 📞 Notas de Implementação

### Jetpack Compose Considerations

```kotlin
// Para Versão 1 (Bottom Navigation)
BottomNavigation(
    backgroundColor = Color.White,
    contentColor = Color(0x6366F1),
    elevation = 8.dp
) {
    BottomNavigationItem(
        icon = { Icon(Icons.Default.Home, ...) },
        label = { Text("Home") },
        selected = isSelected,
        onClick = { /* navigate */ }
    )
}

// Para Versão 2 (Navigation Drawer)
ModalDrawer(
    drawerContent = {
        NavigationDrawerItem(
            icon = { Icon(...) },
            label = { Text("Home") },
            selected = isSelected,
            onClick = { /* navigate */ }
        )
    }
) { /* content */ }

// FAB
FloatingActionButton(
    onClick = { /* create subscription */ },
    backgroundColor = Color(0x6366F1)
) {
    Icon(Icons.Default.Add, ...)
}
```

### Architecture Pattern

```
ui/
├── screens/
│   ├── LoginScreen
│   ├── HomeScreen
│   ├── ProfileScreen
│   ├── NotificationsScreen
│   ├── HistoryScreen
│   ├── SubscriptionFormScreen
│   └── SubscriptionDetailScreen
├── components/
│   ├── SubscriptionCard
│   ├── NotificationItem
│   ├── PayFlowBottomNavigation (V1)
│   ├── PayFlowNavigationDrawer (V2)
│   └── PayFlowFAB (V2)
└── theme/
    ├── Color.kt
    ├── Type.kt
    └── Theme.kt

domain/
├── models/
├── repositories/
└── usecases/

data/
├── local/ (Room)
├── remote/ (Retrofit)
└── mappers/
```

---

## ✅ Checklist de Validação

Ao implementar, certifique-se de:

- [ ] Todas as 7 telas estão implementadas
- [ ] Navegação funciona sem erros
- [ ] Estados (loading, error, empty) são tratados
- [ ] MVVM pattern é seguido
- [ ] Room está configurado para persistência
- [ ] Retrofit está integrado para APIs
- [ ] Hilt é usado para DI
- [ ] Cores seguem Material Design 3
- [ ] Componentes são reutilizáveis
- [ ] Acessibilidade foi considerada
- [ ] Responsividade funciona em diferentes tamanhos

---

## 📄 Informações Adicionais

**Projeto:** PayFlow  
**Versão:** 1.0 (Mockups)  
**Data:** Maio 2026  
**Formato:** HTML Interactive Prototypes  
**Browser Support:** Chrome, Safari, Firefox, Edge (moderno)  

---

## 🎓 Referências

- [Material Design 3 Guidelines](https://m3.material.io/)
- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
- [MVVM Pattern Best Practices](https://developer.android.com/jetpack/guide)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)

---

**Última Atualização:** 15 de Maio de 2026  
**Status:** ✅ Mockups Completos e Prontos para Desenvolvimento
