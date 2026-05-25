# Visao de Produto e Tema

## Objetivo do MVP

PayFlow e um aplicativo Android para organizar assinaturas e gastos recorrentes, ajudando o usuario a visualizar custos mensais, identificar servicos pouco utilizados, acompanhar vencimentos e manter historico das assinaturas.

## Proposta de valor

- Centralizar assinaturas ativas e passadas em um unico fluxo.
- Evidenciar economia potencial com alertas de baixo uso.
- Reduzir esquecimentos de cobrancas por meio de notificacoes e indicadores de vencimento.
- Permitir cadastro, edicao, consulta e cancelamento de assinaturas com navegacao simples.

## Diretriz de UX/UI

- Tema principal: dark nativo.
- Tema alternativo: light opcional, acionado por configuracao do usuario ou preferencia do sistema.
- Design system: Material Design 3.
- Paleta principal: Teal `#4DD0E1` e Amber `#FFD54F`.
- Hierarquia visual: surfaces tonais em 5 niveis.
- Navegacao principal: CenterAligned TopAppBar + NavigationBar com pill indicator.
- CTA principal: Extended FAB `Nova Assinatura`.

## Requisitos transversais

### Funcionais

- O app deve suportar autenticacao inicial.
- O app deve permitir listar assinaturas ativas e historicas.
- O app deve permitir criar, editar, visualizar e cancelar assinaturas.
- O app deve exibir notificacoes de vencimento, promocao e baixo uso.
- O app deve persistir os dados localmente e consumir ao menos uma API fake.

### UX/UI

- Todos os componentes devem ter equivalencia visual e funcional nos modos dark e light.
- O modo dark e a referencia visual primaria para implementacao do MVP.
- O modo light deve preservar tokens semanticos, legibilidade e estados visuais.
- A interface deve prever estados de loading, sucesso, vazio e erro nas telas principais.
- Os elementos interativos devem respeitar touch target minimo de 48dp.

### Regras de tema

- Nao usar cores hardcoded nos componentes de produto; usar tokens semanticos.
- `primary`, `secondary`, `surface`, `surfaceContainer*`, `error` e `success` devem mapear para dark e light.
- Indicadores de status nao podem depender apenas de cor; devem incluir texto ou icone.
- O toggle de tema deve existir nas configuracoes de Perfil e aceitar ao menos: `Claro`, `Escuro` e `Seguir sistema`.

## Mapa de telas do MVP

1. Login/Autenticacao
2. Home/Dashboard
3. Perfil
4. Notificacoes
5. Historico
6. Cadastro/Edicao de Assinatura
7. Detalhe da Assinatura