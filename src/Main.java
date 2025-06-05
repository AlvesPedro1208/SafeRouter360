import dao.AbrigoDAO;
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
            int categoria;

            do {
                System.out.println("\n🔘 Escolha uma categoria:");
                System.out.println("1 - Sensores");
                System.out.println("2 - Abrigos");
                System.out.println("3 - Alertas");
                System.out.println("4 - Banco de Dados");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");
                categoria = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (categoria) {
                    case 1 -> menuSensores(scanner);
                    case 2 -> menuAbrigos(scanner);
                    case 3 -> menuAlertas(scanner);
                    case 4 -> menuBancoDeDados(scanner);
                    case 0 -> System.out.println("👋 Encerrando...");
                    default -> System.out.println("⚠️ Opção inválida.");
                }
            } while (categoria != 0);

        } else {
            System.out.println("❌ Falha na conexão.");
        }
    }

    private static void menuSensores(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n📦 Menu: Sensores");
            System.out.println("1 - Cadastrar novo sensor");
            System.out.println("2 - Listar sensores");
            System.out.println("3 - Simular aumento de nível");
            System.out.println("4 - Excluir sensor");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> SensorDAO.inserirSensor();
                case 2 -> SensorDAO.listarSensores();
                case 3 -> SensorDAO.simularAumentoNivel();
                case 4 -> SensorDAO.excluirSensorViaTerminal();
                case 0 -> System.out.println("↩️ Voltando ao menu principal...");
                default -> System.out.println("⚠️ Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuAbrigos(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n🏠 Menu: Abrigos");
            System.out.println("1 - Cadastrar novo abrigo");
            System.out.println("2 - Listar abrigos");
            System.out.println("3 - Registrar entrada de pessoas");
            System.out.println("4 - Registrar saída de pessoas");
            System.out.println("5 - Excluir abrigo");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> AbrigoDAO.inserirAbrigo();
                case 2 -> AbrigoDAO.listarAbrigos();
                case 3 -> AbrigoDAO.registrarEntrada();
                case 4 -> AbrigoDAO.registrarSaida();
                case 5 -> AbrigoDAO.excluirAbrigoViaTerminal();
                case 0 -> System.out.println("↩️ Voltando ao menu principal...");
                default -> System.out.println("⚠️ Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuAlertas(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n🚨 Menu: Alertas");
            System.out.println("1 - Listar alertas registrados");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> SensorDAO.listarAlertas();
                case 0 -> System.out.println("↩️ Voltando ao menu principal...");
                default -> System.out.println("⚠️ Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuBancoDeDados(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n🛠️ Menu: Banco de Dados");
            System.out.println("1 - Listar tabelas existentes");
            System.out.println("2 - Criar todas as tabelas");
            System.out.println("3 - Deletar todas as tabelas");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> ConexaoOracle.listarTabelas();
                case 2 -> ConexaoOracle.criarTabelas();
                case 3 -> ConexaoOracle.deletarTabelas();
                case 0 -> System.out.println("↩️ Voltando ao menu principal...");
                default -> System.out.println("⚠️ Opção inválida.");
            }
        } while (opcao != 0);
    }
}
