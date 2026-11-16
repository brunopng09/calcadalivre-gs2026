package br.com.fiap.controller;

import br.com.fiap.bo.UsuarioBO;
import br.com.fiap.entities.Usuario;
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

/**
 * API REST de usuarios.
 */
@RestController
@RequestMapping(value = "/api/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController {

    private final UsuarioBO usuarioBO = new UsuarioBO();

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable int id) throws RecursoNaoEncontradoException {
        return ResponseEntity.ok(usuarioBO.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) throws RegraNegocioException {
        Usuario criado = usuarioBO.inserir(usuario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getIdUsuario())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable int id, @RequestBody Usuario usuario)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        return ResponseEntity.ok(usuarioBO.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws RecursoNaoEncontradoException {
        usuarioBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
