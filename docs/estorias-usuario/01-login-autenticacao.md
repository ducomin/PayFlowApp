# Tela 01 - Login / Autenticacao

## Objetivo da tela

Permitir que o usuario entre no aplicativo com rapidez e seguranca, iniciando o fluxo principal do PayFlow.

## Historias de usuario

### HU-LOGIN-01
Como usuario recorrente, quero informar meu e-mail e senha para acessar minhas assinaturas salvas.

### HU-LOGIN-02
Como usuario recorrente, quero ativar `Lembrar acesso` para reduzir atrito em acessos futuros.

### HU-LOGIN-03
Como usuario que esqueceu a senha, quero acessar a opcao `Esqueci a senha` para recuperar o acesso.

### HU-LOGIN-04
Como novo usuario, quero acessar `Criar nova conta` para iniciar meu cadastro no aplicativo.

## Criterios de aceite

- A tela deve exibir logo, nome do produto, subtitulo e campos de e-mail e senha.
- O campo senha deve permitir revelar e ocultar conteudo.
- O botao `Entrar` deve ser a CTA primaria e ficar claramente destacada nos modos dark e light.
- O usuario deve conseguir ativar ou desativar `Lembrar acesso` com feedback visual imediato.
- Em caso de credenciais invalidas, a tela deve apresentar mensagem de erro clara e acao para tentar novamente.
- Em caso de sucesso, a navegacao deve levar para Home/Dashboard.

## Regras de negocio

- Autenticacao pode ser fake no MVP, mas precisa simular sucesso e falha.
- Se `Lembrar acesso` estiver ativo, a sessao deve ser reaproveitada conforme regra definida pelo time.
- O login nao pode bloquear o acesso sem feedback de loading durante a validacao.

## Observacoes UX/UI

- Componente principal: M3 Filled TextField.
- O modo dark e a referencia visual base; o modo light deve manter o mesmo layout e hierarquia.
- A tela deve contemplar estados de loading, erro e sucesso.