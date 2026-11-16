package br.com.fiap.controller;

import br.com.fiap.bo.TrechoBO;
import br.com.fiap.entities.Trecho;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * API REST de trechos de calcada.
 */
@RestController
@RequestMapping(value = "/api/trechos", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrechoController {

    private final TrechoBO trechoBO = new TrechoBO();

    @GetMapping
    public ResponseEntity<List<Trecho>> listar() {
        return ResponseEntity.ok(trechoBO.listar());
    }

    @GetMapping("/prioridade")
    public ResponseEntity<List<Map<String, Object>>> listarPrioridade() {
        return ResponseEntity.ok(trechoBO.listarPrioridade());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trecho> buscar(@PathVariable int id) throws RecursoNaoEncontradoException {
        return ResponseEntity.ok(trechoBO.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Trecho> criar(@RequestBody Trecho trecho) throws RegraNegocioException {
        Trecho criado = trechoBO.inserir(trecho);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getIdTrecho())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trecho> atualizar(@PathVariable int id, @RequestBody Trecho trecho)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        return ResponseEntity.ok(trechoBO.atualizar(id, trecho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        trechoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
