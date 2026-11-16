package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Trecho;
import br.com.fiap.excecoes.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acesso JDBC puro a tabela CL_TRECHO.
 */
public class TrechoDAO {

    private static final String COLUNAS = "id_trecho, id_ponto_origem, id_ponto_destino, nm_rua, id_tipo_via, "
            + "nr_comprimento_m, nr_largura_total_m, nr_largura_livre_m, nr_inclinacao_pct, nr_buracos, "
            + "st_tem_degrau, nr_obstaculos_100m, st_ocupado_comercio, st_rampa_esquina, st_piso_tatil, "
            + "nr_idade_calcada_anos, nr_fluxo_pedestres_hora, nr_indice_acessibilidade, nr_prob_intransitavel, "
            + "dt_ultima_vistoria";

    public List<Trecho> listar() {
        return consultar("SELECT " + COLUNAS + " FROM CL_TRECHO ORDER BY nm_rua", null);
    }

    public Trecho buscarPorId(int id) {
        List<Trecho> lista = consultar("SELECT " + COLUNAS + " FROM CL_TRECHO WHERE id_trecho = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Media do indice de acessibilidade por tipo de via (ranking decrescente).
     */
    public List<Map<String, Object>> listarPrioridade() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        String sql = "SELECT v.ds_tipo_via, COUNT(*) AS qtd_trechos, ROUND(AVG(t.nr_indice_acessibilidade), 1) AS media_indice "
                + "FROM CL_TRECHO t INNER JOIN CL_TIPO_VIA v ON v.id_tipo_via = t.id_tipo_via "
                + "GROUP BY v.ds_tipo_via ORDER BY media_indice DESC";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> linha = new LinkedHashMap<>();
                linha.put("dsTipoVia", rs.getString("ds_tipo_via"));
                linha.put("qtdTrechos", rs.getLong("qtd_trechos"));
                linha.put("mediaIndice", rs.getDouble("media_indice"));
                resultado.add(linha);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao calcular prioridade por tipo de via", e);
        }
        return resultado;
    }

    public int inserir(Trecho trecho) {
        String sql = "INSERT INTO CL_TRECHO (id_ponto_origem, id_ponto_destino, nm_rua, id_tipo_via, "
                + "nr_comprimento_m, nr_largura_total_m, nr_largura_livre_m, nr_inclinacao_pct, nr_buracos, "
                + "st_tem_degrau, nr_obstaculos_100m, st_ocupado_comercio, st_rampa_esquina, st_piso_tatil, "
                + "nr_idade_calcada_anos, nr_fluxo_pedestres_hora, nr_indice_acessibilidade, nr_prob_intransitavel, "
                + "dt_ultima_vistoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_TRECHO" })) {
            preencher(stmt, trecho, 1);
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao inserir trecho", e);
        }
        return 0;
    }

    public void atualizar(Trecho trecho) {
        String sql = "UPDATE CL_TRECHO SET id_ponto_origem = ?, id_ponto_destino = ?, nm_rua = ?, id_tipo_via = ?, "
                + "nr_comprimento_m = ?, nr_largura_total_m = ?, nr_largura_livre_m = ?, nr_inclinacao_pct = ?, "
                + "nr_buracos = ?, st_tem_degrau = ?, nr_obstaculos_100m = ?, st_ocupado_comercio = ?, "
                + "st_rampa_esquina = ?, st_piso_tatil = ?, nr_idade_calcada_anos = ?, nr_fluxo_pedestres_hora = ?, "
                + "nr_indice_acessibilidade = ?, nr_prob_intransitavel = ?, dt_ultima_vistoria = ? "
                + "WHERE id_trecho = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            int proximo = preencher(stmt, trecho, 1);
            stmt.setInt(proximo, trecho.getIdTrecho());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar trecho", e);
        }
    }

    public void deletar(int id) {
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM CL_TRECHO WHERE id_trecho = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao excluir trecho", e);
        }
    }

    private int preencher(PreparedStatement stmt, Trecho trecho, int inicio) throws SQLException {
        int i = inicio;
        stmt.setInt(i++, trecho.getIdPontoOrigem());
        stmt.setInt(i++, trecho.getIdPontoDestino());
        stmt.setString(i++, trecho.getNmRua());
        stmt.setInt(i++, trecho.getIdTipoVia());
        stmt.setInt(i++, trecho.getNrComprimentoM());
        stmt.setDouble(i++, trecho.getNrLarguraTotalM());
        stmt.setDouble(i++, trecho.getNrLarguraLivreM());
        stmt.setDouble(i++, trecho.getNrInclinacaoPct());
        stmt.setInt(i++, trecho.getNrBuracos());
        stmt.setString(i++, trecho.getStTemDegrau());
        stmt.setInt(i++, trecho.getNrObstaculos100m());
        stmt.setString(i++, trecho.getStOcupadoComercio());
        stmt.setString(i++, trecho.getStRampaEsquina());
        stmt.setString(i++, trecho.getStPisoTatil());
        stmt.setInt(i++, trecho.getNrIdadeCalcadaAnos());
        stmt.setInt(i++, trecho.getNrFluxoPedestresHora());
        stmt.setDouble(i++, trecho.getNrIndiceAcessibilidade());
        stmt.setDouble(i++, trecho.getNrProbIntransitavel());
        stmt.setDate(i++, paraSqlDate(trecho.getDtUltimaVistoria()));
        return i;
    }

    private List<Trecho> consultar(String sql, Integer parametro) {
        List<Trecho> lista = new ArrayList<>();
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            if (parametro != null) {
                stmt.setInt(1, parametro);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao consultar trechos", e);
        }
        return lista;
    }

    private Trecho mapear(ResultSet rs) throws SQLException {
        Trecho trecho = new Trecho();
        trecho.setIdTrecho(rs.getInt("id_trecho"));
        trecho.setIdPontoOrigem(rs.getInt("id_ponto_origem"));
        trecho.setIdPontoDestino(rs.getInt("id_ponto_destino"));
        trecho.setNmRua(rs.getString("nm_rua"));
        trecho.setIdTipoVia(rs.getInt("id_tipo_via"));
        trecho.setNrComprimentoM(rs.getInt("nr_comprimento_m"));
        trecho.setNrLarguraTotalM(rs.getDouble("nr_largura_total_m"));
        trecho.setNrLarguraLivreM(rs.getDouble("nr_largura_livre_m"));
        trecho.setNrInclinacaoPct(rs.getDouble("nr_inclinacao_pct"));
        trecho.setNrBuracos(rs.getInt("nr_buracos"));
        trecho.setStTemDegrau(rs.getString("st_tem_degrau"));
        trecho.setNrObstaculos100m(rs.getInt("nr_obstaculos_100m"));
        trecho.setStOcupadoComercio(rs.getString("st_ocupado_comercio"));
        trecho.setStRampaEsquina(rs.getString("st_rampa_esquina"));
        trecho.setStPisoTatil(rs.getString("st_piso_tatil"));
        trecho.setNrIdadeCalcadaAnos(rs.getInt("nr_idade_calcada_anos"));
        trecho.setNrFluxoPedestresHora(rs.getInt("nr_fluxo_pedestres_hora"));
        trecho.setNrIndiceAcessibilidade(rs.getDouble("nr_indice_acessibilidade"));
        trecho.setNrProbIntransitavel(rs.getDouble("nr_prob_intransitavel"));
        trecho.setDtUltimaVistoria(paraLocalDate(rs.getDate("dt_ultima_vistoria")));
        return trecho;
    }

    private java.sql.Date paraSqlDate(LocalDate data) {
        return data == null ? null : java.sql.Date.valueOf(data);
    }

    private LocalDate paraLocalDate(java.sql.Date data) {
        return data == null ? null : data.toLocalDate();
    }
}
