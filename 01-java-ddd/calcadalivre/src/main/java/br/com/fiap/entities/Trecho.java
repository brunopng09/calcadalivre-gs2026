package br.com.fiap.entities;

import java.time.LocalDate;

/**
 * Trecho de calcada entre dois pontos (tabela CL_TRECHO).
 */
public class Trecho {

    private int idTrecho;
    private int idPontoOrigem;
    private int idPontoDestino;
    private String nmRua;
    private int idTipoVia;
    private int nrComprimentoM;
    private double nrLarguraTotalM;
    private double nrLarguraLivreM;
    private double nrInclinacaoPct;
    private int nrBuracos;
    private String stTemDegrau;
    private int nrObstaculos100m;
    private String stOcupadoComercio;
    private String stRampaEsquina;
    private String stPisoTatil;
    private int nrIdadeCalcadaAnos;
    private int nrFluxoPedestresHora;
    private double nrIndiceAcessibilidade;
    private double nrProbIntransitavel;
    private LocalDate dtUltimaVistoria;

    public Trecho() {
    }

    public Trecho(int idTrecho, int idPontoOrigem, int idPontoDestino, String nmRua, int idTipoVia,
                  int nrComprimentoM, double nrLarguraTotalM, double nrLarguraLivreM, double nrInclinacaoPct,
                  int nrBuracos, String stTemDegrau, int nrObstaculos100m, String stOcupadoComercio,
                  String stRampaEsquina, String stPisoTatil, int nrIdadeCalcadaAnos, int nrFluxoPedestresHora,
                  double nrIndiceAcessibilidade, double nrProbIntransitavel, LocalDate dtUltimaVistoria) {
        this.idTrecho = idTrecho;
        this.idPontoOrigem = idPontoOrigem;
        this.idPontoDestino = idPontoDestino;
        this.nmRua = nmRua;
        this.idTipoVia = idTipoVia;
        this.nrComprimentoM = nrComprimentoM;
        this.nrLarguraTotalM = nrLarguraTotalM;
        this.nrLarguraLivreM = nrLarguraLivreM;
        this.nrInclinacaoPct = nrInclinacaoPct;
        this.nrBuracos = nrBuracos;
        this.stTemDegrau = stTemDegrau;
        this.nrObstaculos100m = nrObstaculos100m;
        this.stOcupadoComercio = stOcupadoComercio;
        this.stRampaEsquina = stRampaEsquina;
        this.stPisoTatil = stPisoTatil;
        this.nrIdadeCalcadaAnos = nrIdadeCalcadaAnos;
        this.nrFluxoPedestresHora = nrFluxoPedestresHora;
        this.nrIndiceAcessibilidade = nrIndiceAcessibilidade;
        this.nrProbIntransitavel = nrProbIntransitavel;
        this.dtUltimaVistoria = dtUltimaVistoria;
    }

    public int getIdTrecho() { return idTrecho; }
    public void setIdTrecho(int idTrecho) { this.idTrecho = idTrecho; }

    public int getIdPontoOrigem() { return idPontoOrigem; }
    public void setIdPontoOrigem(int idPontoOrigem) { this.idPontoOrigem = idPontoOrigem; }

    public int getIdPontoDestino() { return idPontoDestino; }
    public void setIdPontoDestino(int idPontoDestino) { this.idPontoDestino = idPontoDestino; }

    public String getNmRua() { return nmRua; }
    public void setNmRua(String nmRua) { this.nmRua = nmRua; }

    public int getIdTipoVia() { return idTipoVia; }
    public void setIdTipoVia(int idTipoVia) { this.idTipoVia = idTipoVia; }

    public int getNrComprimentoM() { return nrComprimentoM; }
    public void setNrComprimentoM(int nrComprimentoM) { this.nrComprimentoM = nrComprimentoM; }

    public double getNrLarguraTotalM() { return nrLarguraTotalM; }
    public void setNrLarguraTotalM(double nrLarguraTotalM) { this.nrLarguraTotalM = nrLarguraTotalM; }

    public double getNrLarguraLivreM() { return nrLarguraLivreM; }
    public void setNrLarguraLivreM(double nrLarguraLivreM) { this.nrLarguraLivreM = nrLarguraLivreM; }

    public double getNrInclinacaoPct() { return nrInclinacaoPct; }
    public void setNrInclinacaoPct(double nrInclinacaoPct) { this.nrInclinacaoPct = nrInclinacaoPct; }

    public int getNrBuracos() { return nrBuracos; }
    public void setNrBuracos(int nrBuracos) { this.nrBuracos = nrBuracos; }

    public String getStTemDegrau() { return stTemDegrau; }
    public void setStTemDegrau(String stTemDegrau) { this.stTemDegrau = stTemDegrau; }

    public int getNrObstaculos100m() { return nrObstaculos100m; }
    public void setNrObstaculos100m(int nrObstaculos100m) { this.nrObstaculos100m = nrObstaculos100m; }

    public String getStOcupadoComercio() { return stOcupadoComercio; }
    public void setStOcupadoComercio(String stOcupadoComercio) { this.stOcupadoComercio = stOcupadoComercio; }

    public String getStRampaEsquina() { return stRampaEsquina; }
    public void setStRampaEsquina(String stRampaEsquina) { this.stRampaEsquina = stRampaEsquina; }

    public String getStPisoTatil() { return stPisoTatil; }
    public void setStPisoTatil(String stPisoTatil) { this.stPisoTatil = stPisoTatil; }

    public int getNrIdadeCalcadaAnos() { return nrIdadeCalcadaAnos; }
    public void setNrIdadeCalcadaAnos(int nrIdadeCalcadaAnos) { this.nrIdadeCalcadaAnos = nrIdadeCalcadaAnos; }

    public int getNrFluxoPedestresHora() { return nrFluxoPedestresHora; }
    public void setNrFluxoPedestresHora(int nrFluxoPedestresHora) { this.nrFluxoPedestresHora = nrFluxoPedestresHora; }

    public double getNrIndiceAcessibilidade() { return nrIndiceAcessibilidade; }
    public void setNrIndiceAcessibilidade(double nrIndiceAcessibilidade) { this.nrIndiceAcessibilidade = nrIndiceAcessibilidade; }

    public double getNrProbIntransitavel() { return nrProbIntransitavel; }
    public void setNrProbIntransitavel(double nrProbIntransitavel) { this.nrProbIntransitavel = nrProbIntransitavel; }

    public LocalDate getDtUltimaVistoria() { return dtUltimaVistoria; }
    public void setDtUltimaVistoria(LocalDate dtUltimaVistoria) { this.dtUltimaVistoria = dtUltimaVistoria; }

    @Override
    public String toString() {
        return "Trecho{" +
                "idTrecho=" + idTrecho +
                ", idPontoOrigem=" + idPontoOrigem +
                ", idPontoDestino=" + idPontoDestino +
                ", nmRua='" + nmRua + '\'' +
                ", idTipoVia=" + idTipoVia +
                ", nrComprimentoM=" + nrComprimentoM +
                ", nrLarguraTotalM=" + nrLarguraTotalM +
                ", nrLarguraLivreM=" + nrLarguraLivreM +
                ", nrInclinacaoPct=" + nrInclinacaoPct +
                ", nrBuracos=" + nrBuracos +
                ", stTemDegrau='" + stTemDegrau + '\'' +
                ", nrObstaculos100m=" + nrObstaculos100m +
                ", stOcupadoComercio='" + stOcupadoComercio + '\'' +
                ", stRampaEsquina='" + stRampaEsquina + '\'' +
                ", stPisoTatil='" + stPisoTatil + '\'' +
                ", nrIdadeCalcadaAnos=" + nrIdadeCalcadaAnos +
                ", nrFluxoPedestresHora=" + nrFluxoPedestresHora +
                ", nrIndiceAcessibilidade=" + nrIndiceAcessibilidade +
                ", nrProbIntransitavel=" + nrProbIntransitavel +
                ", dtUltimaVistoria=" + dtUltimaVistoria +
                '}';
    }
}
