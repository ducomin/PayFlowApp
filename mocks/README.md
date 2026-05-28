# Mock API do PayFlowApp

Este diretório concentra a API fake usada pelo app Android para simular buscas de serviços e métricas de consumo mensal.

A implementação atual **não utiliza `json-server` diretamente**. Em vez disso, usa um servidor Node.js simples com `@tinyhttp/app`, `@tinyhttp/cors` e `lowdb`, lendo os dados do arquivo `db.json`.

## Objetivo

A mock API existe para:

- listar serviços disponíveis para cadastro de assinatura;
- permitir busca incremental por nome do serviço;
- retornar consumo mensal por usuário e por mês de referência;
- desacoplar o app Android de uma API real durante o desenvolvimento.

## Estrutura do diretório

```text
mocks/
├── db.json
├── package.json
├── package-lock.json
├── server.js
├── README.md
└── node_modules/
```

## Legenda da estrutura

- `mocks/`: raiz da API fake local do projeto.
- `db.json`: base de dados mock em JSON, lida em memória/arquivo pelo `lowdb`.
- `package.json`: manifesto Node.js com scripts e dependências do servidor mock.
- `package-lock.json`: arquivo gerado pelo npm para travar versões instaladas.
- `server.js`: servidor HTTP responsável por expor os endpoints REST fake.
- `README.md`: documentação específica deste diretório.
- `node_modules/`: dependências instaladas localmente pelo npm.

## Tecnologias usadas

- **Node.js**
- **@tinyhttp/app**: servidor HTTP leve
- **@tinyhttp/cors**: habilita CORS para acesso pelo app Android
- **lowdb**: leitura/escrita simples em arquivo JSON

## Pré-requisitos

- Node.js instalado
- npm disponível no terminal

## Como rodar

No Windows PowerShell:

```powershell
cd .\mocks
npm install
npm start
```

No Linux/macOS:

```bash
cd ./mocks
npm install
npm start
```

Ao subir, o servidor inicia por padrão em:

```text
http://localhost:3000
```

## Endpoints disponíveis

### 1. Listar todos os serviços

```http
GET /api/v1/streamings
```

Exemplo:

```text
http://localhost:3000/api/v1/streamings
```

### 2. Buscar serviços por nome

```http
GET /api/v1/streamings/search?nome=net
```

Regras:

- a busca é case-insensitive;
- o filtro usa correspondência parcial com `includes()`;
- se `nome` vier vazio, retorna todos os registros.

Exemplo:

```text
http://localhost:3000/api/v1/streamings/search?nome=glob
```

### 3. Consultar consumo mensal por usuário

```http
GET /api/v1/streamings/:username/consumo_mensal?nome=netflix&anomes=2026-05
```

Parâmetros:

- `username`: identificador do usuário na URL;
- `nome`: nome do serviço;
- `anomes`: mês de referência no formato `YYYY-MM`.

Exemplo:

```text
http://localhost:3000/api/v1/streamings/jose/consumo_mensal?nome=netflix&anomes=2026-05
```

## Estrutura do `db.json`

O arquivo possui atualmente duas coleções principais:

### `streamings`

Catálogo de serviços exibidos no autocomplete/pesquisa do app.

Campos principais:

- `id`
- `nome`
- `logo_url`
- `categoria_principal`

### `consumo_mensal`

Base de referência para identificar serviços pouco usados.

Campos principais:

- `streaming_id`
- `nome`
- `username`
- `mes_referencia`
- `total_dias_no_mes`
- `dias_utilizados`
- `total_minutos_mes`

## Observações de integração com Android

- No emulador Android, a base URL normalmente deve usar `10.0.2.2` no lugar de `localhost`.
- Exemplo de base URL para o app:

```text
http://10.0.2.2:3000/
```

- Para dispositivo físico, substitua por IP da máquina na rede local.

## Fluxo de manutenção

Quando quiser adicionar novos serviços ou ampliar o mock:

1. edite o arquivo `db.json`;
2. salve o arquivo;
3. reinicie o servidor, se necessário;
4. teste os endpoints no navegador, PowerShell ou app Android.

