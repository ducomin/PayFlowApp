# Tela 07 - Detalhe da Assinatura

## Objetivo da tela

Dar ao usuario uma visao completa de uma assinatura, combinando status, custo, uso, historico de pagamentos e proximas acoes.

## Historias de usuario

### HU-DET-01
Como usuario autenticado, quero visualizar os dados completos de uma assinatura para entender seu custo e situacao atual.

### HU-DET-02
Como usuario autenticado, quero acompanhar o nivel de uso da assinatura para decidir se vale mantela.

### HU-DET-03
Como usuario autenticado, quero consultar o extrato de pagamentos para validar renovacoes anteriores.

### HU-DET-04
Como usuario autenticado, quero editar uma assinatura ativa para ajustar informacoes incorretas ou desatualizadas.

### HU-DET-05
Como usuario autenticado, quero cancelar uma assinatura ativa com seguranca para encerrar uma recorrencia que nao faz mais sentido.

## Criterios de aceite

- A tela deve exibir cabecalho com identidade da assinatura, categoria, status e modalidade.
- A tela deve apresentar informacoes essenciais como valor, proximo vencimento, tempo de relacionamento e total pago.
- O indicador de uso mensal deve mostrar percentual e interpretacao do nivel de uso.
- O extrato de pagamentos deve listar ao menos competencia, data e valor.
- Assinaturas ativas devem oferecer acoes de `Editar` e `Cancelar`.
- Assinaturas abertas a partir do Historico devem ficar em modo leitura, sem permitir edicao nem cancelamento.

## Regras de negocio

- O modo leitura deve ser determinado pela origem e pelo status da assinatura.
- O cancelamento deve exigir confirmacao explicita.
- O total pago deve considerar historico persistido ou valor simulado coerente no MVP.

## Observacoes UX/UI

- Componentes principais: hero card, grid de informacoes, LinearProgressIndicator, lista de pagamentos e acoes de rodape.
- O uso deve ser comunicado por numero, texto e cor sem depender apenas do progresso visual.
- No tema light, o hero precisa manter identidade da marca da assinatura sem prejudicar contraste dos textos e chips.