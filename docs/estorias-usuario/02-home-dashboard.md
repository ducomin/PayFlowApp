# Tela 02 - Home / Dashboard

## Objetivo da tela

Apresentar o panorama mensal do usuario, priorizando visibilidade de custos, assinaturas ativas, baixo uso e atalhos para os fluxos mais importantes.

## Historias de usuario

### HU-HOME-01
Como usuario autenticado, quero ver o total mensal das minhas assinaturas para entender meu compromisso recorrente atual.

### HU-HOME-02
Como usuario autenticado, quero identificar assinaturas pouco utilizadas para avaliar oportunidades de economia.

### HU-HOME-03
Como usuario autenticado, quero buscar e filtrar assinaturas por categoria para encontrar rapidamente um servico.

### HU-HOME-04
Como usuario autenticado, quero acessar os detalhes de uma assinatura pela lista para consultar status, vencimento e uso.

### HU-HOME-05
Como usuario autenticado, quero usar a FAB `Nova Assinatura` para cadastrar rapidamente um novo servico.

### HU-HOME-06
Como usuario autenticado, quero navegar por Home, Historico, Avisos e Perfil pela barra inferior para manter orientacao no app.

## Criterios de aceite

- A tela deve exibir saudacao personalizada, referencia de mes e resumo financeiro.
- O resumo deve informar ao menos: total mensal, quantidade de assinaturas ativas, quantidade pouco usadas e quantidade com vencimento imediato.
- Deve existir barra de busca e FilterChips selecionaveis para categorias.
- Cada card de assinatura ativa deve mostrar nome, valor, status, vencimento e LinearProgressIndicator de uso.
- Assinaturas pouco utilizadas devem ter destaque visual semanticamente diferente das ativas comuns.
- A FAB `Nova Assinatura` deve permanecer visivel sem competir com a NavigationBar.
- A NavigationBar deve ter pill indicator no item ativo e badge em notificacoes quando houver pendencias.

## Regras de negocio

- A lista da Home deve conter apenas assinaturas ativas.
- O status `Pouco usada` deve ser calculado por criterio definido pelo produto ou simulado no MVP.
- O item `Vence hoje` deve priorizar exibicao no resumo e refletir alertas do modulo de notificacoes.
- Ao tocar em uma assinatura, o usuario deve ser levado para a tela de detalhe.

## Observacoes UX/UI

- Componentes principais: TopAppBar, SearchBar, FilterChips, Elevated Cards, Extended FAB, NavigationBar.
- O tema dark usa surfaces tonais para separar resumo, cards e barras de navegacao.
- O tema light deve manter os mesmos tokens semanticos, sem perder contraste em chips, indicadores e badges.
- Estados necessarios: loading com skeleton, vazio com CTA para cadastrar assinatura, erro com retry e sucesso com dados populados.