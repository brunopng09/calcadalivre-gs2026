package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.TipoBarreira;
import br.com.fiap.excecoes.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso JDBC puro a tabela CL_TIPO_BARREIRA.
 */
public class TipoBarreiraDAO {

    private static final String COLUNAS =
            "id_tipo_barreira, ds_tipo_barreira, nr_gravidade_base, st_ativo";

    public List<TipoBarreira> listar() {
        List<TipoBarreira> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM CL_TIPO_BARREIRA ORDER BY ds_tipo_barreira";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar tipos de barreira", e);
        }
        return lista;
    }

    public TipoBarreira buscarPorId(int id) {
        List<TipoBarreira> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM CL_TIPO_BARREIRA WHERE id_tipo_barreira = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar tipo de barreira", e);
        }
        return lista.isEmpty() ? null : lista.get(0);
    }

    public int inserir(TipoBarreira tipo) {
        String sql = "INSERT INTO CL_TIPO_BARREIRA (ds_tipo_barreira, nr_gravidade_base, st_ativo) "
                + "VALUES (?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_TIPO_BARREIRA" })) {
            stmt.setString(1, tipo.getDsTipoBarreira());
            stmt.setInt(2, tipo.getNrGravidadeBase());
            stmt.setString(3, tipo.getStAtivo());
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao inserir tipo de barreira", e);
        }
        return 0;
    }

    public void atualizar(TipoBarreira tipo) {
        String sql = "UPDATE CL_TIPO_BARREIRA SET ds_tipo_barreira = ?, nr_gravidade_base = ?, st_ativo = ? "
                + "WHERE id_tipo_barreira = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tipo.getDsTipoBarreira());
            stmt.setInt(2, tipo.getNrGravidadeBase());
            stmt.setString(3, tipo.getStAtivo());
            stmt.setInt(4, tipo.getIdTipoBarreira());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar tipo de barreira", e);
        }
    }

    public void deletar(int id) {
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(
                     "DELETE FROM CL_TIPO_BARREIRA WHERE id_tipo_barreira = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao excluir tipo de barreira", e);
        }
    }

    private TipoBarreira mapear(ResultSet rs) throws SQLException {
        TipoBarreira tipo = new TipoBarreira();
        tipo.setIdTipoBarreira(rs.getInt("id_tipo_barreira"));
        tipo.setDsTipoBarreira(rs.getString("ds_tipo_barreira"));
        tipo.setNrGravidadeBase(rs.getInt("nr_gravidade_base"));
        tipo.setStAtivo(rs.getString("st_ativo"));
        return tipo;
    }
}
