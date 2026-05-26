# Tela 04 - Notificacoes

## Objetivo da tela

Concentrar alertas relevantes para que o usuario aja sobre vencimentos, promocoes e sinais de baixo uso no momento certo.

## Historias de usuario

### HU-NOTIF-01
Como usuario autenticado, quero ver notificacoes agrupadas por periodo para priorizar minha leitura.

### HU-NOTIF-02
Como usuario autenticado, quero identificar rapidamente alertas criticos de vencimento para evitar cobrancas esquecidas.

### HU-NOTIF-03
Como usuario autenticado, quero receber alertas de baixo uso para decidir se devo manter ou cancelar uma assinatura.

### HU-NOTIF-04
Como usuario autenticado, quero marcar todas as notificacoes como lidas para limpar minha fila de avisos.

### HU-NOTIF-05
Como usuario autenticado, quero abrir o detalhe relacionado a uma notificacao para agir sem procurar manualmente a assinatura.

## Criterios de aceite

- A tela deve agrupar notificacoes por periodos como `Hoje` e `Esta semana`.
- A tela deve exibir contador de notificacoes nao lidas.
- Cada notificacao deve apresentar tipo, titulo, descricao resumida, horario ou data e acao quando aplicavel.
- Alertas criticos devem ter destaque semantico diferente de avisos informativos.
- O comando `Ler todas` deve atualizar o estado visual da lista e o badge da NavigationBar.
- Notificacoes acionaveis devem permitir navegar para o detalhe da assinatura ou tela relacionada.

## Regras de negocio

- Tipos minimos de notificacao: vencimento, baixo uso, promocao e confirmacao de renovacao.
- O badge da NavigationBar deve refletir apenas itens nao lidos.
- Notificacoes lidas devem continuar disponiveis na lista ate regra de retencao definida.

## Observacoes UX/UI

- Componentes principais: cards de alerta com destaque por severidade, agrupamento por secao e badge semantico.
- O uso de cor deve ser acompanhado de icone e copy para nao depender apenas de contraste cromatico.
- Deve existir estado vazio com mensagem como `Nenhum aviso no momento`.