# Tela 05 - Historico

## Objetivo da tela

Permitir consulta e analise das assinaturas encerradas, canceladas ou expiradas, apoiando aprendizado e acompanhamento de economia.

## Historias de usuario

### HU-HIST-01
Como usuario autenticado, quero ver minhas assinaturas passadas para manter um registro do que ja contratei.

### HU-HIST-02
Como usuario autenticado, quero filtrar o historico por status e periodo para encontrar eventos passados com rapidez.

### HU-HIST-03
Como usuario autenticado, quero visualizar indicadores de canceladas, expiradas.

### HU-HIST-04
Como usuario autenticado, quero buscar por nome do servico no historico para localizar uma assinatura especifica.

### HU-HIST-05
Como usuario autenticado, quero abrir o detalhe de uma assinatura historica em modo leitura para consultar contexto e datas.

## Criterios de aceite

- A tela deve conter busca, filtros selecionaveis e lista de itens historicos.
- O resumo superior deve informar quantidade de canceladas, expiradas e valor economizado.
- Cada item do historico deve exibir nome, categoria, valor, status e datas relevantes.
- Itens com status `Cancelada` e `Expirada` devem ter identificacao visual distinta.
- Ao tocar em um item, a navegacao deve abrir Detalhe da Assinatura em modo leitura quando a assinatura nao estiver mais ativa.

## Regras de negocio

- O Historico deve listar apenas assinaturas passadas, nunca as ativas.
- O valor `economizados` pode ser real ou calculado por regra simplificada no MVP, desde que fique consistente.
- Acoes de edicao devem estar desabilitadas quando o item estiver em modo historico somente leitura.

## Observacoes UX/UI

- Componentes principais: SearchBar, FilterChips, cards de historico, status chips.
- O modo dark deve usar surfaces diferenciadas para resumo e lista, mantendo legibilidade dos status.
- O estado vazio deve incentivar o retorno para Home, nao o cadastro de itens historicos manualmente.