package br.com.fiap.bo;

import br.com.fiap.dao.AvaliacaoTrechoDAO;
import br.com.fiap.dao.TrechoDAO;
import br.com.fiap.entities.AvaliacaoTrecho;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.RegraNegocioException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras de negocio da avaliacao de trecho: nota entre 0 e 10.
 */
public class AvaliacaoTrechoBO {

    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;

    private final AvaliacaoTrechoDAO avaliacaoTrechoDAO = new AvaliacaoTrechoDAO();
    private final TrechoDAO trechoDAO = new TrechoDAO();

    public List<AvaliacaoTrecho> listar() {
        return avaliacaoTrechoDAO.listar();
    }

    public AvaliacaoTrecho buscarPorId(int id) throws RecursoNaoEncontradoException {
        AvaliacaoTrecho avaliacao = avaliacaoTrechoDAO.buscarPorId(id);
        if (avaliacao == null) {
            throw new RecursoNaoEncontradoException("Avaliação não encontrada: id " + id);
        }
        return avaliacao;
    }

    public List<AvaliacaoTrecho> listarPorTrecho(int idTrecho) {
        return avaliacaoTrechoDAO.listarPorTrecho(idTrecho);
    }

    public AvaliacaoTrecho inserir(AvaliacaoTrecho avaliacao) throws RegraNegocioException {
        validar(avaliacao);
        normalizar(avaliacao);
        avaliacao.setIdAvaliacao(avaliacaoTrechoDAO.inserir(avaliacao));
        return avaliacao;
    }

    public AvaliacaoTrecho atualizar(int id, AvaliacaoTrecho avaliacao)
            throws RegraNegocioException, RecursoNaoEncontradoException {
        buscarPorId(id);
        validar(avaliacao);
        avaliacao.setIdAvaliacao(id);
        normalizar(avaliacao);
        avaliacaoTrechoDAO.atualizar(avaliacao);
        return avaliacao;
    }

    public void deletar(int id) throws RecursoNaoEncontradoException {
        buscarPorId(id);
        avaliacaoTrechoDAO.deletar(id);
    }

    private void validar(AvaliacaoTrecho avaliacao) throws RegraNegocioException {
        if (avaliacao == null) {
            throw new RegraNegocioException("Os dados da avaliação não foram informados");
        }
        if (avaliacao.getNrIndiceAvaliado() < NOTA_MINIMA || avaliacao.getNrIndiceAvaliado() > NOTA_MAXIMA) {
            throw new RegraNegocioException("A nota da avaliação deve estar entre 0 e 10");
        }
        if (trechoDAO.buscarPorId(avaliacao.getIdTrecho()) == null) {
            throw new RegraNegocioException("O trecho informado não existe: id " + avaliacao.getIdTrecho());
        }
    }

    private void normalizar(AvaliacaoTrecho avaliacao) {
        if (avaliacao.getStTransitavel() == null || avaliacao.getStTransitavel().isBlank()) {
            avaliacao.setStTransitavel("S");
        }
        if (avaliacao.getDtAvaliacao() == null) {
            avaliacao.setDtAvaliacao(LocalDateTime.now());
        }
    }
}
