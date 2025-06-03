package dao;

import java.sql.*;
import java.util.Scanner;

public class AbrigoDAO {

    public static void criarTabelaAbrigos() {
        String sql = """
            CREATE TABLE abrigos (
                id          INTEGER PRIMARY KEY,
                nome        VARCHAR2(120)   NOT NULL,
                endereco    VARCHAR2(255)   NOT NULL,
                capacidade_maxima  NUMBER          NOT NULL,
                ocupacao_atual    NUMBER          DEFAULT 0,
                telefone    VARCHAR2(30),
            );          
        """;

        try (Connection conn = ConexaoOracle.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Tabela 'abrigos' criada com sucesso!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela: " + e.getMessage());
        }
    }

    private static final Scanner sc = new Scanner(System.in);

    /* ------------------------------------------------------------------
       CRUD BÁSICO + ATUALIZAÇÃO DE OCUPAÇÃO
       ------------------------------------------------------------------ */

    /** Cadastra um novo abrigo. */
    public static void inserirAbrigo() {
        System.out.println("\n🆕 Cadastro de Abrigo");

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Endereço: ");
        String endereco = sc.nextLine();

        System.out.print("Capacidade máxima: ");
        int capacidade = sc.nextInt();
        sc.nextLine(); // flush

        System.out.print("Telefone (opcional): ");
        String telefone = sc.nextLine();

        String sql = """
                INSERT INTO abrigos
                (nome, endereco, capacidade_maxima, ocupacao_atual, telefone)
                VALUES (?, ?, ?, 0, ?)
                """;

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setString(2, endereco);
            ps.setInt(3, capacidade);
            ps.setString(4, telefone.isBlank() ? null : telefone);

            ps.executeUpdate();
            System.out.println("✅ Abrigo cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao cadastrar abrigo: " + e.getMessage());
        }
    }

    /** Lista todos os abrigos com vagas restantes. */
    public static void listarAbrigos() {
        System.out.println("\n📋 Lista de Abrigos");

        String sql = """
                SELECT id_abrigo,
                       nome,
                       capacidade_maxima,
                       ocupacao_atual,
                       (capacidade_maxima - ocupacao_atual) AS vagas
                FROM abrigos
                ORDER BY id_abrigo
                """;

        try (Connection conn = ConexaoOracle.conectar();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            System.out.printf("%-4s %-25s %12s %12s %12s%n",
                    "ID", "Nome", "Capacidade Máxima", "Ocupação Atual", "Vagas");

            while (rs.next()) {
                System.out.printf("%-4d %-25s %12d %12d %12d%n",
                        rs.getInt("id_abrigo"),
                        rs.getString("nome"),
                        rs.getInt("capacidade_maxima"),
                        rs.getInt("ocupacao_atual"),
                        rs.getInt("vagas"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar abrigos: " + e.getMessage());
        }
    }

    /** Registra entrada de pessoas em um abrigo. */
    public static void registrarEntrada() {
        System.out.print("\nID do abrigo: ");
        int id = sc.nextInt();

        System.out.print("Quantidade que entrou: ");
        int qtd = sc.nextInt();
        sc.nextLine();

        if (qtd <= 0) {
            System.out.println("⚠️ Quantidade deve ser positiva.");
            return;
        }

        String select = """
                SELECT capacidade_maxima, ocupacao_atual
                FROM abrigos WHERE id_abrigo = ?
                """;
        String update = """
                UPDATE abrigos
                SET ocupacao_atual = ocupacao_atual + ?
                WHERE id_abrigo = ?
                """;

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement psSel = conn.prepareStatement(select);
             PreparedStatement psUp  = conn.prepareStatement(update)) {

            psSel.setInt(1, id);
            ResultSet rs = psSel.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Abrigo não encontrado.");
                return;
            }

            int capacidade = rs.getInt("capacidade_maxima");
            int ocupacao   = rs.getInt("ocupacao_atual");

            if (ocupacao + qtd > capacidade) {
                System.out.printf("🚫 Sem vagas suficientes (restam %d).%n",
                        capacidade - ocupacao);
                return;
            }

            psUp.setInt(1, qtd);
            psUp.setInt(2, id);
            psUp.executeUpdate();

            System.out.println("✅ Entrada registrada. Ocupação atual: "
                    + (ocupacao + qtd));

        } catch (SQLException e) {
            System.err.println("❌ Erro ao registrar entrada: " + e.getMessage());
        }
    }

    /** Registra saída de pessoas de um abrigo. */
    public static void registrarSaida() {
        System.out.print("\nID do abrigo...........: ");
        int id = sc.nextInt();

        System.out.print("Quantidade que saiu....: ");
        int qtd = sc.nextInt();
        sc.nextLine();

        if (qtd <= 0) {
            System.out.println("⚠️ Quantidade deve ser positiva.");
            return;
        }

        String update = """
                UPDATE abrigos
                SET ocupacao_atual = GREATEST(0, ocupacao_atual - ?)
                WHERE id_abrigo = ?
                """;

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setInt(1, qtd);
            ps.setInt(2, id);

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                System.out.println("❌ Abrigo não encontrado.");
            } else {
                System.out.println("✅ Saída registrada com sucesso.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao registrar saída: " + e.getMessage());
        }
    }
}
