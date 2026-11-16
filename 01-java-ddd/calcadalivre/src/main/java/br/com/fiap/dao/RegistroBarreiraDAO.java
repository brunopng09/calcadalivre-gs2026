package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.RegistroBarreira;
import br.com.fiap.excecoes.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acesso JDBC puro a tabela CL_REGISTRO_BARREIRA.
 */
public class RegistroBarreiraDAO {

    private static final String COLUNAS = "id_registro, id_trecho, id_usuario, id_tipo_barreira, nr_gravidade, "
            + "ds_registro, nr_latitude, nr_longitude, url_foto, dt_registro, st_status";

    public List<RegistroBarreira> listar() {
        return consultar("SELECT " + COLUNAS + " FROM CL_REGISTRO_BARREIRA ORDER BY dt_registro DESC", null);
    }

    public RegistroBarreira buscarPorId(int id) {
        List<RegistroBarreira> lista = consultar(
                "SELECT " + COLUNAS + " FROM CL_REGISTRO_BARREIRA WHERE id_registro = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<RegistroBarreira> listarPorTrecho(int idTrecho) {
        return consultar("SELECT " + COLUNAS + " FROM CL_REGISTRO_BARREIRA WHERE id_trecho = ? "
                + "ORDER BY dt_registro DESC", idTrecho);
    }

    /**
     * Quantidade de registros de barreira agrupada por trecho.
     */
    public Map<Integer, Integer> contarPorTrecho() {
        Map<Integer, Integer> contagem = new LinkedHashMap<>();
        String sql = "SELECT id_trecho, COUNT(*) AS qtd FROM CL_REGISTRO_BARREIRA GROUP BY id_trecho";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contagem.put(rs.getInt("id_trecho"), rs.getInt("qtd"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao contar registros de barreira", e);
        }
        return contagem;
    }

    public int inserir(RegistroBarreira registro) {
        String sql = "INSERT INTO CL_REGISTRO_BARREIRA (id_trecho, id_usuario, id_tipo_barreira, nr_gravidade, "
                + "ds_registro, nr_latitude, nr_longitude, url_foto, dt_registro, st_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_REGISTRO" })) {
            stmt.setInt(1, registro.getIdTrecho());
            stmt.setInt(2, registro.getIdUsuario());
            stmt.setInt(3, registro.getIdTipoBarreira());
            stmt.setInt(4, registro.getNrGravidade());
            stmt.setString(5, registro.getDsRegistro());
            if (registro.getNrLatitude() == null) {
                stmt.setNull(6, Types.DOUBLE);
            } else {
                stmt.setDouble(6, registro.getNrLatitude());
            }
            if (registro.getNrLongitude() == null) {
                stmt.setNull(7, Types.DOUBLE);
            } else {
                stmt.setDouble(7, registro.getNrLongitude());
            }
            stmt.setString(8, registro.getUrlFoto());
            stmt.setTimestamp(9, paraTimestamp(registro.getDtRegistro()));
            stmt.setString(10, registro.getStStatus());
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao inserir registro de barreira", e);
        }
        return 0;
    }

    public void atualizar(RegistroBarreira registro) {
        String sql = "UPDATE CL_REGISTRO_BARREIRA SET id_trecho = ?, id_usuario = ?, id_tipo_barreira = ?, "
                + "nr_gravidade = ?, ds_registro = ?, nr_latitude = ?, nr_longitude = ?, url_foto = ?, "
                + "dt_registro = ?, st_status = ? WHERE id_registro = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, registro.getIdTrecho());
            stmt.setInt(2, registro.getIdUsuario());
            stmt.setInt(3, registro.getIdTipoBarreira());
            stmt.setInt(4, registro.getNrGravidade());
            stmt.setString(5, registro.getDsRegistro());
            if (registro.getNrLatitude() == null) {
                stmt.setNull(6, Types.DOUBLE);
            } else {
                stmt.setDouble(6, registro.getNrLatitude());
            }
            if (registro.getNrLongitude() == null) {
                stmt.setNull(7, Types.DOUBLE);
            } else {
                stmt.setDouble(7, registro.getNrLongitude());
            }
            stmt.setString(8, registro.getUrlFoto());
            stmt.setTimestamp(9, paraTimestamp(registro.getDtRegistro()));
            stmt.setString(10, registro.getStStatus());
            stmt.setInt(11, registro.getIdRegistro());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar registro de barreira", e);
        }
    }

    public void deletar(int id) {
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(
                     "DELETE FROM CL_REGISTRO_BARREIRA WHERE id_registro = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao excluir registro de barreira", e);
        }
    }

    private List<RegistroBarreira> consultar(String sql, Integer parametro) {
        List<RegistroBarreira> lista = new ArrayList<>();
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
            throw new PersistenciaException("Erro ao consultar registros de barreira", e);
        }
        return lista;
    }

    private RegistroBarreira mapear(ResultSet rs) throws SQLException {
        RegistroBarreira registro = new RegistroBarreira();
        registro.setIdRegistro(rs.getInt("id_registro"));
        registro.setIdTrecho(rs.getInt("id_trecho"));
        registro.setIdUsuario(rs.getInt("id_usuario"));
        registro.setIdTipoBarreira(rs.getInt("id_tipo_barreira"));
        registro.setNrGravidade(rs.getInt("nr_gravidade"));
        registro.setDsRegistro(rs.getString("ds_registro"));
        double latitude = rs.getDouble("nr_latitude");
        registro.setNrLatitude(rs.wasNull() ? null : latitude);
        double longitude = rs.getDouble("nr_longitude");
        registro.setNrLongitude(rs.wasNull() ? null : longitude);
        registro.setUrlFoto(rs.getString("url_foto"));
        registro.setDtRegistro(paraLocalDateTime(rs.getTimestamp("dt_registro")));
        registro.setStStatus(rs.getString("st_status"));
        return registro;
    }

    private Timestamp paraTimestamp(LocalDateTime data) {
        return data == null ? null : Timestamp.valueOf(data);
    }

    private LocalDateTime paraLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
