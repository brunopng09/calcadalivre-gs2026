"""Gera a base SIMULADA de trechos de calçada do entorno Estação Suzano -> Santa Casa de Suzano.

Fonte: base sintética criada pelo grupo CalçadaLivre. As faixas de valores seguem a NBR 9050:2020
(faixa livre mínima de 1,20 m na calçada) e o que foi observado no trajeto (calçadas estreitas,
ocupadas pelo comércio e com piso quebrado). Não contém dados pessoais.

Uso: python gerar_base.py  ->  gera calcadas_suzano.csv
"""
import numpy as np
import pandas as pd

rng = np.random.default_rng(2026)
N = 640

# Ruas do entorno: cada rua tem uma largura de calçada típica diferente.
# (rua, bairro, tipo_via, largura total média em m, peso na amostra)
RUAS = [
    ("Rua Prudente de Moraes",             "Centro",              "coletora", 2.00, 0.08),
    ("Rua Marechal Deodoro",               "Centro",              "coletora", 1.70, 0.07),
    ("Rua Felício de Camargo",             "Centro",              "coletora", 2.00, 0.07),
    ("Rua General Francisco Glicério",     "Centro",              "local",    1.30, 0.09),
    ("Rua Benjamin Constant",              "Centro",              "local",    1.50, 0.06),
    ("Av. Antônio Marques Figueira",       "Vila Figueira",       "arterial", 2.40, 0.10),
    ("Rua Baruel",                         "Vila Figueira",       "local",    1.20, 0.06),
    ("Rua Professor Roberto David",        "Vila Figueira",       "local",    1.50, 0.05),
    ("Av. Armando Salles de Oliveira",     "Parque Suzano",       "arterial", 2.60, 0.08),
    ("Rua Sete de Setembro",               "Parque Suzano",       "coletora", 1.90, 0.06),
    ("Av. Paulo Portela",                  "Jardim Paulista",     "arterial", 2.50, 0.06),
    ("Rua Rui Barbosa",                    "Vila Costa",          "local",    1.40, 0.06),
    ("Rua Tiradentes",                     "Vila Costa",          "local",    1.50, 0.05),
    ("Rua Nove de Julho",                  "Jardim Santa Helena", "local",    1.50, 0.06),
    ("Rua Missionária Sara Cooper",        "Jardim Santa Helena", "local",    1.40, 0.05),
]
pesos = np.array([r[4] for r in RUAS]); pesos = pesos / pesos.sum()
idx = rng.choice(len(RUAS), size=N, p=pesos)
rua = np.array([RUAS[i][0] for i in idx])
bairro = np.array([RUAS[i][1] for i in idx])
tipo_via = np.array([RUAS[i][2] for i in idx])
media_larg = np.array([RUAS[i][3] for i in idx])
largura_total = np.clip(rng.normal(media_larg, 0.30), 0.9, 4.0)    # varia também dentro da mesma rua

p_ocup = np.where(bairro == "Centro", 0.45, 0.15) + np.where(tipo_via == "arterial", 0.10, 0.0)
ocupado_comercio = rng.binomial(1, p_ocup)
obstaculos_100m = rng.poisson(1.5 + 2.0 * ocupado_comercio)

largura_livre = (largura_total - 0.12 * obstaculos_100m - 0.40 * ocupado_comercio
                 - np.abs(rng.normal(0, 0.10, N)))
largura_livre = np.clip(largura_livre, 0.40, largura_total)

inclinacao = np.clip(rng.gamma(2.0, 2.2, N) + np.where(bairro == "Vila Figueira", 1.5, 0.0), 0, 18)
idade_calcada = rng.integers(2, 36, N)
z_bur = -1.2 + 0.08 * idade_calcada + rng.normal(0, 0.8, N)
buracos = np.digitize(z_bur, [0.0, 1.0, 2.0])            # 0 = piso bom ... 3 = muito quebrado
tem_degrau = rng.binomial(1, 0.10 + 0.12 * (inclinacao > 8))
rampa_esquina = rng.binomial(1, np.where(tipo_via == "arterial", 0.55, 0.35))
piso_tatil = rng.binomial(1, np.where((tipo_via == "arterial") | (bairro == "Centro"), 0.30, 0.10))
fluxo = np.round(rng.lognormal(np.where(bairro == "Centro", 5.3, 4.4), 0.5)).astype(int)
comprimento = rng.integers(40, 251, N)

# Alvo 1 (numérico): índice de acessibilidade dado na avaliação de campo (0 a 100)
indice = (95 - 40 * np.maximum(0, 1.5 - largura_livre) - 7 * buracos - 1.6 * inclinacao
          - 12 * tem_degrau - 1.5 * obstaculos_100m - 4 * ocupado_comercio
          + 5 * rampa_esquina + 3 * piso_tatil + rng.normal(0, 5, N))
indice = np.clip(np.round(indice, 1), 0, 100)

# Alvo 2 (binário): o avaliador em cadeira de rodas NÃO conseguiu passar pelo trecho (precisou ir pela rua)
z = (3.2 - 4.5 * largura_livre + 0.9 * buracos + 0.22 * inclinacao + 2.0 * tem_degrau
     + 0.25 * obstaculos_100m - 0.8 * rampa_esquina + rng.normal(0, 0.6, N))
intransitavel = rng.binomial(1, 1 / (1 + np.exp(-z)))

df = pd.DataFrame({
    "id_trecho": [f"T{i:04d}" for i in range(1, N + 1)],
    "rua": rua, "bairro": bairro, "tipo_via": tipo_via, "comprimento_m": comprimento,
    "largura_total_m": largura_total.round(2), "largura_livre_m": largura_livre.round(2),
    "inclinacao_pct": inclinacao.round(1), "buracos": buracos, "tem_degrau": tem_degrau,
    "obstaculos_100m": obstaculos_100m, "ocupado_comercio": ocupado_comercio,
    "rampa_esquina": rampa_esquina, "piso_tatil": piso_tatil, "idade_calcada_anos": idade_calcada,
    "fluxo_pedestres_hora": fluxo, "indice_acessibilidade": indice, "intransitavel_cadeira": intransitavel,
})

# Problemas comuns de uma coleta real, para serem tratados no notebook
for col in ["largura_livre_m", "inclinacao_pct"]:
    df.loc[rng.choice(N, 13, replace=False), col] = np.nan
df.loc[rng.choice(N, 3, replace=False), "largura_livre_m"] = [12.0, 15.0, 11.0]   # erro de digitação (vírgula)
df.loc[rng.choice(N, 6, replace=False), "bairro"] = "centro "                       # texto fora do padrão
df = pd.concat([df, df.sample(10, random_state=1)]).reset_index(drop=True)        # linhas duplicadas

df.to_csv("calcadas_suzano.csv", index=False)
print(df.shape, "| intransitável:", round(df["intransitavel_cadeira"].mean(), 3),
      "| índice médio:", round(df["indice_acessibilidade"].mean(), 1))
