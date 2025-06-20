package dao;

import model.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.ResultSet;

public class SensorDAO {

//    public static void criarTabelaSensores() {
//        String sql = """
//            CREATE TABLE sensores (
//                id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
//                localizacao VARCHAR2(100),
//                nivel_cm NUMBER(5,2),
//                data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
//            )
//        """;
//
//        try (Connection conn = ConexaoOracle.conectar();
//             Statement stmt = conn.createStatement()) {
//
//            stmt.execute(sql);
//            System.out.println("✅ Tabela 'sensores' criada com sucesso!");
//
//        } catch (SQLException e) {
//            System.err.println("❌ Erro ao criar tabela: " + e.getMessage());
//        }
//    }

    public static void inserirSensor() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe a localização do sensor: ");
        String localizacao = scanner.nextLine();

        System.out.print("Informe o nível inicial (cm): ");
        double nivel = scanner.nextDouble();

        String sql = "INSERT INTO sensores (localizacao, nivel_cm) VALUES (?, ?)";

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, localizacao);
            pstmt.setDouble(2, nivel);
            pstmt.executeUpdate();

            System.out.println("✅ model.Sensor inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir sensor: " + e.getMessage());
        }
    }

    public static void listarSensores() {
        String sql = "SELECT * FROM sensores";

        try (Connection conn = ConexaoOracle.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📋 Lista de Sensores Cadastrados:");
            System.out.println("ID | Localização            | Nível (cm) | Data/Hora");
            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String local = rs.getString("localizacao");
                double nivel = rs.getDouble("nivel_cm");
                String data = rs.getString("data_hora");

                System.out.printf("%-3d| %-23s| %-11.2f| %s\n", id, local, nivel, data);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar sensores: " + e.getMessage());
        }
    }

    public static void simularAumentoNivel() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("\nDigite o ID do sensor que deseja simular: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            System.out.print("Digite quanto deseja aumentar o nível (cm): ");
            double aumento = scanner.nextDouble();
            scanner.nextLine(); // limpar buffer

            String sqlAtualizar = "UPDATE sensores SET nivel_cm = nivel_cm + ? WHERE id = ?";

            try (Connection conn = ConexaoOracle.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sqlAtualizar)) {

                pstmt.setDouble(1, aumento);
                pstmt.setInt(2, id);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("✅ Nível atualizado com sucesso!");

                    // Verifica o novo nível e dispara alerta se necessário
                    String sqlVerificacao = "SELECT nivel_cm FROM sensores WHERE id = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(sqlVerificacao)) {
                        checkStmt.setInt(1, id);
                        ResultSet rs = checkStmt.executeQuery();

                        if (rs.next()) {
                            double nivelAtual = rs.getDouble("nivel_cm");
                            Alerta alerta = new Alerta(id, nivelAtual);

                            if (alerta.deveDisparar()) {
                                alerta.disparar();
                            }
                        }
                    }

                } else {
                    System.out.println("⚠️ Nenhum sensor encontrado com esse ID.");
                }

            } catch (SQLException e) {
                System.err.println("❌ Erro ao atualizar o nível: " + e.getMessage());
            }

        } catch (InputMismatchException e) {
            System.err.println("❌ Entrada inválida. Use números corretos.");
        }
    }

    public static void listarAlertas() {
        String sql = "SELECT * FROM alertas ORDER BY data_hora DESC";

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n🚨 Lista de Alertas Registrados:");
            System.out.println("ID | model.Sensor | Nível (cm) | Mensagem                              | Data/Hora");
            System.out.println("-------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                int idSensor = rs.getInt("id_sensor");
                double nivel = rs.getDouble("nivel_cm");
                String mensagem = rs.getString("mensagem");
                String dataHora = rs.getString("data_hora");

                System.out.printf("%-3d| %-7d| %-11.2f| %-36s| %s\n",
                        id, idSensor, nivel, mensagem, dataHora);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar alertas: " + e.getMessage());
        }
    }

//    public static void criarTabelaAlertas() {
//        String sql = """
//        CREATE TABLE alertas (
//            id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
//            id_sensor NUMBER NOT NULL,
//            nivel_cm NUMBER(5,2),
//            mensagem VARCHAR2(255),
//            data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//            FOREIGN KEY (id_sensor) REFERENCES sensores(id)
//        )
//    """;
//
//        try (Connection conn = ConexaoOracle.conectar();
//             Statement stmt = conn.createStatement()) {
//
//            stmt.execute(sql);
//            System.out.println("✅ Tabela 'alertas' criada com sucesso!");
//
//        } catch (SQLException e) {
//            System.err.println("❌ Erro ao criar tabela de alertas: " + e.getMessage());
//        }
//    }

    public static boolean excluirSensor(int id) {
        String sql = "DELETE FROM sensores WHERE id = ?";

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir sensor: " + e.getMessage());
            return false;
        }
    }
    public static void excluirSensorViaTerminal() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do sensor que deseja excluir: ");
        int id = scanner.nextInt();
        boolean sucesso = excluirSensor(id);
        if (sucesso) {
            System.out.println("✅ Sensor excluído com sucesso!");
        } else {
            System.out.println("❌ Sensor não encontrado.");
        }
    }


}
