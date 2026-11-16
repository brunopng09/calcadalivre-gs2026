"""Mochila 0/1 (tabulação): quais trechos reformar dentro do orçamento."""


def escolher_reformas(custos, beneficios, orcamento):
    """dp[i][b] = maior benefício usando os i primeiros itens com orçamento b."""
    n = len(custos)
    dp = [[0] * (orcamento + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        for b in range(orcamento + 1):
            dp[i][b] = dp[i - 1][b]                      # não reformar o item i
            if custos[i - 1] <= b:                       # reformar o item i
                dp[i][b] = max(dp[i][b], dp[i - 1][b - custos[i - 1]] + beneficios[i - 1])
    escolhidos, b = [], orcamento
    for i in range(n, 0, -1):
        if dp[i][b] != dp[i - 1][b]:
            escolhidos.append(i - 1)
            b -= custos[i - 1]
    return dp[n][orcamento], escolhidos[::-1]
