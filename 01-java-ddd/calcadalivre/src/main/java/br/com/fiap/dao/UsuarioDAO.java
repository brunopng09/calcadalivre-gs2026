package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Usuario;
import br.com.fiap.excecoes.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso JDBC puro a tabela CL_USUARIO.
 */
public class UsuarioDAO {

    private static final String COLUNAS =
            "id_usuario, nm_usuario, nm_email, id_perfil_preferido, st_ativo, dt_cadastro";

    public List<Usuario> listar() {
        return consultar("SELECT " + COLUNAS + " FROM CL_USUARIO ORDER BY id_usuario", null);
    }

    public Usuario buscarPorId(int id) {
        List<Usuario> lista = consultar("SELECT " + COLUNAS + " FROM CL_USUARIO WHERE id_usuario = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Usuario buscarPorEmail(String email) {
        List<Usuario> lista = consultar("SELECT " + COLUNAS + " FROM CL_USUARIO WHERE UPPER(nm_email) = UPPER(?)", null);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO CL_USUARIO (nm_usuario, nm_email, id_perfil_preferido, st_ativo, dt_cadastro) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_USUARIO" })) {
            stmt.setString(1, usuario.getNmUsuario());
            stmt.setString(2, usuario.getNmEmail());
            if (usuario.getIdPerfilPreferido() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, usuario.getIdPerfilPreferido());
            }
            stmt.setString(4, usuario.getStAtivo());
            stmt.setDate(5, paraSqlDate(usuario.getDtCadastro()));
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao inserir usuário", e);
        }
        return 0;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE CL_USUARIO SET nm_usuario = ?, nm_email = ?, id_perfil_preferido = ?, "
                + "st_ativo = ?, dt_cadastro = ? WHERE id_usuario = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNmUsuario());
            stmt.setString(2, usuario.getNmEmail());
            if (usuario.getIdPerfilPreferido() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, usuario.getIdPerfilPreferido());
            }
            stmt.setString(4, usuario.getStAtivo());
            stmt.setDate(5, paraSqlDate(usuario.getDtCadastro()));
            stmt.setInt(6, usuario.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar usuário", e);
        }
    }

    public void deletar(int id) {
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM CL_USUARIO WHERE id_usuario = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao excluir usuário", e);
        }
    }

    private List<Usuario> consultar(String sql, Integer parametro) {
        List<Usuario> lista = new ArrayList<>();
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
            throw new PersistenciaException("Erro ao consultar usuários", e);
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNmUsuario(rs.getString("nm_usuario"));
        usuario.setNmEmail(rs.getString("nm_email"));
        int idPerfil = rs.getInt("id_perfil_preferido");
        usuario.setIdPerfilPreferido(rs.wasNull() ? null : idPerfil);
        usuario.setStAtivo(rs.getString("st_ativo"));
        usuario.setDtCadastro(paraLocalDate(rs.getDate("dt_cadastro")));
        return usuario;
    }

    private java.sql.Date paraSqlDate(LocalDate data) {
        return data == null ? null : java.sql.Date.valueOf(data);
    }

    private LocalDate paraLocalDate(java.sql.Date data) {
        return data == null ? null : data.toLocalDate();
    }
}
