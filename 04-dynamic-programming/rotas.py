"""Grafo de calçadas: Dijkstra por perfil, Floyd-Warshall (tabulação) e memoização."""
import heapq
from functools import lru_cache
from dados import LIMIAR_RISCO_CADEIRA

INF = float("inf")

# Limites por perfil. Abaixo da largura mínima, acima da inclinação ou com degrau/piso muito quebrado,
# o trecho é intransitável e a pessoa seria obrigada a descer para a rua.
# "risco": probabilidade de intransitável prevista pela regressão logística (Data Science) a partir da qual
# o trecho é evitado. O modelo foi treinado com avaliações em cadeira de rodas, por isso só vale para esse perfil.
LIMITES = {
    "cadeira":  {"inclinacao": 8.0,  "largura": 0.90, "degrau": True,  "buracos": 3, "risco": LIMIAR_RISCO_CADEIRA},
    "carrinho": {"inclinacao": 10.0, "largura": 0.80, "degrau": True,  "buracos": 3, "risco": None},
    "idoso":    {"inclinacao": 12.0, "largura": 0.70, "degrau": False, "buracos": 4, "risco": None},
}


def construir_grafo(trechos):
    """Grafo não-direcionado: {no: [(vizinho, trecho_dict), ...]}."""
    grafo = {}
    for o, d, rua, metros, incl, degrau, largura, buracos, ocupado, indice, p_intrans, gravidade in trechos:
        t = {"rua": rua, "metros": metros, "inclinacao_pct": incl, "tem_degrau": degrau,
             "largura_livre_m": largura, "buracos": buracos, "ocupado": ocupado,
             "indice": indice, "p_intransitavel": p_intrans, "gravidade": gravidade}
        grafo.setdefault(o, []).append((d, t))
        grafo.setdefault(d, []).append((o, t))
    return grafo


def motivos_bloqueio(trecho, perfil):
    """Lista de motivos pelos quais o trecho é intransitável para o perfil (vazia = passa)."""
    lim, motivos = LIMITES[perfil], []
    if trecho["largura_livre_m"] < lim["largura"]:
        causa = " (calçada ocupada)" if trecho["ocupado"] else ""
        motivos.append(f"calçada estreita: {trecho['largura_livre_m']:.2f} m livres, mínimo {lim['largura']:.2f} m{causa}")
    if trecho["buracos"] >= lim["buracos"]:
        motivos.append("piso muito quebrado")
    if trecho["tem_degrau"] and lim["degrau"]:
        motivos.append("degrau/escada")
    if trecho["inclinacao_pct"] > lim["inclinacao"]:
        motivos.append(f"inclinação de {trecho['inclinacao_pct']:.1f}% (máx. {lim['inclinacao']:.0f}%)")
    p = trecho["p_intransitavel"]
    if lim["risco"] is not None and p is not None and p >= lim["risco"]:
        motivos.append(f"risco previsto de {p:.0%} de ser intransitável (modelo de Data Science)")
    return motivos


def custo(trecho, perfil):
    """Custo do trecho para o perfil; None = intransitável."""
    if motivos_bloqueio(trecho, perfil):
        return None
    penalidade = 1 + (100 - trecho["indice"]) / 50   # índice 100 -> 1x ; índice 0 -> 3x
    return trecho["metros"] * penalidade


def rota_mais_curta(grafo, origem, destino):
    """Ignora acessibilidade: só a distância (para comparar)."""
    dist, ant, fila = {origem: 0}, {}, [(0, origem)]
    while fila:
        d, u = heapq.heappop(fila)
        if u == destino:
            break
        if d > dist.get(u, INF):
            continue
        for v, t in grafo[u]:
            nd = d + t["metros"]
            if nd < dist.get(v, INF):
                dist[v], ant[v] = nd, u
                heapq.heappush(fila, (nd, v))
    return _reconstruir(dist, ant, origem, destino)


def rota_acessivel(grafo, origem, destino, perfil):
    """Dijkstra com custo que depende do perfil."""
    dist, ant, fila = {origem: 0}, {}, [(0, origem)]
    while fila:
        d, u = heapq.heappop(fila)
        if u == destino:
            break
        if d > dist.get(u, INF):
            continue
        for v, t in grafo[u]:
            c = custo(t, perfil)
            if c is None:
                continue
            nd = d + c
            if nd < dist.get(v, INF):
                dist[v], ant[v] = nd, u
                heapq.heappush(fila, (nd, v))
    return _reconstruir(dist, ant, origem, destino)


def _reconstruir(dist, ant, origem, destino):
    if destino not in dist:
        return None, []
    caminho, no = [destino], destino
    while no != origem:
        no = ant[no]
        caminho.append(no)
    return dist[destino], caminho[::-1]


def trechos_de_risco(grafo, caminho, perfil):
    """Trechos do caminho em que a pessoa seria obrigada a ir pela rua, com os motivos."""
    riscos = []
    for a, b in zip(caminho, caminho[1:]):
        for v, t in grafo[a]:
            if v == b:
                motivos = motivos_bloqueio(t, perfil)
                if motivos:
                    riscos.append((a, b, motivos))
    return riscos


def rota_com_alerta(grafo, origem, destino, perfil):
    """Devolve a rota acessível; se não existir, devolve a mais curta com ALERTA de risco de ir pela rua."""
    custo_a, caminho = rota_acessivel(grafo, origem, destino, perfil)
    if caminho:
        return {"acessivel": True, "custo": custo_a, "caminho": caminho, "alertas": []}
    metros, caminho = rota_mais_curta(grafo, origem, destino)
    return {"acessivel": False, "custo": metros, "caminho": caminho,
            "alertas": trechos_de_risco(grafo, caminho, perfil)}


def floyd_warshall(grafo, perfil):
    """Programação dinâmica (tabulação): menor custo entre TODOS os pares, em O(n^3).
    dp[k][i][j] = menor custo de i a j usando apenas {nós 0..k-1} como intermediários."""
    nos = sorted(grafo)
    n = len(nos)
    idx = {no: i for i, no in enumerate(nos)}
    dp = [[INF] * n for _ in range(n)]
    for i in range(n):
        dp[i][i] = 0
    for u in nos:
        for v, t in grafo[u]:
            c = custo(t, perfil)
            if c is not None:
                dp[idx[u]][idx[v]] = min(dp[idx[u]][idx[v]], c)
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dp[i][k] + dp[k][j] < dp[i][j]:
                    dp[i][j] = dp[i][k] + dp[k][j]
    return nos, dp


# --- Memoização: guarda rotas já calculadas por (perfil, origem, destino) ---
_GRAFO_ATUAL = {}


def definir_grafo(grafo):
    """Troca o grafo e invalida o cache (necessário quando uma barreira muda)."""
    global _GRAFO_ATUAL
    _GRAFO_ATUAL = grafo
    rota_memo.cache_clear()


@lru_cache(maxsize=None)
def rota_memo(perfil, origem, destino):
    return rota_acessivel(_GRAFO_ATUAL, origem, destino, perfil)
