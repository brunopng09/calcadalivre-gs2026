package br.com.fiap.excecoes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento centralizado das excecoes da API.
 * Recurso nao encontrado -> 404 | Regra de negocio -> 400 | Persistencia -> 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(RecursoNaoEncontradoException excecao,
                                                                   HttpServletRequest requisicao) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso não encontrado", excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraDeNegocio(RegraNegocioException excecao,
                                                                    HttpServletRequest requisicao) {
        return resposta(HttpStatus.BAD_REQUEST, "Regra de negócio violada", excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<Map<String, Object>> tratarPersistencia(PersistenciaException excecao,
                                                                  HttpServletRequest requisicao) {
        LOG.error("Falha de persistencia em {}", requisicao.getRequestURI(), excecao);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de persistência", excecao.getMessage(), requisicao);
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String erro, String mensagem,
                                                         HttpServletRequest requisicao) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", erro);
        corpo.put("mensagem", mensagem);
        corpo.put("caminho", requisicao.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}
