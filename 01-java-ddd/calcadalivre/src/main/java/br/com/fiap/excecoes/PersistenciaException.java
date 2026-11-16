package br.com.fiap.excecoes;

/**
 * Falha de acesso ao banco (HTTP 500). Embrulha a SQLException original.
 */
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String mensagem) {
        super(mensagem);
    }

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public PersistenciaException(Throwable causa) {
        super(causa);
    }
}
