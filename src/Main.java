import dao.ConexaoOracle;
import dao.SensorDAO;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("📡 SafeRoute360 - Backend Inicial\n");

        Connection conn = ConexaoOracle.conectar();
        if (conn != null) {
            System.out.println("✅ Conexão bem-sucedida com OracleDB!\n");

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                System.out.println("\n🔘 Escolha uma opção:");
                System.out.println("1 - Cadastrar novo sensor");
                System.out.println("2 - Listar sensores");
                System.out.println("3 - Simular aumento de nível");
                System.out.println("4 - Listar alertas registrados");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // limpar o buffer

                switch (opcao) {
                    case 1 -> SensorDAO.inserirSensor();
                    case 2 -> SensorDAO.listarSensores();
                    case 3 -> SensorDAO.simularAumentoNivel();
                    case 4 -> SensorDAO.listarAlertas();
                    case 0 -> System.out.println("👋 Encerrando...");
                    default -> System.out.println("⚠️ Opção inválida.");
                }

            } while (opcao != 0);

        } else {
            System.out.println("❌ Falha na conexão.");
        }
    }
}
