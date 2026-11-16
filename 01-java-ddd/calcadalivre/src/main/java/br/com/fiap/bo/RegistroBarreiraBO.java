package br.com.fiap.bo;

import br.com.fiap.dao.RegistroBarreiraDAO;
import br.com.fiap.dao.TipoBarreiraDAO;
import br.com.fiap.dao.TrechoDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.entities.RegistroBarreira;
import br.com.fiap.entities.TipoBarreira;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Regras de negocio do registro de barreira: gravidade 1 a 5, data nao futura,
 * trecho e usuario existentes.
 */
public class RegistroBarreiraBO {

    private static final int GRAVIDADE_MINIMA = 1;
    private static final int GRAVIDADE_MAXIMA = 5;

    private final RegistroBarreiraDAO registroBarreiraDAO = new RegistroBarreiraDAO();
    private final TrechoDAO trechoDAO = new TrechoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final TipoBarreiraDAO tipoBarreiraDAO = new TipoBarreiraDAO();

    public List<RegistroBarreira> listar() {
        return registroBarreiraDAO.listar();
    }

    public List<TipoBarreira> listarTipos() {
        return tipoBarreiraDAO.listar();
    }

    public Map<Integer, Integer> contarPorTrecho() {
        return registroBarreiraDAO.contarPorTrecho();
    }

    public RegistroBarreira buscarPorId(int id) throws RecursoNaoEncontradoException {
        RegistroBarreira registro = registroBarreiraDAO.buscarPorId(id);
        if (registro == null) {
            throw new RecursoNaoEncontradoException("Registro de barreira não encontrado: id " + id);
        }
        return registro;
    }

    public List<RegistroBarreira> listarPorTrecho(int idTrecho) throws RecursoNaoEncontradoException {
        if (trechoDAO.buscarPorId(idTrecho) == null) {
            throw new RecursoNaoEncontradoException("Trecho não encontrado: id " + idTrecho);
        }
        return registroBarreiraDAO.listarPorTrecho(idTrecho);
    }

    public RegistroBarreira inserir(RegistroBarreira registro) throws RegraNegocioException {
        validar(registro);
        normalizar(registro);
        registro.setIdRegistro(registroBarreiraDAO.inserir(registro));
        return registro;
    }

    public RegistroBarreira atualizar(int id, RegistroBarreira registro)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        buscarPorId(id);
        validar(registro);
        registro.setIdRegistro(id);
        normalizar(registro);
        registroBarreiraDAO.atualizar(registro);
        return registro;
    }

    public void deletar(int id) throws RecursoNaoEncontradoException {
        buscarPorId(id);
        registroBarreiraDAO.deletar(id);
    }

    private void validar(RegistroBarreira registro) throws RegraNegocioException {
        if (registro == null) {
            throw new RegraNegocioException("Os dados do registro de barreira não foram informados");
        }
        if (registro.getNrGravidade() < GRAVIDADE_MINIMA || registro.getNrGravidade() > GRAVIDADE_MAXIMA) {
            throw new RegraNegocioException("A gravidade deve estar entre 1 e 5");
        }
        if (registro.getDtRegistro() != null && registro.getDtRegistro().isAfter(LocalDateTime.now())) {
            throw new RegraNegocioException("A data do registro não pode ser futura");
        }
        if (trechoDAO.buscarPorId(registro.getIdTrecho()) == null) {
            throw new RegraNegocioException("O trecho informado não existe: id " + registro.getIdTrecho());
        }
        if (usuarioDAO.buscarPorId(registro.getIdUsuario()) == null) {
            throw new RegraNegocioException("O usuário informado não existe: id " + registro.getIdUsuario());
        }
    }

    private void normalizar(RegistroBarreira registro) {
        if (registro.getDtRegistro() == null) {
            registro.setDtRegistro(LocalDateTime.now());
        }
        if (registro.getStStatus() == null || registro.getStStatus().isBlank()) {
            registro.setStStatus("ABERTO");
        }
    }
}
