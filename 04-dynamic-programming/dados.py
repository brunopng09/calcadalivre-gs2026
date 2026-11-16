"""Dados do trajeto: da Estação Suzano até a Santa Casa de Suzano (Rua Professor Roberto David, 499 - Vila Figueira).

- Distância total da rota mais curta (2.080 m): medida pelo grupo no mapa.
- As calçadas NÃO têm a mesma largura: cada trecho é uma rua (ou parte dela) com a sua largura livre.
- indice, p_intransitavel e gravidade vêm dos modelos do notebook de Data Science
  (CalcadaLivre_DataScience.ipynb, seção 8.1): regressão linear e regressão logística.
- A divisão em trechos e as medidas de largura, piso e inclinação são ESTIMADAS: substitua-as pelas medições de campo.
Nenhum dado pessoal é usado: o sistema registra apenas as características das calçadas.
"""

ESQUINAS = {
    "P": "Plataforma da Estação Suzano",
    "A": "Saída da estação (Rua Prudente de Moraes)",
    "B": "Esquina em frente à estação",
    "H": "Rua General Francisco Glicério (meio do comércio)",
    "C": "Fim do comércio da Glicério",
    "I": "Rua Baruel x Rua Professor Roberto David",
    "J": "Rua Marechal Deodoro x Rua Felício de Camargo",
    "D": "Rua Felício de Camargo x Av. Antônio Marques Figueira",
    "K": "Av. Antônio Marques Figueira (meio do trajeto)",
    "E": "Av. Antônio Marques Figueira x Rua Professor Roberto David",
    "G": "Santa Casa de Suzano (Rua Professor Roberto David, 499)",
}

# Situação do elevador da estação. True = funcionando; False = em manutenção (só escada).
ELEVADOR_FUNCIONANDO = False

# Limiar de risco adotado no notebook de Data Science: acima dele, o trecho é evitado para cadeira de rodas.
LIMIAR_RISCO_CADEIRA = 0.3

# Trechos de calçada. Formato:
# (origem, destino, rua, metros, inclinacao_pct, tem_degrau, largura_livre_m, buracos 0-3, ocupado,
#  indice 0-100, p_intransitavel, gravidade 1-5)
#   indice / p_intransitavel / gravidade: previstos pelos modelos do notebook de Data Science
CALCADAS = [
    # rota mais curta: ruas do comércio, calçadas estreitas (2.080 m com P-A e A-B)
    ("A", "B", "Rua Prudente de Moraes (saída da estação)",       50, 2.0, False, 1.50, 0, False, 93, 0.04, 1),
    ("B", "H", "Rua General Francisco Glicério (início)",           400, 2.0, False, 0.95, 1, True,  50, 0.80, 4),
    ("H", "C", "Rua General Francisco Glicério (comércio)",         750, 3.0, False, 0.75, 2, True,  35, 0.98, 5),
    ("C", "I", "Rua Baruel",                                     500, 4.0, False, 0.70, 3, False, 39, 0.98, 5),
    ("I", "G", "Rua Professor Roberto David (lado da Baruel)",       350, 5.0, True,  0.80, 2, False, 35, 0.99, 5),
    # ligações entre as duas rotas
    ("H", "J", "Travessa Glicério -> Marechal Deodoro",              250, 2.0, False, 1.00, 1, True,  51, 0.71, 4),
    ("C", "K", "Travessa Glicério -> avenida",                   400, 3.0, False, 0.85, 2, False, 47, 0.92, 5),
    # rota pela avenida: calçadas mais largas (2.330 m com P-A e A-B)
    ("B", "J", "Rua Marechal Deodoro",                           300, 3.0, False, 1.30, 1, False, 77, 0.21, 2),
    ("J", "D", "Rua Felício de Camargo",                         400, 4.0, False, 1.40, 1, False, 79, 0.27, 2),
    ("D", "K", "Av. Antônio Marques Figueira (início)",         1000, 3.0, False, 1.30, 1, False, 71, 0.20, 2),
    ("K", "E", "Av. Antônio Marques Figueira (até a Santa Casa)", 450, 2.0, False, 1.60, 0, False, 91, 0.02, 1),
    ("E", "G", "Rua Professor Roberto David (lado da avenida)",      100, 2.0, False, 1.40, 1, False, 80, 0.16, 2),
]


def montar_trechos(elevador_funcionando=ELEVADOR_FUNCIONANDO):
    """Devolve todos os trechos. O trecho P-A é a ligação plataforma <-> rua: elevador ou escada.
    Ele não é calçada, por isso não tem previsão do modelo (p_intransitavel = None)."""
    elevador = ("elevador em funcionamento" if elevador_funcionando
                else "elevador em manutenção: só há escada")
    ligacao_estacao = ("P", "A", f"Estação Suzano ({elevador})", 30, 0.0, not elevador_funcionando, 1.50, 0,
                       False, 90 if elevador_funcionando else 30, None, 1 if elevador_funcionando else 5)
    return [ligacao_estacao] + CALCADAS


TRECHOS = montar_trechos()

# Reformas candidatas: (trecho, custo_em_mil_reais, pessoas_por_dia)
# Custo das calçadas: cerca de R$ 200 por metro (faixa livre ampliada + piso recomposto).
# A gravidade (1-5) de cada trecho vem do modelo de Data Science (campo gravidade em CALCADAS).
REFORMAS = [
    (("P", "A"), 35, 600),    # conserto/manutenção do elevador
    (("B", "H"), 80, 500),
    (("H", "C"), 150, 500),
    (("C", "I"), 100, 450),
    (("I", "G"), 70, 450),
    (("H", "J"), 50, 150),
    (("C", "K"), 80, 150),
    (("B", "J"), 60, 300),
    (("J", "D"), 80, 300),
    (("D", "K"), 200, 300),
    (("K", "E"), 90, 350),
    (("E", "G"), 20, 350),
]


def gravidade_do_trecho(origem, destino, elevador_funcionando=ELEVADOR_FUNCIONANDO):
    for t in montar_trechos(elevador_funcionando):
        if {t[0], t[1]} == {origem, destino}:
            return t[11]
    raise KeyError((origem, destino))
