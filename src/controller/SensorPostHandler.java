package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SensorPostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String linha;

        while ((linha = br.readLine()) != null) {
            sb.append(linha);
        }

        Gson gson = new Gson();
        try {
            SensorRequest novoSensor = gson.fromJson(sb.toString(), SensorRequest.class);

            String sql = "INSERT INTO sensores (localizacao, nivel_cm) VALUES (?, ?)";

            try (Connection conn = ConexaoOracle.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novoSensor.getLocalizacao());
                pstmt.setDouble(2, novoSensor.getNivel_cm());
                pstmt.executeUpdate();
            }

            String resposta = "{\"status\":\"model.Sensor inserido com sucesso!\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, resposta.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();

        } catch (JsonSyntaxException | SQLException e) {
            String erro = "{\"erro\":\"" + e.getMessage() + "\"}";
            exchange.sendResponseHeaders(400, erro.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(erro.getBytes());
            os.close();
        }
    }
}
