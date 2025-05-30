package model;

import dao.ConexaoOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Alerta {
    private int idSensor;
    private double nivelAtual;
    private String mensagem;
    private static final double LIMITE_CRITICO = 100.0;

    public Alerta(int idSensor, double nivelAtual) {
        this.idSensor = idSensor;
        this.nivelAtual = nivelAtual;
        this.mensagem = verificarCondicao();
    }

    private String verificarCondicao() {
        if (nivelAtual > LIMITE_CRITICO) {
            return "⚠️ ALERTA: model.Sensor " + idSensor + " ultrapassou o nível crítico! (" + nivelAtual + " cm)";
        }
        return null;
    }

    public boolean deveDisparar() {
        return mensagem != null;
    }

    public void disparar() {
        if (mensagem != null) {
            System.out.println(mensagem);
            salvarNoBanco();
        }
    }

    private void salvarNoBanco() {
        String sql = "INSERT INTO alertas (id_sensor, nivel_cm, mensagem) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoOracle.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSensor);
            pstmt.setDouble(2, nivelAtual);
            pstmt.setString(3, mensagem);
            pstmt.executeUpdate();

            System.out.println("💾 model.Alerta salvo no banco!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao salvar alerta: " + e.getMessage());
        }
    }

    // Getters
    public int getIdSensor() {
        return idSensor;
    }

    public double getNivelAtual() {
        return nivelAtual;
    }

    public String getMensagem() {
        return mensagem;
    }
}
