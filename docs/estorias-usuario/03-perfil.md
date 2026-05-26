# Tela 03 - Perfil

## Objetivo da tela

Oferecer ao usuario uma visao resumida da propria conta, plano e configuracoes pessoais, incluindo controle de tema e saida da sessao.

## Historias de usuario

### HU-PERFIL-01
Como usuario autenticado, quero visualizar meu nome, e-mail e plano para confirmar meus dados de conta.

### HU-PERFIL-02
Como usuario autenticado, quero ver indicadores resumidos das minhas assinaturas para entender meu perfil de uso.

### HU-PERFIL-03
Como usuario autenticado, quero acessar configuracoes de privacidade e notificacoes para ajustar preferencias do app.

### HU-PERFIL-04
Como usuario autenticado, quero alternar entre tema claro, escuro ou seguir sistema para usar a interface conforme minha preferencia.

### HU-PERFIL-05
Como usuario autenticado, quero sair da conta com seguranca para encerrar minha sessao.

## Criterios de aceite

- A tela deve exibir avatar, nome, e-mail e identificacao do plano do usuario.
- A tela deve apresentar indicadores resumidos de assinaturas ativas, gasto mensal e quantidade de categorias.
- Deve haver lista de configuracoes com acessos a `Privacidade e seguranca`, `Notificacoes` e `Tema`.
- A opcao de tema deve suportar no minimo `Claro`, `Escuro` e `Seguir sistema`.
- O botao `Sair da conta` deve exigir confirmacao antes de encerrar a sessao.

## Regras de negocio

- A alteracao de tema deve persistir localmente.
- Quando `Seguir sistema` estiver ativo, o app deve responder a mudancas do tema do dispositivo.
- O logout deve limpar o contexto autenticado e retornar para Login.

## Observacoes UX/UI

- Componentes principais: hero de perfil, cards de resumo, lista de configuracoes, switch/selector de tema.
- O mockup v3 mostra switch M3; para contemplar claro, escuro e sistema, o produto pode evoluir para segmented control, modal selector ou tela secundaria.
- No modo light, o hero precisa manter destaque sem comprometer legibilidade dos textos sobre gradiente.