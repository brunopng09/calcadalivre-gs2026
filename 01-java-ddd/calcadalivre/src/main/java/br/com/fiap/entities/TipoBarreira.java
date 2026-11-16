package br.com.fiap.entities;

/**
 * Tipo de barreira reportavel (tabela CL_TIPO_BARREIRA).
 */
public class TipoBarreira {

    private int idTipoBarreira;
    private String dsTipoBarreira;
    private int nrGravidadeBase;
    private String stAtivo;

    public TipoBarreira() {
    }

    public TipoBarreira(int idTipoBarreira, String dsTipoBarreira, int nrGravidadeBase, String stAtivo) {
        this.idTipoBarreira = idTipoBarreira;
        this.dsTipoBarreira = dsTipoBarreira;
        this.nrGravidadeBase = nrGravidadeBase;
        this.stAtivo = stAtivo;
    }

    public int getIdTipoBarreira() {
        return idTipoBarreira;
    }

    public void setIdTipoBarreira(int idTipoBarreira) {
        this.idTipoBarreira = idTipoBarreira;
    }

    public String getDsTipoBarreira() {
        return dsTipoBarreira;
    }

    public void setDsTipoBarreira(String dsTipoBarreira) {
        this.dsTipoBarreira = dsTipoBarreira;
    }

    public int getNrGravidadeBase() {
        return nrGravidadeBase;
    }

    public void setNrGravidadeBase(int nrGravidadeBase) {
        this.nrGravidadeBase = nrGravidadeBase;
    }

    public String getStAtivo() {
        return stAtivo;
    }

    public void setStAtivo(String stAtivo) {
        this.stAtivo = stAtivo;
    }

    @Override
    public String toString() {
        return "TipoBarreira{" +
                "idTipoBarreira=" + idTipoBarreira +
                ", dsTipoBarreira='" + dsTipoBarreira + '\'' +
                ", nrGravidadeBase=" + nrGravidadeBase +
                ", stAtivo='" + stAtivo + '\'' +
                '}';
    }
}
