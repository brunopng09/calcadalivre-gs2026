package br.com.fiap.entities;

import java.time.LocalDateTime;

/**
 * Vistoria de campo que valida o indice de acessibilidade (tabela CL_AVALIACAO_TRECHO).
 */
public class AvaliacaoTrecho {

    private int idAvaliacao;
    private int idTrecho;
    private int idUsuario;
    private int idPerfil;
    private double nrIndiceAvaliado;
    private String stTransitavel;
    private String dsObservacao;
    private LocalDateTime dtAvaliacao;

    public AvaliacaoTrecho() {
    }

    public AvaliacaoTrecho(int idAvaliacao, int idTrecho, int idUsuario, int idPerfil, double nrIndiceAvaliado,
                           String stTransitavel, String dsObservacao, LocalDateTime dtAvaliacao) {
        this.idAvaliacao = idAvaliacao;
        this.idTrecho = idTrecho;
        this.idUsuario = idUsuario;
        this.idPerfil = idPerfil;
        this.nrIndiceAvaliado = nrIndiceAvaliado;
        this.stTransitavel = stTransitavel;
        this.dsObservacao = dsObservacao;
        this.dtAvaliacao = dtAvaliacao;
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(int idTrecho) {
        this.idTrecho = idTrecho;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public double getNrIndiceAvaliado() {
        return nrIndiceAvaliado;
    }

    public void setNrIndiceAvaliado(double nrIndiceAvaliado) {
        this.nrIndiceAvaliado = nrIndiceAvaliado;
    }

    public String getStTransitavel() {
        return stTransitavel;
    }

    public void setStTransitavel(String stTransitavel) {
        this.stTransitavel = stTransitavel;
    }

    public String getDsObservacao() {
        return dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public LocalDateTime getDtAvaliacao() {
        return dtAvaliacao;
    }

    public void setDtAvaliacao(LocalDateTime dtAvaliacao) {
        this.dtAvaliacao = dtAvaliacao;
    }

    @Override
    public String toString() {
        return "AvaliacaoTrecho{" +
                "idAvaliacao=" + idAvaliacao +
                ", idTrecho=" + idTrecho +
                ", idUsuario=" + idUsuario +
                ", idPerfil=" + idPerfil +
                ", nrIndiceAvaliado=" + nrIndiceAvaliado +
                ", stTransitavel='" + stTransitavel + '\'' +
                ", dsObservacao='" + dsObservacao + '\'' +
                ", dtAvaliacao=" + dtAvaliacao +
                '}';
    }
}
