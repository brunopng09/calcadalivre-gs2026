package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.AvaliacaoTrecho;
import br.com.fiap.excecoes.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso JDBC puro a tabela CL_AVALIACAO_TRECHO.
 */
public class AvaliacaoTrechoDAO {

    private static final String COLUNAS = "id_avaliacao, id_trecho, id_usuario, id_perfil, nr_indice_avaliado, "
            + "st_transitavel, ds_observacao, dt_avaliacao";

    public List<AvaliacaoTrecho> listar() {
        return consultar("SELECT " + COLUNAS + " FROM CL_AVALIACAO_TRECHO ORDER BY dt_avaliacao DESC", null);
    }

    public AvaliacaoTrecho buscarPorId(int id) {
        List<AvaliacaoTrecho> lista = consultar(
                "SELECT " + COLUNAS + " FROM CL_AVALIACAO_TRECHO WHERE id_avaliacao = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<AvaliacaoTrecho> listarPorTrecho(int idTrecho) {
        return consultar("SELECT " + COLUNAS + " FROM CL_AVALIACAO_TRECHO WHERE id_trecho = ? "
                + "ORDER BY dt_avaliacao DESC", idTrecho);
    }

    public int inserir(AvaliacaoTrecho avaliacao) {
        String sql = "INSERT INTO CL_AVALIACAO_TRECHO (id_trecho, id_usuario, id_perfil, nr_indice_avaliado, "
                + "st_transitavel, ds_observacao, dt_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_AVALIACAO" })) {
            stmt.setInt(1, avaliacao.getIdTrecho());
            stmt.setInt(2, avaliacao.getIdUsuario());
            stmt.setInt(3, avaliacao.getIdPerfil());
            stmt.setDouble(4, avaliacao.getNrIndiceAvaliado());
            stmt.setString(5, avaliacao.getStTransitavel());
            stmt.setString(6, avaliacao.getDsObservacao());
            stmt.setTimestamp(7, paraTimestamp(avaliacao.getDtAvaliacao()));
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao inserir avaliação de trecho", e);
        }
        return 0;
    }

    public void atualizar(AvaliacaoTrecho avaliacao) {
        String sql = "UPDATE CL_AVALIACAO_TRECHO SET id_trecho = ?, id_usuario = ?, id_perfil = ?, "
                + "nr_indice_avaliado = ?, st_transitavel = ?, ds_observacao = ?, dt_avaliacao = ? "
                + "WHERE id_avaliacao = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, avaliacao.getIdTrecho());
            stmt.setInt(2, avaliacao.getIdUsuario());
            stmt.setInt(3, avaliacao.getIdPerfil());
            stmt.setDouble(4, avaliacao.getNrIndiceAvaliado());
            stmt.setString(5, avaliacao.getStTransitavel());
            stmt.setString(6, avaliacao.getDsObservacao());
            stmt.setTimestamp(7, paraTimestamp(avaliacao.getDtAvaliacao()));
            stmt.setInt(8, avaliacao.getIdAvaliacao());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar avaliação de trecho", e);
        }
    }

    public void deletar(int id) {
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(
                     "DELETE FROM CL_AVALIACAO_TRECHO WHERE id_avaliacao = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao excluir avaliação de trecho", e);
        }
    }

    private List<AvaliacaoTrecho> consultar(String sql, Integer parametro) {
        List<AvaliacaoTrecho> lista = new ArrayList<>();
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
            throw new PersistenciaException("Erro ao consultar avaliações de trecho", e);
        }
        return lista;
    }

    private AvaliacaoTrecho mapear(ResultSet rs) throws SQLException {
        AvaliacaoTrecho avaliacao = new AvaliacaoTrecho();
        avaliacao.setIdAvaliacao(rs.getInt("id_avaliacao"));
        avaliacao.setIdTrecho(rs.getInt("id_trecho"));
        avaliacao.setIdUsuario(rs.getInt("id_usuario"));
        avaliacao.setIdPerfil(rs.getInt("id_perfil"));
        avaliacao.setNrIndiceAvaliado(rs.getDouble("nr_indice_avaliado"));
        avaliacao.setStTransitavel(rs.getString("st_transitavel"));
        avaliacao.setDsObservacao(rs.getString("ds_observacao"));
        avaliacao.setDtAvaliacao(paraLocalDateTime(rs.getTimestamp("dt_avaliacao")));
        return avaliacao;
    }

    private Timestamp paraTimestamp(LocalDateTime data) {
        return data == null ? null : Timestamp.valueOf(data);
    }

    private LocalDateTime paraLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
