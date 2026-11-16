# Registro do uso de IA

A IA foi usada como **copiloto** — para gerar rascunho, tirar dúvidas e revisar — nunca como substituta da equipe.
Todo código e texto foi lido, executado e corrigido por nós antes da entrega. As seções abaixo registram os prompts
representativos e o que foi feito com cada resposta.

---

## Java / DDD

**Prompt 1 — estrutura do projeto**

> Monte a estrutura de um projeto Spring Boot seguindo o padrão em camadas do material da disciplina: pacotes
> `main`, `conexoes`, `entities`, `dao`, `bo`, `excecoes`, `controller`. Use JDBC puro com `PreparedStatement`, sem
> ORM. Entidades: Trecho, Usuario, RegistroBarreira, AvaliacaoTrecho e TipoBarreira, espelhando as 14 tabelas do
> modelo relacional.

*Resultado usado:* esqueleto dos pacotes e dos DAOs. **Revisão nossa:** conferimos cada `SELECT`/`INSERT` contra o DDL
e ajustamos tipos e nomes de coluna.

**Prompt 2 — tratamento de exceções**

> Crie a hierarquia de exceções do projeto com uma raiz `checked` e um `@RestControllerAdvice` que traduza
> `RegraNegocioException` em 400, `RecursoNaoEncontradoException` em 404 e `PersistenciaException` em 500, sempre com
> um JSON de erro no mesmo formato (`timestamp`, `status`, `erro`, `mensagem`, `caminho`).

*Resultado usado:* base do handler. **Revisão nossa:** testamos os três códigos com chamadas reais à API.

**Prompt 3 — páginas Thymeleaf**

> Monte as páginas web com Thymeleaf: painel inicial, lista de trechos com o índice de acessibilidade e formulário
> para registrar uma barreira. O CSS deve seguir a identidade visual da CalçadaLivre (azul #0B2545, verde #00A86B,
> amarelo #FFB703, vermelho #D90429), com corpo de no mínimo 18 px e alvos de toque de 56 px.

*Resultado usado:* marcação das telas. **Revisão nossa:** ajustamos o layout e testamos em janela estreita.

---

## Documentação técnica (PDF)

**Prompt 4 — estrutura do documento**

> Escreva a documentação técnica do projeto em HTML/CSS paginado (A4) seguindo a rubrica: capa, sumário, objetivo,
> arquitetura em camadas, diagrama de classes, API REST, protótipos de tela, execução e deploy, e especificação
> técnica com decisões de projeto.

*Resultado usado:* texto-base de várias seções. **Revisão nossa:** reescrevemos o conteúdo técnico, refizemos o
diagrama de classes e ajustamos a paginação página por página.

---

## Database

**Prompt 5 — modelagem e scripts**

> A partir do modelo da CalçadaLivre (trechos de calçada, barreiras, avaliações e reformas), monte o modelo relacional
> normalizado em Oracle com prefixo `CL_`, gere o DDL com constraints de integridade e o DML com no mínimo 5 linhas
> por tabela, respeitando as chaves estrangeiras.

*Resultado usado:* rascunho do DDL e do DML. **Revisão nossa:** executamos tudo no Oracle 19c da FIAP (135 comandos DDL
e 118 de DML, sem erro) e conferimos a contagem de registros por tabela.

**Prompt 6 — consultas de análise**

> Escreva três consultas DQL que atendam aos critérios: uma com INNER JOIN, uma com INNER JOIN + GROUP BY e uma com
> LEFT OUTER JOIN + WHERE + ORDER BY, todas com significado real para o problema da acessibilidade.

*Resultado usado:* as três consultas, executadas e validadas no banco.

---

## Diagramas

**Prompt 7 — diagrama de classes**

> Gere o diagrama de classes como SVG, em uma página A4 paisagem, com faixas por camada (controller, bo, dao,
> entities), sem sobreposição de rótulos e com distinção visual entre chamada e dependência.

*Resultado usado:* primeira versão do SVG. **Revisão nossa:** o diagrama foi refeito três vezes — o script final é
nosso, com as coordenadas recalculadas e o resultado conferido em imagem renderizada.

---

## O que não foi feito pela IA

- Leitura e correção de todo o código gerado.
- Execução do projeto, do DDL/DML/DQL no Oracle e validação das consultas.
- Definição do problema, do trajeto, dos perfis de mobilidade e dos limites técnicos (NBR 9050).
- Conferência dos dados de campo e das larguras de calçada usadas nos exemplos.
