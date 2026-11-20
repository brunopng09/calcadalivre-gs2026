package br.com.fiap.contingencia;

import br.com.fiap.entities.TipoBarreira;
import br.com.fiap.entities.Trecho;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Snapshot de contingência do banco Oracle da FIAP.
 *
 * <p>Enquanto a conta do schema estiver bloqueada (ORA-28000) nenhuma consulta funciona.
 * Este snapshot foi capturado em 16/11/2026, quando o banco ainda respondia, e só é
 * servido pelas telas e pela API dentro do tratamento de falha de persistência.</p>
 *
 * <p>As listas são lidas dos resources uma única vez, na primeira chamada. Qualquer
 * erro de leitura ou de parse gera WARN e lista vazia: o modo contingência nunca
 * derruba a aplicação.</p>
 */
public final class DadosContingencia {

    /** Texto do aviso exibido no banner das páginas servidas a partir do snapshot. */
    public static final String AVISO_DADOS_DE_CACHE =
            "Banco Oracle da FIAP indisponível no momento. "
                    + "Os dados abaixo são os últimos sincronizados, em 16/11/2026; "
                    + "o diagnóstico técnico da falha está em /health/db.";

    /** Header da API que sinaliza que a resposta veio do snapshot, não do banco. */
    public static final String CABECALHO_DADOS_DE_CACHE = "X-Dados-De-Cache";

    private static final Logger LOG = LoggerFactory.getLogger(DadosContingencia.class);
    private static final ObjectMapper MAPPER = JsonMapper.builder().build();
    private static final String ARQUIVO_TRECHOS = "fallback/trechos.json";
    private static final String ARQUIVO_TIPOS_BARREIRA = "fallback/tipos-barreira.json";

    private static final List<Trecho> TRECHOS =
            carregar(ARQUIVO_TRECHOS, new TypeReference<List<Trecho>>() {});
    private static final List<TipoBarreira> TIPOS_BARREIRA =
            carregar(ARQUIVO_TIPOS_BARREIRA, new TypeReference<List<TipoBarreira>>() {});

    private DadosContingencia() {
    }

    /** Últimos trechos conhecidos (14 registros do snapshot de 16/11/2026). */
    public static List<Trecho> trechos() {
        return TRECHOS;
    }

    /** Últimos tipos de barreira conhecidos (6 registros do snapshot de 16/11/2026). */
    public static List<TipoBarreira> tiposBarreira() {
        return TIPOS_BARREIRA;
    }

    private static <T> List<T> carregar(String caminho, TypeReference<List<T>> tipo) {
        ClassPathResource recurso = new ClassPathResource(caminho);
        try (InputStream entrada = recurso.getInputStream()) {
            List<T> dados = MAPPER.readValue(entrada, tipo);
            return List.copyOf(dados);
        } catch (IOException | RuntimeException e) {
            LOG.warn("Snapshot de contingência indisponível ou inválido em {}: servindo lista vazia", caminho, e);
            return List.of();
        }
    }
}
