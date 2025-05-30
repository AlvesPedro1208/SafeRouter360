package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoOracle {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; // banco local, SID padrão XE
    private static final String USUARIO = "system";
    private static final String SENHA = "root";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("✅ Conexão bem-sucedida com OracleDB!");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}
