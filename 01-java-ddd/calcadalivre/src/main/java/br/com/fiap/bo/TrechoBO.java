package br.com.fiap.bo;

import br.com.fiap.dao.RegistroBarreiraDAO;
import br.com.fiap.dao.TrechoDAO;
import br.com.fiap.entities.Trecho;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Regras de negocio de trecho: indice entre 0 e 10, rua obrigatoria e
 * bloqueio de exclusao de trecho que possua registros de barreira.
 */
public class TrechoBO {

    private static final double INDICE_MINIMO = 0.0;
    private static final double INDICE_MAXIMO = 10.0;

    private final TrechoDAO trechoDAO = new TrechoDAO();
    private final RegistroBarreiraDAO registroBarreiraDAO = new RegistroBarreiraDAO();

    public List<Trecho> listar() {
        return trechoDAO.listar();
    }

    public List<Map<String, Object>> listarPrioridade() {
        return trechoDAO.listarPrioridade();
    }

    public Trecho buscarPorId(int id) throws RecursoNaoEncontradoException {
        Trecho trecho = trechoDAO.buscarPorId(id);
        if (trecho == null) {
            throw new RecursoNaoEncontradoException("Trecho não encontrado: id " + id);
        }
        return trecho;
    }

    public Trecho inserir(Trecho trecho) throws RegraNegocioException {
        validar(trecho);
        normalizar(trecho);
        trecho.setIdTrecho(trechoDAO.inserir(trecho));
        return trecho;
    }

    public Trecho atualizar(int id, Trecho trecho) throws RegraNegocioException, RecursoNaoEncontradoException {
        buscarPorId(id);
        validar(trecho);
        trecho.setIdTrecho(id);
        normalizar(trecho);
        trechoDAO.atualizar(trecho);
        return trecho;
    }

    public void deletar(int id) throws RegraNegocioException, RecursoNaoEncontradoException {
        buscarPorId(id);
        if (!registroBarreiraDAO.listarPorTrecho(id).isEmpty()) {
            throw new RegraNegocioException(
                    "Não é possível excluir o trecho " + id + ": existem registros de barreira vinculados");
        }
        trechoDAO.deletar(id);
    }

    private void validar(Trecho trecho) throws RegraNegocioException {
        if (trecho == null) {
            throw new RegraNegocioException("Os dados do trecho não foram informados");
        }
        if (trecho.getNmRua() == null || trecho.getNmRua().isBlank()) {
            throw new RegraNegocioException("O nome da rua do trecho é obrigatório");
        }
        if (trecho.getNrIndiceAcessibilidade() < INDICE_MINIMO
                || trecho.getNrIndiceAcessibilidade() > INDICE_MAXIMO) {
            throw new RegraNegocioException("O índice de acessibilidade deve estar entre 0 e 10");
        }
    }

    private void normalizar(Trecho trecho) {
        trecho.setNmRua(trecho.getNmRua().trim());
        trecho.setStTemDegrau(valorOuPadrao(trecho.getStTemDegrau()));
        trecho.setStOcupadoComercio(valorOuPadrao(trecho.getStOcupadoComercio()));
        trecho.setStRampaEsquina(valorOuPadrao(trecho.getStRampaEsquina()));
        trecho.setStPisoTatil(valorOuPadrao(trecho.getStPisoTatil()));
        if (trecho.getDtUltimaVistoria() == null) {
            trecho.setDtUltimaVistoria(LocalDate.now());
        }
    }

    private String valorOuPadrao(String valor) {
        return valor == null || valor.isBlank() ? "N" : valor;
    }
}
