# Tela 06 - Cadastro / Edicao de Assinatura

## Objetivo da tela

Permitir que o usuario cadastre ou atualize assinaturas com dados essenciais de cobranca, categoria e modalidade.

## Historias de usuario

### HU-CAD-01
Como usuario autenticado, quero cadastrar uma nova assinatura informando nome, valor, vencimento, modalidade e categoria para controlar meus gastos recorrentes.

### HU-CAD-02
Como usuario autenticado, quero editar uma assinatura existente para manter meus dados atualizados.

### HU-CAD-03
Como usuario autenticado, quero selecionar se a cobranca e mensal ou anual para refletir corretamente minha recorrencia.

### HU-CAD-04
Como usuario autenticado, quero cancelar a edicao sem perder contexto e com confirmacao quando houver alteracoes pendentes.

### HU-CAD-05
Como usuario autenticado, quero receber validacao clara dos campos obrigatorios para salvar sem erros.

## Criterios de aceite

- A tela deve conter campos para nome do servico, valor, modalidade, data de vencimento, categoria e URL.
- Os campos obrigatorios devem ser identificados visualmente.
- A modalidade deve usar SegmentedButton com opcoes `Mensal` e `Anual`.
- A categoria deve usar chips selecionaveis.
- O usuario deve conseguir salvar ou cancelar o fluxo.
- Se houver campos invalidos, a tela deve mostrar erro junto ao campo e impedir salvamento.
- Em caso de sucesso, o app deve persistir a assinatura e retornar para o contexto correto.

## Regras de negocio

- Campos obrigatorios minimos: nome, valor, modalidade, vencimento e categoria.
- A URL e opcional, mas deve ser validada quando preenchida.
- Em edicao, a tela deve carregar os valores atuais da assinatura.
- O formulario deve suportar tanto criacao quanto atualizacao sem duplicar logica visual.

## Observacoes UX/UI

- Componentes principais: Filled TextField, SegmentedButton, FilterChips, CTA primaria e secundaria.
- O step indicator do mockup sugere fluxo em etapas; se o produto mantiver tela unica, o indicador deve representar progresso real ou ser removido para evitar ambiguidade.
- O tema light deve preservar delimitacao dos campos e contraste da linha ativa do Filled TextField.
- Estados relevantes: preenchimento, erro de validacao, salvando e sucesso.