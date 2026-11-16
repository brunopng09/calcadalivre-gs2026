import time
import dados
import rotas
from reformas import escolher_reformas

ORIGEM, DESTINO = "P", "G"   # Plataforma da Estação Suzano -> Santa Casa de Suzano


def nome(no):
    return f"{no} ({dados.ESQUINAS[no]})"


def metros_do_caminho(grafo, caminho):
    return sum(t["metros"] for a, b in zip(caminho, caminho[1:]) for v, t in grafo[a] if v == b)


def demo_rotas(grafo, titulo):
    print(f"\n=== 1. ROTAS (Dijkstra por perfil) - {titulo} ===")
    m_curta, c_curta = rotas.rota_mais_curta(grafo, ORIGEM, DESTINO)
    print(f"De {nome(ORIGEM)}\naté {nome(DESTINO)}")
    print(f"\nMais curta: {' -> '.join(c_curta)} ({m_curta:.0f} m)")
    for perfil in ("cadeira", "carrinho", "idoso"):
        print(f"\n[{perfil}]")
        riscos = rotas.trechos_de_risco(grafo, c_curta, perfil)
        if riscos:
            print("  A rota curta OBRIGARIA a ir pela rua ou a desistir do trajeto:")
            for a, b, motivos in riscos:
                rua = next(t["rua"] for v, t in grafo[a] if v == b)
                print(f"   - trecho {a}->{b} [{rua}]: {'; '.join(motivos)}")
        else:
            print("  A rota curta é transitável para este perfil.")
        r = rotas.rota_com_alerta(grafo, ORIGEM, DESTINO, perfil)
        if r["acessivel"]:
            print(f"  Rota acessível: {' -> '.join(r['caminho'])} ({metros_do_caminho(grafo, r['caminho']):.0f} m)")
        else:
            print("  NÃO existe rota totalmente acessível. ALERTA: o trajeto exige superar uma barreira intransponível.")


def demo_floyd(grafo):
    print("\n=== 2. FLOYD-WARSHALL (tabulação): custo entre todos os pares, perfil cadeira (elevador em manutenção) ===")
    nos, dp = rotas.floyd_warshall(grafo, "cadeira")
    print("     " + "".join(f"{n:>7}" for n in nos))
    for i, n in enumerate(nos):
        print(f"{n:>4} " + "".join(f"{(dp[i][j] if dp[i][j] != rotas.INF else float('nan')):7.0f}" for j in range(len(nos))))
    print("(nan = sem rota acessível; a plataforma P fica isolada enquanto o elevador está em manutenção)")


def demo_memo():
    print("\n=== 3. MEMOIZAÇÃO e invalidação do cache ===")
    g_manut = rotas.construir_grafo(dados.montar_trechos(False))
    g_ok = rotas.construir_grafo(dados.montar_trechos(True))

    rotas.definir_grafo(g_manut)
    pares = [(p, ORIGEM, DESTINO) for p in ("cadeira", "carrinho", "idoso")] * 2000
    t0 = time.perf_counter()
    for p in pares:
        rotas.rota_acessivel(g_manut, p[1], p[2], p[0])
    sem = time.perf_counter() - t0
    t0 = time.perf_counter()
    for p in pares:
        rotas.rota_memo(*p)
    com = time.perf_counter() - t0
    print(f"{len(pares)} consultas: sem cache {sem*1000:.1f} ms | com cache {com*1000:.1f} ms")
    print(f"cache: {rotas.rota_memo.cache_info()}")

    antes = rotas.rota_memo("cadeira", ORIGEM, DESTINO)[1]
    print(f"\nElevador em manutenção, cadeira: {antes if antes else 'sem rota acessível'}")
    rotas.definir_grafo(g_ok)    # o elevador foi consertado: o cache precisa ser limpo
    depois = rotas.rota_memo("cadeira", ORIGEM, DESTINO)[1]
    print(f"Elevador consertado (cache limpo), cadeira: {' -> '.join(depois)}")


def demo_reformas():
    print("\n=== 4. MOCHILA (tabulação): o que consertar primeiro ===")
    print("(benefício = pessoas por dia x gravidade prevista pelo modelo de Data Science)")
    custos = [r[1] for r in dados.REFORMAS]
    beneficios = [r[2] * dados.gravidade_do_trecho(*r[0]) for r in dados.REFORMAS]
    for orcamento in (35, 200, 400):
        total, idx = escolher_reformas(custos, beneficios, orcamento)
        nomes = [f"{dados.REFORMAS[i][0][0]}-{dados.REFORMAS[i][0][1]}" for i in idx]
        gasto = sum(custos[i] for i in idx)
        print(f"orçamento R$ {orcamento} mil -> reformar {nomes} | gasto R$ {gasto} mil | benefício {total}")


if __name__ == "__main__":
    g_manut = rotas.construir_grafo(dados.montar_trechos(False))
    g_ok = rotas.construir_grafo(dados.montar_trechos(True))
    demo_rotas(g_manut, "ELEVADOR EM MANUTENÇÃO (situação atual)")
    demo_rotas(g_ok, "ELEVADOR FUNCIONANDO (cenário após o conserto)")
    demo_floyd(g_manut)
    demo_memo()
    demo_reformas()
