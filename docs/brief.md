## Direcionamento dos Projetos Finais – Turma Android - PayFlow
### Contexto
O projeto final da turma Android tem como objetivo criar um aplicativo funcional e completo, seguindo as melhores práticas de desenvolvimento Android. O foco é entregar um MVP (Produto Mínimo Viável) que demonstre a capacidade de criar uma aplicação com interface agradável, navegação fluida, consumo de API e persistência local, tudo estruturado em uma arquitetura MVVM. O tema sugerido para o projeto é um organizador de assinaturas e gastos recorrentes da assinatura. O projeto deve ser entregue com um README detalhado e uma apresentação final de 10 minutos.

### Resumo do Projeto

|Titulo|Descrição|
|---|---|
|Proposta| Organizador de assinaturas e fastros recorrentes |
|Telas sugeridas| Assinaturas; vencimentos; notificações; categorias; histórico; resumo mensal;economia. |
|API e Persistencia| API de serviços/preços mock; persistência de assinaturas. |
|Diferenciais| Mostra gastos com assinaturas pouco utilizadas. |
|Escopo esperado| Um MVP funcional, com dados simulados quando necessário. |

### Decrição detalhada do projeto
O aplicativo deve permitir que os usuários gerenciem suas assinaturas e gastos recorrentes de forma eficiente. A tela inicial (Home/Dashboard) deve mostrar um resumo das assinaturas ativas, destacando aquelas que são pouco utilizadas, por meio de gráficos ou indicadores visuais. Os usuários devem ser capazes de navegar para outras telas, como perfil, notificações, listagem das assinaturas, com detalhes e opções para editar ou cancelar e cadastro de novas assinaturas. A tela de histórico deve permitir que os usuários vejam suas assinaturas passadas. O cadastro de assinaturas deve incluir campos para nome, valor, data de vencimento, modalidade (mensal ou anual), categoria e URL do serviço. O aplicativo deve consumir pelo menos uma API (FAKE) para obter dados relacionados a serviços ou preços, e deve persistir os dados. 

**Observação sobre Listagem e Histórico:**
A listagem de assinaturas exibida na Dashboard (Home) mostra todas as assinaturas ativas do usuário, ou seja, aquelas que estão em vigor no momento. Já a tela de Histórico apresenta apenas as assinaturas passadas, ou seja, aquelas que foram canceladas ou expiraram. Assim, a listagem serve para o gerenciamento das assinaturas atuais, enquanto o histórico é voltado para consulta e análise de assinaturas antigas.

### Stack Técnico do Projeto
| Componente | Tecnologia | Descrição |
|---|---|---|
|**Linguagem**| Kotlin | Linguagem principal para desenvolvimento Android moderna e segura. |
|**UI (Interface)**| Jetpack Compose | Framework declarativo moderno para construir interfaces Android. |
|**IDE**| Android Studio | Ferramenta oficial de desenvolvimento Android. |
|**Arquitetura**| MVVM | Model-View-ViewModel para separação clara de responsabilidades. |
|**Banco de Dados**| SQLite | Armazenamento local de dados persistentes das assinaturas. |
|**Networking**| Retrofit + OkHttp | Retrofit para chamadas HTTP e OkHttp para gerenciamento de requisições. |
|**Injeção de Dependência**| Hilt | Framework de DI baseado em Dagger 2 para Android. |

### Requisitos obrigatórios para todos os projetos
| Obrigatórios | Direcionamento |
|---|---|
|Interface e navegação| Usar Jetpack Compose e navegação entre telas. |
|Quantidade de telas| Mínimo de 5 telas e máximo de 8 telas. |
|API| Consumir pelo menos uma API, que pode ser pública, mockada, própria ou gerenciada por serviço como Firebase/Supabase/JSON Server. |
|Persistência local| Gerenciar dados localmente usando Room, DataStore, SQLite, cache local ou solução equivalente. |
|Arquitetura| Estruturar o projeto em MVVM, separando UI, ViewModel, controle de estados, repositórios, modelos e camada de dados. |
|README| Explicar proposta, funcionalidades, prints ou GIFs, arquitetura, dependências, instruções de execução e integrantes. |

### Estrutura mínima recomendada do aplicativo
- **Tela 1 - Login/Autenticação**: entrada do usuário; pode usar autenticação fake, JWT mockado ou serviço real.
- **Tela 2 - Home/Dashboard**: 
    - Mostra gastos com assinaturas pouco utilizadas (gráfico); 
    - Barra de navegação para acessar outras telas;
        - **2.1 SubTela: Perfil**;
            - Nome, email e foto;
        - **2.2 SubTela: Notificações**;
            - Lista de notificações sobre vencimentos, promoções, etc;
    - Lista de assinaturas cadastradas com filtros e ordenação; 
    - Acesso rápido para criar nova assinatura;
    - Acesso rápido para histórico;
- **Tela 3 - Histórico**: 
    - Lista de assinaturas passadas, com filtros por data, categoria ou valor.
    - Nos itens, opção para editar ou cancelar assinaturas.
- **Tela 4 - Cadastro**: Formulário para criar ou editar assinaturas, com campos para nome, valor, data de vencimento, modalidade (mensal ou anual), categoria (Saúde, Streaming, Entretenimento, Educação, Outro.) e url do serviço.
- **Tela 5 - Detalhe**: Mostra detalhes de uma assinatura selecionada, com opções para extrato de pagamentos.

### Critérios de escopo e qualidade
- Priorizar fluxo completo e estável em vez de muitas telas incompletas.
- Tratar controle deestados de carregamento, sucesso, erro e vazio nas telas principais.
- Usar dados persistidos localmente para que o app continue útil após fechar e abrir novamente.
- Usar uma API de forma visível no app com dados mockados.
- Garantir que cada tela tenha propósito claro e esteja conectada ao fluxo principal do MVP.

### Checklist de funcionalidades
- [ ]  App possui entre 5 e 8 telas implementadas
- [ ]  Jetpack Compose e navegação estão funcionando sem travamentos no fluxo principal.
- [ ]  Há consumo de pelo menos uma API.
- [ ]  Há persistência local dos dados principais.
- [ ]  Arquitetura MVVM está visível na estrutura de pacotes.
- [ ]  README contém descrição, funcionalidades, integrantes, tecnologias e instruções de execução.
- [ ]  Apresentação está ensaiada para caber em 10 minutos

### Fluxo do usuário
1. O usuário abre o aplicativo e é direcionado para a tela de login/autenticação.
2. Após o login, o usuário é levado para a tela Home/Dashboard, onde pode ver um resumo das assinaturas ativas e um gráfico destacando aquelas pouco utilizadas e a lista de assinaturas.
    - O usuário pode clicar em uma assinatura para ver detalhes ou editar/cancelar.
    - O usuário pode clicar em um botão para criar uma nova assinatura, que o leva para a tela de cadastro.
    - O usuário pode clicar em um botão para acessar o histórico de assinaturas, que o leva para a tela de histórico.
3. O usuário pode navegar para a tela de perfil para ver suas informações, ou para a tela de notificações para ver alertas sobre vencimentos e promoções.
4. Na tela de cadastro, o usuário pode preencher o formulário para criar ou editar uma assinatura, e salvar as informações.
5. O usuário pode acessar o histórico para ver suas assinaturas passadas, com opções de filtro e ordenação.
6. O usuário pode clicar em uma assinatura na listagem para ver detalhes ou editar/cancelar.
7. O usuário pode fechar o aplicativo e, ao reabri-lo, as informações persistidas localmente ainda estarão disponíveis.

