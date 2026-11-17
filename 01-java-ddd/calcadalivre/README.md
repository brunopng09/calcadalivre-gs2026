# CalçadaLivre

Sistema que mapeia trechos de calçada, registra barreiras de acessibilidade e calcula a
prioridade de manutenção a partir do índice de acessibilidade e do tipo de via.

O projeto foi construído para a **Global Solution 2026.2 da FIAP**, disciplina de **DDD**
(Design Dirigido a Domínio), usando **Java 21**, **Spring Boot** (Web MVC + Thymeleaf),
**JDBC puro** com **Oracle** e empacotamento em **Docker**, com deploy no **Render**.

## Arquitetura em camadas

```
                   +-----------------------------------+
   Navegador ----> | controller (REST + Thymeleaf)      |
   Cliente API --> +-----------------------------------+
                              |
                              v
                   +-----------------------------------+
                   | bo (regras de negócio)             |
                   | UsuarioBO, TrechoBO,               |
                   | RegistroBarreiraBO,                |
                   | AvaliacaoTrechoBO                  |
                   +-----------------------------------+
                              |
                              v
                   +-----------------------------------+
                   | dao (JDBC puro, PreparedStatement) |
                   +-----------------------------------+
                              |
                              v
                   +-----------------------------------+
                   | conexoes.ConexaoFactory            |
                   +-----------------------------------+
                              |
                              v
                   +-----------------------------------+
                   | Oracle FIAP (schema com CL_*)      |
                   +-----------------------------------+
```

Elementos de apoio:

- `entities` — POJOs JavaBean que espelham as tabelas (`CL_USUARIO`, `CL_TRECHO`,
  `CL_TIPO_BARREIRA`, `CL_REGISTRO_BARREIRA`, `CL_AVALIACAO_TRECHO`).
- `excecoes` — exceções da aplicação (`CalcadaLivreException`, `RegraNegocioException`,
  `RecursoNaoEncontradoException`, `PersistenciaException`) e o `GlobalExceptionHandler`,
  que traduz essas exceções em respostas JSON de 400/404/500.

Estrutura de pacotes:

```
src/main/java/br/com/fiap/
├── main/CalcadaLivreApplication.java
├── conexoes/ConexaoFactory.java
├── entities/  (Usuario, Trecho, TipoBarreira, RegistroBarreira, AvaliacaoTrecho)
├── dao/       (UsuarioDAO, TrechoDAO, TipoBarreiraDAO, RegistroBarreiraDAO, AvaliacaoTrechoDAO)
├── bo/        (UsuarioBO, TrechoBO, RegistroBarreiraBO, AvaliacaoTrechoBO)
├── excecoes/  (exceções + GlobalExceptionHandler)
└── controller/ (UsuarioController, TrechoController, BarreiraController,
                 AvaliacaoController, WebController)
```

## Regras de negócio

- **Usuário**: nome obrigatório e e-mail válido e único.
- **Trecho**: nome da rua obrigatório; índice de acessibilidade entre 0 e 10; não é permitido
  excluir um trecho que já tenha registros de barreira vinculados
  (`RegraNegocioException`).
- **Registro de barreira**: gravidade entre 1 e 5; data de registro não pode ser futura;
  trecho e usuário precisam existir.
- **Avaliação do trecho**: nota (índice avaliado) entre 0 e 10.

## Como rodar localmente

Pré-requisitos: **Java 21+** (o wrapper do Maven já está no repositório). No PowerShell:

```powershell
cd 01-java-ddd\calcadalivre

$env:DB_USUARIO="SEU_USUARIO_FIAP"
$env:DB_SENHA="SUA_SENHA_FIAP"

.\mvnw.cmd spring-boot:run
```

A aplicação sobe em <http://localhost:8080>. A conexão usa
`jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`; se as variáveis `DB_USUARIO`/`DB_SENHA`
não estiverem definidas, a aplicação falha com uma mensagem explicando como configurá-las.
A variável opcional `DB_URL` sobrescreve a URL padrão do banco.

Para gerar o pacote executável:

```powershell
.\mvnw.cmd -B -DskipTests clean package
java -jar target/calcadalivre-0.0.1-SNAPSHOT.jar
```

## Como criar o banco

Os scripts SQL estão em `02-database` e devem ser executados **nesta ordem**, no
schema do usuário FIAP (SQL Developer, VS Code + extensão Oracle ou similar):

| Ordem | Script | Conteúdo |
|-------|--------|----------|
| 1 | `02-database/01_ddl_calcadalivre.sql` | cria as 14 tabelas `CL_*`, constraints e índices (idempotente) |
| 2 | `02-database/02_dml_calcadalivre.sql` | carga de dados de exemplo para demonstração |
| 3 | `02-database/03_dql_calcadalivre.sql` | consultas de verificação e relatórios |

## Endpoints da API REST

Todas as respostas são `application/json`. Criações devolvem `201 Created` com o cabeçalho
`Location`; exclusões devolvem `204 No Content`.

### Usuários — `/api/usuarios`

| Método | Rota | Função |
|--------|------|--------|
| GET | `/api/usuarios` | lista todos os usuários |
| GET | `/api/usuarios/{id}` | busca um usuário pelo id |
| POST | `/api/usuarios` | cria um usuário (e-mail válido e único) |
| PUT | `/api/usuarios/{id}` | atualiza um usuário |
| DELETE | `/api/usuarios/{id}` | exclui um usuário |

### Trechos — `/api/trechos`

| Método | Rota | Função |
|--------|------|--------|
| GET | `/api/trechos` | lista todos os trechos |
| GET | `/api/trechos/{id}` | busca um trecho pelo id |
| GET | `/api/trechos/prioridade` | média do índice de acessibilidade por tipo de via (ordem decrescente) |
| POST | `/api/trechos` | cria um trecho |
| PUT | `/api/trechos/{id}` | atualiza um trecho |
| DELETE | `/api/trechos/{id}` | exclui um trecho sem registros de barreira |

### Barreiras — `/api/barreiras`

| Método | Rota | Função |
|--------|------|--------|
| GET | `/api/barreiras` | lista todos os registros de barreira |
| GET | `/api/barreiras/{id}` | busca um registro pelo id |
| GET | `/api/barreiras/trecho/{idTrecho}` | lista as barreiras de um trecho |
| GET | `/api/barreiras/tipos` | lista os tipos de barreira disponíveis |
| POST | `/api/barreiras` | registra uma barreira (gravidade 1 a 5) |
| PUT | `/api/barreiras/{id}` | atualiza um registro de barreira |
| DELETE | `/api/barreiras/{id}` | exclui um registro de barreira |

### Avaliações — `/api/avaliacoes`

| Método | Rota | Função |
|--------|------|--------|
| GET | `/api/avaliacoes/trecho/{idTrecho}` | lista as avaliações de um trecho |
| POST | `/api/avaliacoes` | cria uma avaliação (nota 0 a 10) |
| DELETE | `/api/avaliacoes/{id}` | exclui uma avaliação |

Exemplo de requisição:

```powershell
$corpo = '{"idTrecho":1,"idUsuario":1,"idTipoBarreira":2,"nrGravidade":4,"dsRegistro":"Bueiro aberto"}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/barreiras `
  -ContentType "application/json" -Body $corpo
```

## Páginas web (Thymeleaf)

| Método | Rota | Função |
|--------|------|--------|
| GET | `/` | painel com total de trechos, total de barreiras e lista resumida |
| GET | `/trechos` | tabela de trechos com larguras, inclinação, índice e barreiras |
| GET | `/barreiras/nova` | formulário de cadastro de barreira |
| POST | `/barreiras/salvar` | grava a barreira e redireciona com mensagem de sucesso ou erro |

O layout é mobile-first, sem JavaScript e sem bibliotecas externas; as cores da marca estão
em `src/main/resources/static/css/estilo.css`
(primária `#0B2545`, sucesso `#00A86B`, alerta `#FFB703`, perigo `#D90429`,
superfície `#F8FAFC`).

## Deploy no Render

1. Suba o projeto para o GitHub (repositório; o projeto está na pasta `01-java-ddd/calcadalivre`).
2. No painel do Render, escolha **New → Web Service**.
3. Conecte o repositório e selecione o runtime **Docker**.
4. Configure **Root Directory** como `01-java-ddd/calcadalivre` e **Dockerfile Path** como
   `./Dockerfile`.
5. Em **Environment**, cadastre as variáveis:
   - `DB_USUARIO` — usuário do schema Oracle FIAP;
   - `DB_SENHA` — senha do schema Oracle FIAP.
6. Confirme o **Health Check Path** como `/` e faça o deploy. O Render injeta a porta em
   `PORT`, já lida por `server.port=${PORT:8080}`.

O arquivo `render.yaml` na pasta `01-java-ddd/calcadalivre` descreve o mesmo serviço e pode ser usado com
**New → Blueprint** (as credenciais ficam com `sync: false`, ou seja, são informadas no
painel).

Repositório: <https://github.com/brunopng09/calcadalivre-gs2026>
