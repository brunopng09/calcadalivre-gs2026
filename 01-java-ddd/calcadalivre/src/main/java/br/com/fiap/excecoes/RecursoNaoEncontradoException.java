package br.com.fiap.excecoes;

/**
 * Recurso inexistente (HTTP 404).
 */
public class RecursoNaoEncontradoException extends CalcadaLivreException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RecursoNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}
