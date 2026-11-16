package br.com.fiap.excecoes;

/**
 * Excecao base (checada) do dominio CalcaLivre.
 * Todas as regras de negocio e recursos nao encontrados derivam dela.
 */
public class CalcadaLivreException extends Exception {

    public CalcadaLivreException(String mensagem) {
        super(mensagem);
    }

    public CalcadaLivreException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public CalcadaLivreException(Throwable causa) {
        super(causa);
    }
}
