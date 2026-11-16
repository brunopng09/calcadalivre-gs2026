"""Dados do trajeto: da Estação Suzano até a Santa Casa de Suzano (Rua Professor Roberto David, 499).

- Pontos e metros de cada trecho: ruas reais do OpenStreetMap (arquivo map.osm). A rota mais curta, pela
  Av. Antônio Marques Figueira, tem 2.332 m (o Google Maps indica cerca de 2,3 km).
- As calçadas NÃO têm a mesma largura: cada trecho é uma rua (ou parte dela) com a sua largura livre.
- indice, p_intransitavel e gravidade vêm dos modelos do notebook de Data Science
  (CalcadaLivre_DataScience.ipynb, seção 8.1): regressão linear e regressão logística.
- Largura, piso, inclinação e degrau de cada trecho são ESTIMADOS: substitua-os pelas medições de campo.
Nenhum dado pessoal é usado: o sistema registra apenas as características das calçadas.
"""

ESQUINAS = {
    "P": "Plataforma da Estação Suzano",
    "A": "Saída da estação (Rua Doutor Prudente de Moraes)",
    "B": "Acesso à Av. Antônio Marques Figueira",
    "C": "Av. Antônio Marques Figueira x Rua Monsenhor Nuno",
    "D": "Av. Antônio Marques Figueira x Rua Seian Hanashiro",
    "E": "Av. Antônio Marques Figueira x Rua Afonso Nicola Redondo",
    "F": "Av. Antônio Marques Figueira x Rua Professor Roberto David",
    "G": "Santa Casa de Suzano (Rua Professor Roberto David, 499)",
    "K": "Rua Kaneji Kodama x Rua Monsenhor Nuno",
    "L": "Rua Kaneji Kodama x Rua Seian Hanashiro",
    "M": "Rua Kaneji Kodama x Rua Afonso Nicola Redondo",
    "N": "Rua Kaneji Kodama x Rua Professor Roberto David",
    "H": "Rua General Francisco Glicério x Praça Expedicionário Antônio Garcia",
    "I": "Rua Baruel x Rua Horácio de Souza Coutinho",
    "W": "Rua Professor Roberto David x Avenida Água Branca",
}

# Coordenadas (latitude, longitude) de cada ponto, tiradas do OpenStreetMap
COORDENADAS = {
    "P": (-23.5341, -46.30795),
    "A": (-23.534604, -46.3080203),
    "B": (-23.5359815, -46.3051687),
    "C": (-23.5385575, -46.3062364),
    "D": (-23.5443148, -46.3066529),
    "E": (-23.5486784, -46.3070894),
    "F": (-23.5526035, -46.3074522),
    "G": (-23.5526545, -46.3067753),
    "K": (-23.5389395, -46.30512),
    "L": (-23.5443554, -46.3055411),
    "M": (-23.5487271, -46.3058413),
    "N": (-23.5527056, -46.3060983),
    "H": (-23.5417919, -46.3109793),
    "I": (-23.5487963, -46.3107072),
    "W": (-23.5525402, -46.3082654),
}

# Situação do elevador da estação. True = funcionando; False = em manutenção (só escada).
ELEVADOR_FUNCIONANDO = False

# Limiar de risco adotado no notebook de Data Science: acima dele, o trecho é evitado para cadeira de rodas.
LIMIAR_RISCO_CADEIRA = 0.3

# Trechos de calçada. Formato:
# (origem, destino, rua, metros, inclinacao_pct, tem_degrau, largura_livre_m, buracos 0-3, ocupado,
#  indice 0-100, p_intransitavel, gravidade 1-5)
#   metros: OpenStreetMap | indice / p_intransitavel / gravidade: modelos do notebook de Data Science
CALCADAS = [
    # saída da estação
    ("A", "B", "Rua Doutor Prudente de Moraes (saída da estação)", 359, 2.0, False, 1.50, 0, False, 92, 0.07, 1),
    # rota mais curta: Av. Antônio Marques Figueira (calçadas estreitas e quebradas no comércio)
    ("B", "C", "Avenida Antônio Marques Figueira (início)", 307, 2.0, False, 1.60, 0, False, 89, 0.06, 1),
    ("C", "D", "Avenida Antônio Marques Figueira (trecho comercial)", 642, 3.0, False, 0.75, 2, True, 31, 0.99, 5),
    ("D", "E", "Avenida Antônio Marques Figueira (trecho central)", 487, 3.0, False, 0.85, 3, False, 38, 0.99, 5),
    ("E", "F", "Avenida Antônio Marques Figueira (chegada à Santa Casa)", 438, 4.0, True, 0.80, 2, True, 26, 1.0, 5),
    ("F", "G", "Rua Professor Roberto David (frente da Santa Casa)", 69, 1.0, False, 1.60, 0, False, 95, 0.02, 1),
    # rota pela Rua Kaneji Kodama: calçadas mais largas, 88 m a mais
    ("C", "K", "Rua Monsenhor Nuno", 121, 3.0, False, 1.30, 1, False, 70, 0.24, 2),
    ("K", "L", "Rua Kaneji Kodama (trecho norte)", 604, 3.0, False, 1.50, 1, False, 82, 0.22, 2),
    ("L", "M", "Rua Kaneji Kodama (trecho central)", 487, 2.0, False, 1.40, 0, False, 85, 0.15, 2),
    ("M", "N", "Rua Kaneji Kodama (trecho sul)", 443, 2.0, False, 1.50, 0, False, 89, 0.09, 1),
    ("N", "G", "Rua Professor Roberto David (lado da Kaneji Kodama)", 69, 1.0, False, 1.50, 0, False, 93, 0.03, 1),
    # ligações entre a avenida e a Kaneji Kodama
    ("D", "L", "Rua Seian Hanashiro", 113, 2.0, False, 1.00, 1, True, 54, 0.66, 4),
    ("E", "M", "Rua Afonso Nicola Redondo", 127, 3.0, False, 1.10, 2, False, 54, 0.83, 4),
    # rota pelas ruas do Centro: Glicério e Baruel
    ("A", "H", "Rua General Francisco Glicério (comércio)", 854, 2.0, False, 0.95, 1, True, 49, 0.94, 5),
    ("H", "I", "Rua Baruel", 831, 4.0, False, 0.70, 3, False, 36, 1.0, 5),
    ("I", "W", "Rua Horácio de Souza Coutinho (Jardim São Luís)", 646, 3.0, False, 1.10, 1, False, 63, 0.78, 4),
    ("W", "F", "Rua Professor Roberto David (lado oeste)", 83, 3.0, True, 0.90, 2, False, 39, 0.98, 5),
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
    (("P", "A"), 35, 900),    # conserto/manutenção do elevador
    (("A", "B"), 70, 900),
    (("B", "C"), 60, 800),
    (("C", "D"), 130, 700),
    (("D", "E"), 95, 600),
    (("E", "F"), 90, 600),
    (("F", "G"), 15, 800),
    (("C", "K"), 25, 250),
    (("K", "L"), 120, 250),
    (("L", "M"), 95, 250),
    (("M", "N"), 90, 300),
    (("N", "G"), 15, 400),
    (("D", "L"), 25, 150),
    (("E", "M"), 25, 150),
    (("A", "H"), 170, 700),
    (("H", "I"), 165, 300),
    (("I", "W"), 130, 200),
    (("W", "F"), 15, 250),
]


def gravidade_do_trecho(origem, destino, elevador_funcionando=ELEVADOR_FUNCIONANDO):
    for t in montar_trechos(elevador_funcionando):
        if {t[0], t[1]} == {origem, destino}:
            return t[11]
    raise KeyError((origem, destino))
