package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ConexaoOracle;
import model.AbrigoResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AbrigoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        List<AbrigoResponse> abrigos = new ArrayList<>();

        String sql =
                "SELECT id, nome, endereco, capacidade_maxima, ocupacao_atual, telefone " +
                        "FROM abrigos";

        try (Connection conn = ConexaoOracle.conectar();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AbrigoResponse abrigo = new AbrigoResponse(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getInt("capacidade_maxima"),
                        rs.getInt("ocupacao_atual"),
                        rs.getString("telefone")
                );
                abrigos.add(abrigo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(abrigos);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}
