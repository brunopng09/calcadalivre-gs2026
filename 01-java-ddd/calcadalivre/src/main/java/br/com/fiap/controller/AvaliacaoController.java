package br.com.fiap.controller;

import br.com.fiap.bo.AvaliacaoTrechoBO;
import br.com.fiap.entities.AvaliacaoTrecho;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * API REST de avaliacoes de trecho.
 */
@RestController
@RequestMapping(value = "/api/avaliacoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvaliacaoController {

    private final AvaliacaoTrechoBO avaliacaoTrechoBO = new AvaliacaoTrechoBO();

    @GetMapping("/trecho/{idTrecho}")
    public ResponseEntity<List<AvaliacaoTrecho>> listarPorTrecho(@PathVariable int idTrecho) {
        return ResponseEntity.ok(avaliacaoTrechoBO.listarPorTrecho(idTrecho));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AvaliacaoTrecho> criar(@RequestBody AvaliacaoTrecho avaliacao)
            throws RegraNegocioException {
        AvaliacaoTrecho criada = avaliacaoTrechoBO.inserir(avaliacao);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getIdAvaliacao())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws RecursoNaoEncontradoException {
        avaliacaoTrechoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
