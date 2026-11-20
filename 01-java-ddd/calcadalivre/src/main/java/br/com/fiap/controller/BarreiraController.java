package br.com.fiap.controller;

import br.com.fiap.bo.RegistroBarreiraBO;
import br.com.fiap.contingencia.DadosContingencia;
import br.com.fiap.entities.RegistroBarreira;
import br.com.fiap.entities.TipoBarreira;
import br.com.fiap.excecoes.PersistenciaException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * API REST de registros de barreira.
 */
@RestController
@RequestMapping(value = "/api/barreiras", produces = MediaType.APPLICATION_JSON_VALUE)
public class BarreiraController {

    private static final Logger LOG = LoggerFactory.getLogger(BarreiraController.class);

    private final RegistroBarreiraBO registroBarreiraBO = new RegistroBarreiraBO();

    @GetMapping
    public ResponseEntity<List<RegistroBarreira>> listar() {
        return ResponseEntity.ok(registroBarreiraBO.listar());
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoBarreira>> listarTipos() {
        try {
            return ResponseEntity.ok(registroBarreiraBO.listarTipos());
        } catch (PersistenciaException e) {
            LOG.warn("Falha ao listar tipos de barreira na API; servindo snapshot de contingência", e);
            return ResponseEntity.ok()
                    .header(DadosContingencia.CABECALHO_DADOS_DE_CACHE, "true")
                    .body(DadosContingencia.tiposBarreira());
        }
    }

    @GetMapping("/trecho/{idTrecho}")
    public ResponseEntity<List<RegistroBarreira>> listarPorTrecho(@PathVariable int idTrecho)
            throws RecursoNaoEncontradoException {
        return ResponseEntity.ok(registroBarreiraBO.listarPorTrecho(idTrecho));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroBarreira> buscar(@PathVariable int id) throws RecursoNaoEncontradoException {
        return ResponseEntity.ok(registroBarreiraBO.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegistroBarreira> criar(@RequestBody RegistroBarreira registro)
            throws RegraNegocioException {
        RegistroBarreira criado = registroBarreiraBO.inserir(registro);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getIdRegistro())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroBarreira> atualizar(@PathVariable int id,
                                                      @RequestBody RegistroBarreira registro)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        return ResponseEntity.ok(registroBarreiraBO.atualizar(id, registro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws RecursoNaoEncontradoException {
        registroBarreiraBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
