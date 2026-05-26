# 📚 Documentação PayFlow - Roadmap e Arquivos

> Guia completo da estrutura de documentação do projeto PayFlow

---

## 🗂️ Estrutura de Diretórios

```
docs/
├── README.md                           # 📄 Este arquivo - Roadmap e índice
├── brief.md                            # 📋 Documento de direcionamento do projeto
├── prompt-instructions-ai-ux-ui.md    # 🎨 Especificações de design para geração de templates
└── wireframes/                         # 🖼️ Prototipagem e wireframes
    ├── payflow-wireframe-ascii.md      # 📐 Mapa de navegação e wireframes ASCII
    └── payflow-wireframe-navegavel.drawio  # 🎯 Prototipo interativo (Draw.io)
```

---

## 📖 Roadmap de Arquivos

### 1. 📋 **brief.md** - Documento de Direcionamento do Projeto
**Localização:** `docs/brief.md`

**Propósito:**
- Contextualização geral do projeto para a turma Android
- Definição clara da proposta e escopo do MVP

**Conteúdo Principal:**
- **Contexto**: Objetivo do projeto final (criar app Android completo com MVVM)
- **Resumo**: Tabela com proposta, telas, API e diferenciais
- **Descrição Detalhada**: Especificações funcionais do app PayFlow
- **Requisitos Obrigatórios**: 
  - Interface com Jetpack Compose
  - 5-8 telas implementadas
  - Consumo de API (mockada ou real)
  - Persistência local (Room/DataStore/SQLite)
  - Arquitetura MVVM
  - README e apresentação de 10 minutos
- **Estrutura Mínima Recomendada**: Detalhamento de cada tela
- **Critérios de Qualidade**: Priorização de fluxo completo
- **Checklist de Funcionalidades**: Validação do MVP
- **Fluxo do Usuário**: Jornada passo a passo

**Quando Usar:**
- Entender os requisitos do projeto
- Validar escopo com a equipe
- Consultar funcionalidades esperadas

---

### 2. 🎨 **prompt-instructions-ai-ux-ui.md** - Especificações de Design AI
**Localização:** `docs/prompt-instructions-ai-ux-ui.md`

**Propósito:**
- Guia para geração de templates UI/UX usando IA
- Especificações visuais e comportamentais para Android Jetpack Compose

**Conteúdo Principal:**
- **Versão Resumida**: Descrição compacta do projeto (≤300 caracteres)
- **Versão Completa**:
  - Descrição geral do app
  - Fluxo principal e detalhamento de 7 telas
  - Navegação e estados de tela
  - Requisitos de UI/UX
  - Stack tecnológico (Jetpack Compose + Material Design 3)
  - Arquitetura sugerida (MVVM)
  - Objetivo: Templates prontos para uso

**Telas Documentadas:**
1. Login/Autenticação
2. Home/Dashboard
3. Perfil
4. Notificações
5. Histórico
6. Cadastro/Edição de Assinatura
7. Detalhe da Assinatura

**Quando Usar:**
- Gerar mockups/templates com IA (ChatGPT, Gemini, etc.)
- Garantir consistência visual entre telas
- Definir vibe design: "Professional, Functional, Universally Friendly"

---

### 3. 📐 **wireframes/payflow-wireframe-ascii.md** - Wireframes de Baixa Fidelidade
**Localização:** `docs/wireframes/payflow-wireframe-ascii.md`

**Propósito:**
- Documentação visual do fluxo de navegação
- Prototipagem rápida com ASCII art

**Conteúdo Principal:**
- **Mapa de Navegação**: Estrutura do fluxo entre telas
- **Wireframes ASCII**: Layouts em texto para:
  - Tela 1: Login/Autenticação
  - (Mais telas em desenvolvimento)

**Benefícios:**
- Fácil de versionar em Git
- Rápido de atualizar
- Facilita comunicação da estrutura

**Quando Usar:**
- Documentar layout de forma leve
- Facilitar discussions sobre UI
- Baseline antes de fazer mockups de alta fidelidade

---

### 4. 🎯 **wireframes/payflow-wireframe-navegavel.drawio** - Protótipo Interativo
**Localização:** `docs/wireframes/payflow-wireframe-navegavel.drawio`

**Propósito:**
- Prototipagem de alta fidelidade com navegação simulada
- Ferramenta: Draw.io (compatível com editores web e desktop)

**Conteúdo Principal:**
- Wireframes detalhadas de cada tela
- Links de navegação entre telas (simulação de fluxo)
- Componentes e layout mais refinados

**Como Usar:**
- Abrir em [draw.io](https://www.draw.io) (web)
- Ou baixar editor desktop
- Navegar entre artboards para visualizar fluxo
- Compartilhar com stakeholders para feedback

**Quando Usar:**
- Apresentações e validações com cliente/professor
- Design de navegação visual
- Identificar fluxos alternativos

---

## 🎯 Fluxo Recomendado de Uso

```
START
  |
  v
1. Ler brief.md
   └─> Entender escopo, requisitos e funcionalidades
  |
  v
2. Revisar prompt-instructions-ai-ux-ui.md
   └─> Validar especificações de design
  |
  v
3. Consultar wireframes/payflow-wireframe-ascii.md
   └─> Entender navegação de forma simples
  |
  v
4. Abrir wireframes/payflow-wireframe-navegavel.drawio
   └─> Visualizar fluxo interativamente
  |
  v
5. Iniciar desenvolvimento
   └─> Usar referências para implementar telas
```

---

## 🔄 Roadmap de Desenvolvimento

| Fase | Artefato | Status | Descrição |
|------|----------|--------|-----------|
| 1 | brief.md | ✅ Completo | Requisitos e escopo definidos |
| 2 | prompt-instructions-ai-ux-ui.md | ✅ Completo | Especificações de design prontas |
| 3 | wireframe-ascii.md | ✅ Completo | Mapa de navegação documentado |
| 4 | wireframe-navegavel.drawio | ✅ Completo | Prototipo interativo disponível |
| 5 | Implementação Android | 🔄 Em Progresso | Desenvolvimento das 7 telas |
| 6 | Testes e Refinamento | ⏳ Planejado | QA e validação com usuários |
| 7 | README Final | ⏳ Planejado | Documentação do código-fonte |
| 8 | Apresentação Final | ⏳ Planejado | Demo de 10 minutos |

---

## 📋 Checklist de Referência

Ao iniciar o desenvolvimento, verifique:

- [ ] Leu o brief.md completamente
- [ ] Entendeu os 7 requisitos obrigatórios (Interface, Telas, API, Persistência, Arquitetura, README, Apresentação)
- [ ] Familiarizou-se com o fluxo de usuário
- [ ] Revisou as especificações de design (prompt-instructions-ai-ux-ui.md)
- [ ] Estudou o mapa de navegação (wireframe-ascii.md)
- [ ] Explorou o prototipo interativo (wireframe-navegavel.drawio)
- [ ] Preparou o ambiente de desenvolvimento (Jetpack Compose, Material Design 3)
- [ ] Arquitetura MVVM planejada

---

## 📞 Referências Rápidas

| Tópico | Arquivo | Seção |
|--------|---------|-------|
| Requisitos Obrigatórios | brief.md | "Requisitos obrigatórios para todos os projetos" |
| Stack Tecnológico | prompt-instructions-ai-ux-ui.md | "Stack Tecnológico (Frontend)" |
| Mapa de Navegação | wireframe-ascii.md | "Mapa de navegacao" |
| Descrição de Telas | brief.md | "Estrutura mínima recomendada do aplicativo" |
| Fluxo de Usuário | brief.md | "Fluxo do usuário" |
| Estados de Tela | prompt-instructions-ai-ux-ui.md | "Estados de Tela" |

---

## 🚀 Próximos Passos

1. **Revisar documentação completa** - Ler todos os arquivos em ordem
2. **Validar escopo com equipe** - Discutir brief.md se há dúvidas
3. **Gerar templates de design** - Usar prompt-instructions-ai-ux-ui.md com IA
4. **Preparar ambiente** - Setup do Android Studio + Jetpack Compose
5. **Implementar MVP** - Começar pelas telas de Login e Dashboard
6. **Iterar e refinar** - Usar wireframes como guia
7. **Documentar código** - Seguir padrões MVVM
8. **Preparar apresentação** - Praticar demo de 10 minutos

---

## 📌 Notas Importantes

> **Escopo**: Mínimo 5 telas, máximo 8. O MVP atualmente documenta 7 telas.

> **Arquitetura**: Obrigatório usar MVVM com separação clara entre UI, ViewModel, repositórios e camada de dados.

> **API**: Pode ser pública, mockada, própria ou gerenciada (Firebase, Supabase, JSON Server).

> **Persistência**: Usar Room, DataStore, SQLite ou equivalente.

> **Prazo**: Apresentação final com demo de 10 minutos.

---

## 📄 Histórico de Atualizações

| Data | Versão | Alterações |
|------|--------|-----------|
| 2026-05-15 | 1.0 | Roadmap inicial criado |

---

**Última Atualização:** 15 de maio de 2026  
**Autor:** Documentação do Projeto PayFlow  
**Status:** ✅ Documentação Base Completa
