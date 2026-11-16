-- =====================================================================
-- CALCADA LIVRE — CitizenTech Global Solution 2026.2 (FIAP)
-- Disciplina: Database Design
-- Arquivo 03: DQL — as 3 consultas exigidas pelo enunciado
--
-- Integrantes:
--   RM562159 — Bruno Costa Scuciato
--   RM566222 — Luiz Fernando Balbino
--   RM561613 — Gustavo Torres Caldeira
--
-- Data: 12/11/2026
-- Item do enunciado: Script DQL (30 pontos) — tres consultas SELECT
--   (INNER JOIN; INNER JOIN + GROUP BY; LEFT OUTER JOIN + WHERE +
--   ORDER BY), cada uma com bloco de comentario indicando o item e a
--   pontuacao.
--
-- Banco: Oracle Database 19c (FIAP) — schema RM562159
-- =====================================================================

-- =====================================================================
-- CONSULTA 1 (10 pts) — INNER JOIN entre 2 tabelas
-- Objetivo: listar os registros de barreira do cidadao com a descricao
--           do tipo de barreira, ordenados por data e gravidade.
-- =====================================================================
SELECT r.id_registro          AS "ID_REGISTRO",
       r.dt_registro          AS "DATA_REGISTRO",
       t.ds_tipo_barreira     AS "TIPO_BARREIRA",
       r.nr_gravidade         AS "GRAVIDADE",
       r.st_status            AS "STATUS",
       r.ds_registro          AS "DESCRICAO"
FROM   CL_REGISTRO_BARREIRA r
       INNER JOIN CL_TIPO_BARREIRA t
               ON r.id_tipo_barreira = t.id_tipo_barreira
ORDER  BY r.dt_registro DESC,
          r.nr_gravidade DESC;

-- =====================================================================
-- CONSULTA 2 (10 pts) — INNER JOIN + funcoes de grupo / GROUP BY
-- Objetivo: por tipo de via, contar os trechos e mostrar o indice medio,
--           minimo e maximo de acessibilidade (vias prioritarias para
--           reforma), usando COUNT, AVG, MIN, MAX, ROUND, GROUP BY e
--           HAVING.
-- =====================================================================
SELECT v.ds_tipo_via                                AS "TIPO_DE_VIA",
       COUNT(t.id_trecho)                           AS "QTD_TRECHOS",
       ROUND(AVG(t.nr_indice_acessibilidade), 1)    AS "INDICE_MEDIO",
       ROUND(MIN(t.nr_indice_acessibilidade), 1)    AS "INDICE_MINIMO",
       ROUND(MAX(t.nr_indice_acessibilidade), 1)    AS "INDICE_MAXIMO"
FROM   CL_TRECHO t
       INNER JOIN CL_TIPO_VIA v
               ON t.id_tipo_via = v.id_tipo_via
GROUP  BY v.ds_tipo_via
HAVING COUNT(t.id_trecho) > 1
ORDER  BY "INDICE_MEDIO" ASC;

-- =====================================================================
-- CONSULTA 3 (10 pts) — LEFT OUTER JOIN + WHERE + ORDER BY
-- Objetivo: mostrar os trechos de calcada que AINDA NAO possuem nenhum
--           registro de barreira (lado direito nulo), filtrados por
--           WHERE e ordenados por indice decrescente e nome da rua.
-- =====================================================================
SELECT t.id_trecho                AS "ID_TRECHO",
       t.nm_rua                   AS "RUA",
       t.nr_indice_acessibilidade AS "INDICE_ACESSIBILIDADE",
       r.id_registro              AS "ID_REGISTRO"
FROM   CL_TRECHO t
       LEFT OUTER JOIN CL_REGISTRO_BARREIRA r
                    ON t.id_trecho = r.id_trecho
WHERE  r.id_registro IS NULL
ORDER  BY t.nr_indice_acessibilidade DESC,
          t.nm_rua ASC;
