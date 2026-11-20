package br.com.fiap.controller;

import br.com.fiap.conexoes.ConexaoFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Diagnostico da camada de persistencia.
 * Testa a conexao com o Oracle da FIAP sem depender das consultas de dominio,
 * para distinguir falha de credencial, de rede e de banco fora do ar em producao.
 * Nao expoe usuario nem senha: apenas a classe e a mensagem da excecao.
 */
@RestController
@RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> verificarBanco() {
        Map<String, Object> corpo = new LinkedHashMap<>();
        try (Connection conexao = new ConexaoFactory().conexao()) {
            corpo.put("banco", "ok");
            corpo.put("conexaoValida", conexao.isValid(5));
            return ResponseEntity.ok(corpo);
        } catch (Exception excecao) {
            corpo.put("banco", "falha");
            corpo.put("tipo", excecao.getClass().getName());
            corpo.put("mensagem", String.valueOf(excecao.getMessage()));
            Throwable causa = excecao;
            while (causa.getCause() != null && causa.getCause() != causa) {
                causa = causa.getCause();
            }
            corpo.put("causaTipo", causa.getClass().getName());
            corpo.put("causa", String.valueOf(causa.getMessage()));
            return ResponseEntity.status(503).body(corpo);
        }
    }
}
