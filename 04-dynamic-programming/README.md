# CalçadaLivre — Dynamic Programming

Global Solution 2026.2 · FIAP · Engenharia de Software · Dynamic Programming

## Integrantes

| RM | Nome |
|---|---|
| RM562159 | Bruno Costa Scuciato |
| RM566222 | Luiz Fernando Balbino |
| RM561613 | Gustavo Torres Caldeira |

## Ficha da CitizenTech

| Campo | Preenchimento |
|---|---|
| Nome | CalçadaLivre |
| Cidade e bairro | Suzano, SP — da Estação Suzano até a Santa Casa de Suzano |
| Cidadão beneficiado | Pessoas com deficiência física, idosos e mães e pais com carrinho de bebê |
| Problema, em uma frase | A Estação Suzano tem acessibilidade precária (o elevador vive em manutenção) e as calçadas até a Santa Casa são irregulares, estreitas ou quebradas, o que impede quem tem mobilidade reduzida de chegar ao atendimento de saúde |
| ODS conectados | 3 (Saúde e bem-estar), 10 (Redução das desigualdades) e 11 (Cidades e comunidades sustentáveis) |

## Contexto desta entrega

O trajeto escolhido é o da **Estação Suzano até a Santa Casa de Suzano**, o caminho de quem chega de trem e precisa
de atendimento de saúde. O problema aparece em dois pontos:

1. **A estação.** Quando o elevador está em manutenção, só resta a escada, e uma pessoa em cadeira de rodas ou com
   carrinho de bebê simplesmente **não consegue sair da plataforma**.
2. **As calçadas.** Depois da estação, o caminho mais curto passa por calçadas estreitas, irregulares ou quebradas.
   Quem não passa por elas é **obrigado a descer para a rua**, entre os carros.

A CalçadaLivre modela o trajeto como um grafo, em que cada ponto é um nó e cada trecho de calçada (ou a ligação da
estação) é uma aresta. As calçadas **não têm a mesma largura**: cada trecho corresponde a uma rua (ou parte dela) com a
sua própria **largura livre**, além de metros, inclinação, degrau, piso quebrado e ocupação. O índice de
acessibilidade e o risco de cada trecho ser intransitável vêm dos modelos de **Data Science**. Com isso o sistema:

1. encontra a rota em que a pessoa **consegue passar**, de acordo com o seu perfil;
2. **avisa quais trechos da rota mais curta são intransitáveis** e por quê, inclusive quando o elevador da estação
   está fora de serviço;
3. **indica quando não existe rota acessível**, em vez de mandar a pessoa por um caminho impossível;
4. ajuda quem decide a **priorizar o que consertar primeiro** dentro de um orçamento limitado.

## Como executar

Requisitos: Python 3.9 ou superior. Não há bibliotecas externas.

```
python main.py
```

O programa executa todas as funcionalidades e imprime os resultados no terminal.

## Estrutura do projeto

| Arquivo | Conteúdo |
|---|---|
| `dados.py` | Pontos, trechos por rua e reformas candidatas, com o estado do elevador e os valores previstos pelos modelos de Data Science |
| `rotas.py` | Construção do grafo, regras por perfil, Dijkstra, Floyd-Warshall e memoização |
| `reformas.py` | Mochila 0/1 por tabulação |
| `main.py` | Demonstração das funcionalidades |

## Modelagem dos dados

### Pontos (nós)

| Nó | Local |
|---|---|
| P | Plataforma da Estação Suzano |
| A | Saída da estação (Rua Prudente de Moraes) |
| B | Esquina em frente à estação |
| H | Rua General Francisco Glicério (meio do comércio) |
| C | Fim do comércio da Glicério |
| I | Rua Baruel x Rua Professor Roberto David |
| J | Rua Marechal Deodoro x Rua Felício de Camargo |
| D | Rua Felício de Camargo x Av. Antônio Marques Figueira |
| K | Av. Antônio Marques Figueira (meio do trajeto) |
| E | Av. Antônio Marques Figueira x Rua Professor Roberto David |
| G | Santa Casa de Suzano (Rua Professor Roberto David, 499 - Vila Figueira) |

### Trechos (arestas)

Cada trecho tem: `origem`, `destino`, `rua`, `metros`, `inclinacao_pct`, `tem_degrau`, `largura_livre_m`, `buracos`
(0 = piso bom, 3 = muito quebrado), `ocupado` e três valores previstos pelos modelos do notebook de Data Science:
`indice` de acessibilidade (0 a 100, regressão linear), `p_intransitavel` (probabilidade de ser intransitável para
cadeira de rodas, regressão logística) e `gravidade` (1 a 5, calculada a partir da probabilidade).

| Trecho | Rua | Metros | Largura livre | Piso | Índice | Risco | Gravidade |
|---|---|---|---|---|---|---|---|
| P–A | Estação Suzano (elevador ou escada) | 30 | 1,50 m | bom | 30 (90 com elevador) | — | 5 (1 com elevador) |
| A–B | Rua Prudente de Moraes (saída da estação) | 50 | 1,50 m | bom | 93 | 4% | 1 |
| B–H | Rua General Francisco Glicério (início) | 400 | 0,95 m | pouco quebrado | 50 | 80% | 4 |
| H–C | Rua General Francisco Glicério (comércio) | 750 | 0,75 m | irregular | 35 | 98% | 5 |
| C–I | Rua Baruel | 500 | 0,70 m | muito quebrado | 39 | 98% | 5 |
| I–G | Rua Professor Roberto David (lado da Baruel) | 350 | 0,80 m | irregular, com degrau | 35 | 99% | 5 |
| H–J | Travessa Glicério → Marechal Deodoro | 250 | 1,00 m | pouco quebrado | 51 | 71% | 4 |
| C–K | Travessa Glicério → avenida | 400 | 0,85 m | irregular | 47 | 92% | 5 |
| B–J | Rua Marechal Deodoro | 300 | 1,30 m | pouco quebrado | 77 | 21% | 2 |
| J–D | Rua Felício de Camargo | 400 | 1,40 m | pouco quebrado | 79 | 27% | 2 |
| D–K | Av. Antônio Marques Figueira (início) | 1.000 | 1,30 m | pouco quebrado | 71 | 20% | 2 |
| K–E | Av. Antônio Marques Figueira (até a Santa Casa) | 450 | 1,60 m | bom | 91 | 2% | 1 |
| E–G | Rua Professor Roberto David (lado da avenida) | 100 | 1,40 m | pouco quebrado | 80 | 16% | 2 |

A rota mais curta (P–A–B–H–C–I–G) soma **2.080 m** e passa pelas ruas mais estreitas: a largura livre cai até 0,70 m.
A rota pela avenida (P–A–B–J–D–K–E–G) soma **2.330 m**, com larguras de 1,30 m a 1,60 m.

O estado do elevador é um parâmetro: `dados.montar_trechos(elevador_funcionando)`. Em manutenção, o trecho P–A passa
a ter escada e índice baixo; funcionando, deixa de ter degrau.

### Perfis e limites

Abaixo da largura mínima, com piso muito quebrado, com degrau (onde se aplica), acima da inclinação máxima ou, para
cadeira de rodas, com risco previsto acima de 30%, o trecho é **intransitável** para o perfil.

| Perfil | Largura livre mínima | Inclinação máxima | Degrau ou escada | Piso muito quebrado | Risco previsto (Data Science) |
|---|---|---|---|---|---|
| `cadeira` | 0,90 m | 8% | intransitável | intransitável | evitar a partir de 30% |
| `carrinho` | 0,80 m | 10% | intransitável | intransitável | não se aplica |
| `idoso` | 0,70 m | 12% | permitido | permitido (com custo maior) | não se aplica |

O modelo de risco foi treinado com avaliações feitas em cadeira de rodas, por isso só é aplicado a esse perfil. O
limiar de 0,3 é o mesmo adotado no notebook (`LIMIAR_RISCO_CADEIRA`, no `dados.py`). Os demais limites ficam em
`LIMITES`, no `rotas.py`, e podem ser ajustados.

## Descrição dos algoritmos

### 1. Grafos: Dijkstra com custo por perfil

**Problema que resolve.** Encontrar o melhor caminho entre dois pontos para um perfil de mobilidade
(`cadeira`, `carrinho` ou `idoso`).

**Modelagem.** Pontos são nós e trechos são arestas, guardados em lista de adjacência (grafo não direcionado).

**Custo de uma aresta.** O custo depende do perfil:

```
custo = metros x (1 + (100 - indice) / 50)
```

Um trecho com índice 100 custa só a distância; um trecho com índice 0 custa o triplo. Se o trecho é intransitável
para o perfil (função `motivos_bloqueio`), ele não entra no cálculo.

**Funcionamento.** O algoritmo mantém uma fila de prioridade (`heapq`) com os nós a visitar, ordenada pelo menor
custo acumulado. A cada passo retira o nó de menor custo, relaxa as arestas para os vizinhos e guarda o nó anterior
para reconstruir o caminho no final.

**Resultado.** O custo total e a lista de pontos do caminho. Se o destino não for alcançável, devolve "sem rota",
que é uma resposta válida: é o que acontece para cadeira e carrinho enquanto o elevador está em manutenção.

**Por que não a rota mais curta?** A rota mais curta ignora largura, piso e elevador, e leva a pessoa por trechos
onde ela não passa. A função `rota_mais_curta` existe para **comparar**, e `trechos_de_risco` lista, trecho a trecho,
onde a pessoa seria obrigada a ir pela rua (ou não conseguiria seguir) e o motivo.

**Complexidade.** O((V + E) log V), em que V é o número de pontos e E o de trechos.

### 2. Tabulação: Floyd-Warshall

**Problema que resolve.** Calcular de uma vez o menor custo entre **todos os pares** de pontos para um perfil, para
responder consultas rápidas sem rodar o Dijkstra de novo.

**Subproblema.** `dp[i][j]` é o menor custo de `i` até `j`. A tabela é construída de baixo para cima (tabulação):
começa com os custos das arestas diretas e, para cada nó `k` usado como intermediário, atualiza

```
dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])
```

**Resultado.** Uma matriz de custos; o valor infinito indica que não existe rota acessível entre o par. Com o
elevador em manutenção, a linha e a coluna da plataforma ficam sem rota para a cadeira de rodas.

**Complexidade.** O(V³) para montar a tabela, O(1) para consultar um par.

### 3. Memoização: cache de rotas

**Problema que resolve.** Muitas pessoas pedem as mesmas rotas (por exemplo, da estação até a Santa Casa).
Recalcular o Dijkstra a cada pedido desperdiça tempo.

**Funcionamento.** A função `rota_memo(perfil, origem, destino)` usa `functools.lru_cache`: o primeiro pedido calcula
e guarda o resultado, e os seguintes devolvem o valor guardado. Quando algo muda (um elevador é consertado ou uma
calçada é reformada), as rotas guardadas deixam de valer, então `definir_grafo` **limpa o cache**.

**Resultado.** Na demonstração, 6.000 consultas repetidas caem de cerca de 25 ms para menos de 1 ms com cache. Há
também um teste de **invalidação**: com o elevador em manutenção a cadeira de rodas fica sem rota; depois de
"consertar" o elevador e limpar o cache, a rota aparece.

**Complexidade.** A primeira consulta de cada combinação custa o mesmo que o Dijkstra; as repetidas são O(1).

### 4. Tabulação: mochila 0/1 de reformas

**Problema que resolve.** Com um orçamento limitado, decidir **o que consertar primeiro** para beneficiar o maior
número de pessoas (o elevador, as calçadas ou ambos).

**Modelagem.** Cada reforma é um item com um custo (em mil reais) e um benefício:

```
beneficio = pessoas_por_dia x gravidade
```

A `gravidade` (1 a 5) de cada trecho vem do modelo de regressão logística do notebook de Data Science. O custo das
calçadas é de cerca de R$ 200 por metro, e o conserto do elevador custa R$ 35 mil.

Cada item é reformado inteiro ou não (0/1).

**Subproblema.** `dp[i][b]` é o maior benefício possível usando os `i` primeiros itens com orçamento `b`. Para cada
item, a tabela compara "não reformar" (`dp[i-1][b]`) com "reformar" (`dp[i-1][b - custo] + beneficio`) e guarda o
maior. Depois, uma varredura de trás para frente reconstrói **quais** itens foram escolhidos.

**Resultado.** O benefício total e a lista de trechos a reformar.

**Complexidade.** O(n x orçamento), contra O(2^n) de testar todas as combinações.

## Descrição das funcionalidades

| # | Funcionalidade | Função | Algoritmo | Entrada | Saída |
|---|---|---|---|---|---|
| 1 | Buscar rota acessível | `rota_acessivel` | Dijkstra | grafo, origem, destino, perfil | custo e caminho (ou "sem rota") |
| 2 | Comparar com a rota mais curta | `rota_mais_curta` | Dijkstra (só distância) | grafo, origem, destino | caminho curto e distância |
| 3 | Avisar onde não dá para passar | `trechos_de_risco`, `motivos_bloqueio` | Regras por perfil + risco previsto | grafo, caminho, perfil | trechos intransitáveis e motivos |
| 4 | Rota com alerta | `rota_com_alerta` | Dijkstra | grafo, origem, destino, perfil | rota acessível, ou rota curta com alerta |
| 5 | Simular o estado do elevador | `montar_trechos` | Reconstrução do grafo | elevador funcionando ou não | grafo atualizado |
| 6 | Custo entre todos os pares | `floyd_warshall` | Tabulação | grafo, perfil | matriz de custos |
| 7 | Rotas repetidas rápidas | `rota_memo`, `definir_grafo` | Memoização | perfil, origem, destino | rota em cache |
| 8 | Priorizar reformas | `escolher_reformas` | Mochila 0/1 (tabulação) | custos, benefícios, orçamento | benefício total e trechos escolhidos |

## Exemplo de execução (Estação Suzano até a Santa Casa)

**Situação atual: elevador em manutenção**

```
Mais curta: P -> A -> B -> H -> C -> I -> G (2080 m)

[cadeira]
  A rota curta OBRIGARIA a ir pela rua ou a desistir do trajeto:
   - trecho P->A [Estação Suzano (elevador em manutenção: só há escada)]: degrau/escada
   - trecho B->H [Rua General Francisco Glicério (início)]: risco previsto de 80% de ser intransitável (modelo de Data Science)
   - trecho H->C [Rua General Francisco Glicério (comércio)]: calçada estreita: 0.75 m livres, mínimo 0.90 m (calçada ocupada); risco previsto de 98% ...
   - trecho C->I [Rua Baruel]: calçada estreita: 0.70 m livres, mínimo 0.90 m; piso muito quebrado; risco previsto de 98% ...
   - trecho I->G [Rua Professor Roberto David (lado da Baruel)]: calçada estreita: 0.80 m livres, mínimo 0.90 m; degrau/escada; risco previsto de 99% ...
  NÃO existe rota totalmente acessível. ALERTA: o trajeto exige superar uma barreira intransponível.

[idoso]
  A rota curta é transitável para este perfil.
  Rota acessível: P -> A -> B -> J -> D -> K -> E -> G (2330 m)
```

**Cenário após o conserto do elevador**

```
[cadeira]
  Rota acessível: P -> A -> B -> J -> D -> K -> E -> G (2330 m)
[carrinho]
  Rota acessível: P -> A -> B -> J -> D -> K -> E -> G (2330 m)
```

Enquanto o elevador está em manutenção, **cadeirantes e pessoas com carrinho não têm rota**: o problema começa na
própria estação. Com o elevador funcionando, o sistema troca as ruas estreitas do comércio (Glicério e Baruel) pelo
caminho da Rua Marechal Deodoro, Rua Felício de Camargo e Av. Antônio Marques Figueira: 2.330 m, 250 m mais longo que os
2.080 m do trajeto curto, porém transitável. O idoso consegue passar pelas ruas estreitas, mas o índice baixo delas
torna a avenida o caminho de menor custo também para ele.

**Reformas** (benefício = pessoas por dia × gravidade prevista):

```
orçamento R$ 35 mil  -> reformar ['P-A']                                   | benefício 3000
orçamento R$ 200 mil -> reformar ['P-A', 'B-H', 'I-G']                     | benefício 7250
orçamento R$ 400 mil -> reformar ['P-A', 'B-H', 'C-I', 'I-G', 'C-K', 'E-G'] | benefício 10950
```

O conserto do elevador é sempre a primeira escolha: custa pouco e libera a saída da estação. Com mais orçamento, a
mochila escolhe os trechos de gravidade alta com mais pessoas por real investido, começando pelo início da Glicério
(B–H) e pela chegada à Santa Casa pelo lado da Baruel (I–G).

## Dados

A distância total da rota mais curta, da Estação Suzano até a Santa Casa de Suzano (Rua Professor Roberto David, 499),
foi **medida no mapa: cerca de 2.080 m** (o Moovit indica 2.092 m e cerca de 27 minutos de caminhada). A rota
pela avenida, que desvia das calçadas estreitas e quebradas, foi estimada em 2.330 m (250 m a mais).

Os nomes das ruas seguem o caminho pelo Centro e pela Vila Figueira. A divisão em trechos e as medidas de largura,
piso e inclinação são **estimadas** e devem ser conferidas no mapa e em campo. O `indice`, o `p_intransitavel` e a
`gravidade` de cada trecho são os valores previstos pelos modelos do notebook de Data Science (seção 8.1). O notebook
tem uma célula que confere se os valores do `dados.py` são iguais aos previstos. Nenhuma informação pessoal é usada.

## Limitações

- O grafo é pequeno e de demonstração; o Floyd-Warshall (O(V³)) não escala para uma cidade inteira, onde seria
  melhor rodar Dijkstra sob demanda com o cache.
- Os limites por perfil são valores de referência e podem ser ajustados em `LIMITES`.
- O estado do elevador é informado manualmente; na solução completa, viria de relatos dos usuários ou do operador.
- A mochila soma os benefícios de cada reforma separadamente. Ela não garante que os trechos escolhidos formem uma
  rota acessível completa: reformar só parte da Glicério ainda deixa a rota curta bloqueada.
- Os valores de índice e risco vêm de modelos treinados com uma base simulada; quando houver medições de campo, os
  modelos devem ser retreinados e os valores do `dados.py` atualizados.

## Ligação com as outras disciplinas

- **Data Science:** o índice de acessibilidade de cada trecho (custo no Dijkstra) vem da regressão linear; a
  probabilidade de o trecho ser intransitável (bloqueio para cadeira de rodas) e a gravidade (benefício na mochila)
  vêm da regressão logística. Os valores são os mesmos do notebook `CalcadaLivre_DataScience.ipynb`.

- **Java (DDD):** a API expõe trechos, barreiras, validações e rotas.
- **Agile:** as personas (Seu Antônio, Camila e Dona Neide) correspondem aos perfis `cadeira`, `carrinho` e `idoso`.
