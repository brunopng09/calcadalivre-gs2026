package br.com.fiap.conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes JDBC com o Oracle da FIAP.
 * As credenciais vem das variaveis de ambiente DB_USUARIO e DB_SENHA.
 * A variavel DB_URL e opcional e sobrescreve a URL padrao.
 */
public class ConexaoFactory {

    private static final String URL_PADRAO = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    public Connection conexao() throws ClassNotFoundException, SQLException {
        String usuario = System.getenv("DB_USUARIO");
        String senha = System.getenv("DB_SENHA");

        if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalStateException(
                    "Configure as variáveis de ambiente DB_USUARIO e DB_SENHA antes de rodar");
        }

        String url = System.getenv("DB_URL");
        if (url == null || url.isBlank()) {
            url = URL_PADRAO;
        }

        // Driver
        Class.forName("oracle.jdbc.driver.OracleDriver");
        // Retorna a conexao
        return DriverManager.getConnection(url, usuario, senha);
    }
}
