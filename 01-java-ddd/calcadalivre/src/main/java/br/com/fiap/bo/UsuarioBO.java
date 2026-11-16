package br.com.fiap.bo;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.entities.Usuario;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;

import java.time.LocalDate;
import java.util.List;

/**
 * Regras de negocio de usuario: e-mail valido e unico, nome nao vazio.
 */
public class UsuarioBO {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<Usuario> listar() {
        return usuarioDAO.listar();
    }

    public Usuario buscarPorId(int id) throws RecursoNaoEncontradoException {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado: id " + id);
        }
        return usuario;
    }

    public Usuario inserir(Usuario usuario) throws RegraNegocioException {
        validar(usuario);
        String email = usuario.getNmEmail().trim();
        if (usuarioDAO.buscarPorEmail(email) != null) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com o e-mail " + email);
        }
        usuario.setNmUsuario(usuario.getNmUsuario().trim());
        usuario.setNmEmail(email);
        normalizar(usuario);
        usuario.setIdUsuario(usuarioDAO.inserir(usuario));
        return usuario;
    }

    public Usuario atualizar(int id, Usuario usuario) throws RegraNegocioException, RecursoNaoEncontradoException {
        buscarPorId(id);
        validar(usuario);
        String email = usuario.getNmEmail().trim();
        Usuario mesmoEmail = usuarioDAO.buscarPorEmail(email);
        if (mesmoEmail != null && mesmoEmail.getIdUsuario() != id) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com o e-mail " + email);
        }
        usuario.setIdUsuario(id);
        usuario.setNmUsuario(usuario.getNmUsuario().trim());
        usuario.setNmEmail(email);
        normalizar(usuario);
        usuarioDAO.atualizar(usuario);
        return usuario;
    }

    public void deletar(int id) throws RecursoNaoEncontradoException {
        buscarPorId(id);
        usuarioDAO.deletar(id);
    }

    private void validar(Usuario usuario) throws RegraNegocioException {
        if (usuario == null) {
            throw new RegraNegocioException("Os dados do usuário não foram informados");
        }
        if (usuario.getNmUsuario() == null || usuario.getNmUsuario().isBlank()) {
            throw new RegraNegocioException("O nome do usuário é obrigatório");
        }
        if (usuario.getNmEmail() == null || !usuario.getNmEmail().trim().matches(EMAIL_REGEX)) {
            throw new RegraNegocioException("Informe um e-mail válido para o usuário");
        }
    }

    private void normalizar(Usuario usuario) {
        if (usuario.getStAtivo() == null || usuario.getStAtivo().isBlank()) {
            usuario.setStAtivo("S");
        }
        if (usuario.getDtCadastro() == null) {
            usuario.setDtCadastro(LocalDate.now());
        }
    }
}
