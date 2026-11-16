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
2. **As calçadas.** Depois da estação, o caminho mais curto segue pela Av. Antônio Marques Figueira, com calçadas
   estreitas, ocupadas pelo comércio e quebradas.
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
| `map.osm` | Mapa da região exportado do OpenStreetMap, de onde vieram os pontos, as coordenadas e os metros de cada trecho (não é lido pelo programa) |

## Modelagem dos dados

### Pontos (nós)

Os pontos são cruzamentos reais, tirados do OpenStreetMap. As coordenadas de cada um estão em `dados.COORDENADAS`.

| Nó | Local |
|---|---|
| P | Plataforma da Estação Suzano |
| A | Saída da estação (Rua Doutor Prudente de Moraes) |
| B | Acesso à Av. Antônio Marques Figueira |
| C | Av. Antônio Marques Figueira x Rua Monsenhor Nuno |
| D | Av. Antônio Marques Figueira x Rua Seian Hanashiro |
| E | Av. Antônio Marques Figueira x Rua Afonso Nicola Redondo |
| F | Av. Antônio Marques Figueira x Rua Professor Roberto David |
| G | Santa Casa de Suzano (Rua Professor Roberto David, 499) |
| K | Rua Kaneji Kodama x Rua Monsenhor Nuno |
| L | Rua Kaneji Kodama x Rua Seian Hanashiro |
| M | Rua Kaneji Kodama x Rua Afonso Nicola Redondo |
| N | Rua Kaneji Kodama x Rua Professor Roberto David |
| H | Rua General Francisco Glicério x Praça Expedicionário Antônio Garcia |
| I | Rua Baruel x Rua Horácio de Souza Coutinho |
| W | Rua Professor Roberto David x Avenida Água Branca |

### Trechos (arestas)

Cada trecho tem: `origem`, `destino`, `rua`, `metros`, `inclinacao_pct`, `tem_degrau`, `largura_livre_m`, `buracos`
(0 = piso bom, 3 = muito quebrado), `ocupado` e três valores previstos pelos modelos do notebook de Data Science:
`indice` de acessibilidade (0 a 100, regressão linear), `p_intransitavel` (probabilidade de ser intransitável para
cadeira de rodas, regressão logística) e `gravidade` (1 a 5, calculada a partir da probabilidade). Os metros de cada
trecho foram medidos sobre a geometria das ruas no OpenStreetMap.

| Trecho | Rua | Metros | Largura livre | Piso | Índice | Risco | Gravidade |
|---|---|---|---|---|---|---|---|
| P–A | Estação Suzano (elevador ou escada) | 30 | 1,50 m | bom | 30 (90 com elevador) | — | 5 (1 com elevador) |
| A–B | Rua Doutor Prudente de Moraes (saída da estação) | 359 | 1,50 m | bom | 92 | 7% | 1 |
| B–C | Avenida Antônio Marques Figueira (início) | 307 | 1,60 m | bom | 89 | 6% | 1 |
| C–D | Avenida Antônio Marques Figueira (trecho comercial) | 642 | 0,75 m | irregular | 31 | 99% | 5 |
| D–E | Avenida Antônio Marques Figueira (trecho central) | 487 | 0,85 m | muito quebrado | 38 | 99% | 5 |
| E–F | Avenida Antônio Marques Figueira (chegada à Santa Casa) | 438 | 0,80 m | irregular, com degrau | 26 | 100% | 5 |
| F–G | Rua Professor Roberto David (frente da Santa Casa) | 69 | 1,60 m | bom | 95 | 2% | 1 |
| C–K | Rua Monsenhor Nuno | 121 | 1,30 m | pouco quebrado | 70 | 24% | 2 |
| K–L | Rua Kaneji Kodama (trecho norte) | 604 | 1,50 m | pouco quebrado | 82 | 22% | 2 |
| L–M | Rua Kaneji Kodama (trecho central) | 487 | 1,40 m | bom | 85 | 15% | 2 |
| M–N | Rua Kaneji Kodama (trecho sul) | 443 | 1,50 m | bom | 89 | 9% | 1 |
| N–G | Rua Professor Roberto David (lado da Kaneji Kodama) | 69 | 1,50 m | bom | 93 | 3% | 1 |
| D–L | Rua Seian Hanashiro | 113 | 1,00 m | pouco quebrado | 54 | 66% | 4 |
| E–M | Rua Afonso Nicola Redondo | 127 | 1,10 m | irregular | 54 | 83% | 4 |
| A–H | Rua General Francisco Glicério (comércio) | 854 | 0,95 m | pouco quebrado | 49 | 94% | 5 |
| H–I | Rua Baruel | 831 | 0,70 m | muito quebrado | 36 | 100% | 5 |
| I–W | Rua Horácio de Souza Coutinho (Jardim São Luís) | 646 | 1,10 m | pouco quebrado | 63 | 78% | 4 |
| W–F | Rua Professor Roberto David (lado oeste) | 83 | 0,90 m | irregular, com degrau | 39 | 98% | 5 |

São três caminhos possíveis da plataforma até a Santa Casa:

| Caminho | Pontos | Distância | Calçadas |
|---|---|---|---|
| Pela Av. Antônio Marques Figueira (mais curto) | P–A–B–C–D–E–F–G | **2.332 m** | de 0,75 m a 0,85 m nos três trechos do meio |
| Pela Rua Kaneji Kodama (paralela à avenida) | P–A–B–C–K–L–M–N–G | **2.420 m** | de 1,30 m a 1,60 m |
| Pela Glicério e pela Baruel (ruas do Centro) | P–A–H–I–W–F–G | **2.513 m** | de 0,70 m a 1,10 m |

As ruas Seian Hanashiro (D–L) e Afonso Nicola Redondo (E–M) ligam a avenida à Kaneji Kodama no meio do caminho.

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
Mais curta: P -> A -> B -> C -> D -> E -> F -> G (2332 m)

[cadeira]
  A rota curta OBRIGARIA a ir pela rua ou a desistir do trajeto:
   - trecho P->A [Estação Suzano (elevador em manutenção: só há escada)]: degrau/escada
   - trecho C->D [Avenida Antônio Marques Figueira (trecho comercial)]: calçada estreita: 0.75 m livres, mínimo 0.90 m (calçada ocupada); risco previsto de 99% ...
   - trecho D->E [Avenida Antônio Marques Figueira (trecho central)]: calçada estreita: 0.85 m livres, mínimo 0.90 m; piso muito quebrado; risco previsto de 99% ...
   - trecho E->F [Avenida Antônio Marques Figueira (chegada à Santa Casa)]: calçada estreita: 0.80 m livres, mínimo 0.90 m (calçada ocupada); degrau/escada; risco previsto de 100% ...
  NÃO existe rota totalmente acessível. ALERTA: o trajeto exige superar uma barreira intransponível.

[idoso]
  A rota curta é transitável para este perfil.
  Rota acessível: P -> A -> B -> C -> K -> L -> M -> N -> G (2420 m)
```

**Cenário após o conserto do elevador**

```
[cadeira]
  Rota acessível: P -> A -> B -> C -> K -> L -> M -> N -> G (2420 m)
[carrinho]
  Rota acessível: P -> A -> B -> C -> K -> L -> M -> N -> G (2420 m)
```

Enquanto o elevador está em manutenção, **cadeirantes e pessoas com carrinho não têm rota**: o problema começa na
própria estação. Com o elevador funcionando, o sistema tira a pessoa da avenida na Rua Monsenhor Nuno e segue pela
Rua Kaneji Kodama, paralela: 2.420 m, só **88 m a mais** que os 2.332 m do caminho pela avenida, porém transitável. O
idoso consegue passar pela avenida, mas o índice baixo dela torna a Kaneji Kodama o caminho de menor custo também
para ele.

**Reformas** (benefício = pessoas por dia × gravidade prevista):

```
orçamento R$ 35 mil  -> reformar ['P-A']                                          | benefício 4500
orçamento R$ 200 mil -> reformar ['P-A', 'D-E', 'F-G', 'N-G', 'D-L', 'W-F']       | benefício 10550
orçamento R$ 400 mil -> reformar ['P-A', 'C-D', 'D-E', 'E-F', 'F-G', 'N-G', 'W-F'] | benefício 16450
```

O conserto do elevador é sempre a primeira escolha: custa pouco e libera a saída da estação. Com R$ 400 mil, a
mochila inclui os três trechos estreitos da avenida (C–D, D–E e E–F), e o caminho mais curto passa a ser também o
acessível.

## Dados

Os pontos, as coordenadas e os metros de cada trecho vêm do **OpenStreetMap** (arquivo `map.osm` da região entre a
Estação Suzano e a Santa Casa). A rota mais curta, pela Av. Antônio Marques Figueira, tem **2.332 m**; o Google Maps
indica cerca de 2,3 km e 32 minutos a pé para o mesmo trajeto.

A largura, o piso, a inclinação e o degrau de cada trecho são **estimados** e devem ser conferidos em campo. O
`indice`, o `p_intransitavel` e a `gravidade` de cada trecho são os valores previstos pelos modelos do notebook de
Data Science (seção 8.1). O notebook tem uma célula que confere se os valores do `dados.py` são iguais aos previstos.
Nenhuma informação pessoal é usada.

## Limitações

- O grafo é pequeno e de demonstração; o Floyd-Warshall (O(V³)) não escala para uma cidade inteira, onde seria
  melhor rodar Dijkstra sob demanda com o cache.
- Os limites por perfil são valores de referência e podem ser ajustados em `LIMITES`.
- O estado do elevador é informado manualmente; na solução completa, viria de relatos dos usuários ou do operador.
- A mochila soma os benefícios de cada reforma separadamente. Ela não garante que os trechos escolhidos formem uma
  rota acessível completa: com R$ 200 mil, ela reforma só um dos três trechos ruins da avenida (D–E), e a
  avenida continua bloqueada para cadeira de rodas.
- Os valores de índice e risco vêm de modelos treinados com uma base simulada; quando houver medições de campo, os
  modelos devem ser retreinados e os valores do `dados.py` atualizados.

