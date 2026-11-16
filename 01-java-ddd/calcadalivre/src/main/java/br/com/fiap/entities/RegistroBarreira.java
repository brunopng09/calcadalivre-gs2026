package br.com.fiap.entities;

import java.time.LocalDateTime;

/**
 * Alerta de barreira enviado pelo cidadao (tabela CL_REGISTRO_BARREIRA).
 */
public class RegistroBarreira {

    private int idRegistro;
    private int idTrecho;
    private int idUsuario;
    private int idTipoBarreira;
    private int nrGravidade;
    private String dsRegistro;
    private Double nrLatitude;
    private Double nrLongitude;
    private String urlFoto;
    private LocalDateTime dtRegistro;
    private String stStatus;

    public RegistroBarreira() {
    }

    public RegistroBarreira(int idRegistro, int idTrecho, int idUsuario, int idTipoBarreira, int nrGravidade,
                            String dsRegistro, Double nrLatitude, Double nrLongitude, String urlFoto,
                            LocalDateTime dtRegistro, String stStatus) {
        this.idRegistro = idRegistro;
        this.idTrecho = idTrecho;
        this.idUsuario = idUsuario;
        this.idTipoBarreira = idTipoBarreira;
        this.nrGravidade = nrGravidade;
        this.dsRegistro = dsRegistro;
        this.nrLatitude = nrLatitude;
        this.nrLongitude = nrLongitude;
        this.urlFoto = urlFoto;
        this.dtRegistro = dtRegistro;
        this.stStatus = stStatus;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
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

    public int getIdTipoBarreira() {
        return idTipoBarreira;
    }

    public void setIdTipoBarreira(int idTipoBarreira) {
        this.idTipoBarreira = idTipoBarreira;
    }

    public int getNrGravidade() {
        return nrGravidade;
    }

    public void setNrGravidade(int nrGravidade) {
        this.nrGravidade = nrGravidade;
    }

    public String getDsRegistro() {
        return dsRegistro;
    }

    public void setDsRegistro(String dsRegistro) {
        this.dsRegistro = dsRegistro;
    }

    public Double getNrLatitude() {
        return nrLatitude;
    }

    public void setNrLatitude(Double nrLatitude) {
        this.nrLatitude = nrLatitude;
    }

    public Double getNrLongitude() {
        return nrLongitude;
    }

    public void setNrLongitude(Double nrLongitude) {
        this.nrLongitude = nrLongitude;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public LocalDateTime getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDateTime dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    @Override
    public String toString() {
        return "RegistroBarreira{" +
                "idRegistro=" + idRegistro +
                ", idTrecho=" + idTrecho +
                ", idUsuario=" + idUsuario +
                ", idTipoBarreira=" + idTipoBarreira +
                ", nrGravidade=" + nrGravidade +
                ", dsRegistro='" + dsRegistro + '\'' +
                ", nrLatitude=" + nrLatitude +
                ", nrLongitude=" + nrLongitude +
                ", urlFoto='" + urlFoto + '\'' +
                ", dtRegistro=" + dtRegistro +
                ", stStatus='" + stStatus + '\'' +
                '}';
    }
}
