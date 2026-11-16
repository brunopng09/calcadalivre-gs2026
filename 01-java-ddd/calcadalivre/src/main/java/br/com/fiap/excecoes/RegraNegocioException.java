package br.com.fiap.excecoes;

/**
 * Violacao de regra de negocio (HTTP 400).
 */
public class RegraNegocioException extends CalcadaLivreException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RegraNegocioException(Throwable causa) {
        super(causa);
    }
}
