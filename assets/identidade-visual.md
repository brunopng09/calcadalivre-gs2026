# Prompt de Identidade Visual & Design System — Projeto "CalçadaLivre"

## 1. Conceito Criativo: "Caminho Claro (Wayfinding Inclusivo)"
A identidade visual do **CalçadaLivre** é inspirada na sinalização urbana de orientação (*wayfinding*), na textura geométrica do piso tátil direcional e nos princípios de Desenho Universal (WCAG 2.1 nível AAA). O design transmite **segurança, autonomia, clareza imediata sob a luz do sol e acolhimento comunitário**.

## 2. Paleta de Cores (Alto Contraste — WCAG AAA)
- **Azul Calçada Profundo (Primary / Brand Base):** `#0B2545` — Usado em cabeçalhos, textos principais e fundos de alto contraste. Transmite confiança institucional e estabilidade.
- **Verde Rota Livre (Success / Caminho Acessível):** `#00A86B` (com variante vibrante `#10B981` em modo escuro) — Representa o trajeto validado, rampas acessíveis e trechos com índice entre 80 e 100.
- **Amarelo Piso Tátil (Warning / Atenção e Destaque):** `#FFB703` — Inspirado diretamente nos pisos táteis urbanos de alerta e direção. Usado em botões de ação primária (CTAs com texto escuro `#0B2545`), alertas moderados (índice 50–79) e foco de navegação por teclado/voz.
- **Vermelho Barreira (Danger / Intransitável):** `#D90429` — Sinaliza obstáculos severos (degraus, buracos, ausência de rampa, índice < 50) e rotas curtas porém bloqueadas.
- **Fundo Urbano Claro (Surface Light):** `#F8FAFC` — Fundo principal limpo que evita reflexo excessivo e garante leitura confortável para baixa visão.
- **Branco Puro (Card Surface):** `#FFFFFF` com bordas estruturais visíveis de `2px solid #CBD5E1` (estilo Neo-Brutalismo Suave / Utilitário, facilitando a distinção de limites de botões por idosos).

## 3. Tipografia e Legibilidade
- **Fonte Principal (Títulos e Botões):** `Atkinson Hyperlegible` ou `Plus Jakarta Sans` (Bold / ExtraBold) — Projetada especificamente para leitores com baixa visão, diferenciando claramente caracteres ambíguos como `0/O` e `1/l/I`.
- **Fonte Secundária (Dados, Índices e Distâncias):** `Inter` ou `JetBrains Mono` (SemiBold) para números de índices (`0 a 100`) e metragens (`240m`).
- **Regras de Escala Acessível:**
  - Tamanho mínimo de corpo de texto: `18px` (escalável para `22px` no Modo Dona Neide / Idoso).
  - Altura mínima de áreas clicáveis (Touch Targets): `56px` a `64px` (permite acionamento fácil com uma única mão enquanto se empurra um carrinho de bebê ou toca a cadeira de rodas).

## 4. Elementos Gráficos e Iconografia
- **Padrão Geométrico de Apoio:** Linhas paralelas arredondadas (remetendo ao piso tátil direcional) e círculos alinhados (remetendo ao piso tátil de alerta) usados de forma sutil em cabeçalhos e divisores.
- **Ícones:** Traço espesso (`stroke-width: 2.5px`), cantos arredondados, sempre acompanhados de rótulo em texto explícito (nunca usar ícone sozinho sem texto).
- **Representação de Rotas no Mapa:**
  - *Rota Acessível Recomendada:* Linha contínua espessa (`6px`) em Verde Rota Livre (`#00A86B`) com ícones de verificação.
  - *Rota Curta com Barreiras:* Linha tracejada (`4px`) em Vermelho Barreira (`#D90429`) com badge de alerta indicando o número de obstáculos evitados.

## 5. Instrução para Geração das 3 Telas do Protótipo (Mobile-First 390x844px)
Construa um protótipo interativo em HTML5 + Tailwind CSS aplicando rigorosamente esta identidade visual nas 3 telas obrigatórias do CalçadaLivre:
1. **Tela 1 (Escolha de Perfil em 1 Toque):** Cabeçalho `#0B2545`, pergunta direta *"Como você vai se deslocar hoje?"* e 3 cards verticais gigantes com borda de `2px`, ícone grande à esquerda e alto contraste: **Cadeira de Rodas** (Seu Antônio), **Carrinho de Bebê** (Camila) e **Idoso / Bengala** (Dona Neide), mais botão de ativação rápida de *Letras Grandes e Voz*.
2. **Tela 2 (Comparador de Rotas):** Mapa estilizado mostrando a origem e o destino (ex: *Casa → Posto de Saúde*), comparando lado a lado o card da **Rota Mais Acessível** (`420m · Índice 92/100 · 0 Barreiras · Recomendada`, destacada em verde `#00A86B`) contra a **Rota Mais Curta** (`290m · Índice 28/100 · 2 Degraus Intransitáveis`, marcada em vermelho `#D90429`).
3. **Tela 3 (Registro Rápido de Barreira em <30s):** Pino fixado no trecho atual, grade 2x2 de botões grandes em Amarelo Piso Tátil (`#FFB703`) e Vermelho (`#D90429`) para escolher o tipo (`Degrau`, `Buraco`, `Poste no Meio`, `Sem Rampa`), seletor de gravidade de 1 a 5 em blocos grandes, botão de anexar foto e botão final *"Enviar Alerta para a Comunidade"*.