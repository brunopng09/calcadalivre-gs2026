# CalçadaLivre — Global Solution 2026.2

**FIAP · Engenharia de Software · 2026.2**
Entregas do semestre: DDD/Java, Database, Data Science, Dynamic Programming, Network e AR/VR.

---

## Integrantes

| RM | Nome |
|---|---|
| RM562159 | Bruno Costa Scuciato |
| RM561613 | Gustavo Torres Caldeira |
| RM566222 | Luiz Fernando Balbino |

---

## Ficha da CitizenTech

| Campo | Preenchimento |
|---|---|
| Nome | CalçadaLivre |
| Link do repositório | https://github.com/brunopng09/calcadalivre-gs2026 |
| Cidade e bairro | Suzano, SP — da Estação Suzano até a Santa Casa de Suzano |
| Cidadão beneficiado | Pessoas com deficiência física, idosos e mães e pais com carrinho de bebê |
| Problema, em uma frase | A Estação Suzano tem acessibilidade precária (o elevador vive em manutenção) e as calçadas até a Santa Casa são irregulares, estreitas ou quebradas, o que impede quem tem mobilidade reduzida de chegar ao atendimento de saúde |
| ODS conectados | 3 (Saúde e bem-estar), 10 (Redução das desigualdades) e 11 (Cidades e comunidades sustentáveis) |

---

## Como funciona

O trajeto de quem chega de trem à Estação Suzano e precisa de atendimento na Santa Casa de Suzano é modelado como um
grafo: cada ponto do caminho é um nó e cada trecho de calçada (ou a ligação da estação) é uma aresta, com largura
livre, piso, inclinação, degrau e ocupação.

| Camada | O que faz |
|---|---|
| **Data Science** | Prevê o índice de acessibilidade de cada trecho (regressão linear) e a probabilidade de ele ser intransitável para cadeira de rodas (regressão logística) |
| **Dynamic Programming** | Encontra a rota que a pessoa **consegue** percorrer (Dijkstra por perfil), avisa onde não há passagem e prioriza reformas com mochila 0/1 |
| **Java / DDD** | API REST + páginas web que cadastram trechos, barreiras e avaliações, com validações de negócio e tratamento de exceções |
| **Database (Oracle)** | Modelo relacional com 14 tabelas `CL_*`, DDL, DML e as consultas de análise (DQL) |
| **AR/VR** | Experiência imersiva do trajeto para conscientização |
| **Network** | Topologia de rede que sustenta coleta e consulta dos dados |

---

## Estrutura do repositório

```
.
├── 01-java-ddd/              API REST Spring Boot (DDD) e documentação técnica
│   └── calcadalivre/         projeto Maven completo (mvnw, Dockerfile, render.yaml)
├── 02-database/              DDL, DML, DQL e PDF do modelo de dados (Oracle)
├── 03-data-science/          notebook, base sintética e gerador da base
├── 04-dynamic-programming/   grafo, Dijkstra, Floyd-Warshall, memoização e mochila 0/1
├── 05-network/               entrega de Network (Packet Tracer)
├── 06-ar-vr/                 entrega de AR/VR (vídeo e PDF)
├── assets/                   identidade visual (paleta, tipografia, logos)
└── prompts.md                registro do uso de IA como copiloto
```

---

## Como executar

### Java (API REST + páginas web)

Requisitos: **Java 21 ou superior** (testado com Java 25). O projeto usa o Maven Wrapper — não é necessário instalar Maven.

```bash
cd 01-java-ddd/calcadalivre
./mvnw spring-boot:run          # Linux/macOS
mvnw.cmd spring-boot:run        # Windows
```

A aplicação sobe em `http://localhost:8080`.

Para conectar ao banco Oracle, defina as variáveis de ambiente (o professor disponibiliza as credenciais):

```bash
export DB_USUARIO=seu_usuario
export DB_SENHA=sua_senha
```

Páginas web: `/` (painel), `/trechos` (lista de trechos), `/barreiras/nova` (registro de barreira).
API REST: `/api/usuarios`, `/api/trechos`, `/api/barreiras`, `/api/avaliacoes`.

### Database (Oracle)

Executar na ordem, no SQL Developer ou SQL*Plus:

```
02-database/01_ddl_calcadalivre.sql
02-database/02_dml_calcadalivre.sql
02-database/03_dql_calcadalivre.sql
```

Validado em Oracle 19c (FIAP): DDL 135/135 comandos, DML 118/118, DQL 3/3 consultas.

### Data Science

Abrir `03-data-science/CalcadaLivre_DataScience.ipynb` no Jupyter Notebook.
A base `calcadas_suzano.csv` é sintética e pode ser regerada com `python gerar_base.py`.

### Dynamic Programming

Requisitos: Python 3.9 ou superior, sem bibliotecas externas.

```bash
cd 04-dynamic-programming
python main.py
```

O programa executa as 8 funcionalidades e imprime os resultados no terminal.

---

## Entregáveis

| Disciplina | Arquivo |
|---|---|
| DDD / Java | `01-java-ddd/CalcadaLivre-DDD.pdf` e `01-java-ddd/calcadalivre/` |
| Database | `02-database/CalcadaLivre-Database-Design.pdf` |
| Data Science | `03-data-science/CalcadaLivre_DataScience.ipynb`, `calcadas_suzano.csv` e `integrantes.txt` |
| Dynamic Programming | `04-dynamic-programming/` (código + `map.osm`) e `04-dynamic-programming/README.md` |
| Network | `05-network/` |
| AR/VR | `06-ar-vr/EntregaARVR.pdf` e o vídeo em https://youtu.be/zg2j8Rqs2YM |

---

## Deploy

Aplicação Java publicada no Render: <!-- preencher após o deploy -->
`https://calcadalivre.onrender.com`

Variáveis de ambiente configuradas no painel do Render: `DB_USUARIO` e `DB_SENHA` (marcadas como secretas).

---

## Uso de IA

O uso de IA como copiloto é permitido na disciplina. Os prompts utilizados e a forma de revisão estão registrados em
[`prompts.md`](prompts.md); todo o código foi lido, executado e validado pela equipe.
