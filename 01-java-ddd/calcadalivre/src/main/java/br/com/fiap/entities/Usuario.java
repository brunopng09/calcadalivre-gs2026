package br.com.fiap.entities;

import java.time.LocalDate;

/**
 * Usuario da solucao (tabela CL_USUARIO).
 */
public class Usuario {

    private int idUsuario;
    private String nmUsuario;
    private String nmEmail;
    private Integer idPerfilPreferido;
    private String stAtivo;
    private LocalDate dtCadastro;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nmUsuario, String nmEmail, Integer idPerfilPreferido,
                   String stAtivo, LocalDate dtCadastro) {
        this.idUsuario = idUsuario;
        this.nmUsuario = nmUsuario;
        this.nmEmail = nmEmail;
        this.idPerfilPreferido = idPerfilPreferido;
        this.stAtivo = stAtivo;
        this.dtCadastro = dtCadastro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getNmEmail() {
        return nmEmail;
    }

    public void setNmEmail(String nmEmail) {
        this.nmEmail = nmEmail;
    }

    public Integer getIdPerfilPreferido() {
        return idPerfilPreferido;
    }

    public void setIdPerfilPreferido(Integer idPerfilPreferido) {
        this.idPerfilPreferido = idPerfilPreferido;
    }

    public String getStAtivo() {
        return stAtivo;
    }

    public void setStAtivo(String stAtivo) {
        this.stAtivo = stAtivo;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nmUsuario='" + nmUsuario + '\'' +
                ", nmEmail='" + nmEmail + '\'' +
                ", idPerfilPreferido=" + idPerfilPreferido +
                ", stAtivo='" + stAtivo + '\'' +
                ", dtCadastro=" + dtCadastro +
                '}';
    }
}
