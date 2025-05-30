package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        List<AlertaResponse> alertas = new ArrayList<>();

        try (Connection conn = ConexaoOracle.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM alertas")) {

            while (rs.next()) {
                AlertaResponse alerta = new AlertaResponse(
                        rs.getInt("id"),
                        rs.getInt("id_sensor"),
                        rs.getDouble("nivel_cm"),
                        rs.getString("mensagem"),
                        rs.getTimestamp("data_hora").toString()
                );
                alertas.add(alerta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(alertas);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
}
