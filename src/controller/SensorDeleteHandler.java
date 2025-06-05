package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.SensorDAO;

import java.io.IOException;
import java.io.OutputStream;

public class SensorDeleteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String query = exchange.getRequestURI().getQuery();
        int id = Integer.parseInt(query.split("=")[1]);

        boolean sucesso = SensorDAO.excluirSensor(id);
        String resposta = sucesso ? "Sensor excluído com sucesso!" : "Sensor não encontrado ou erro ao excluir.";

        exchange.sendResponseHeaders(sucesso ? 200 : 404, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
