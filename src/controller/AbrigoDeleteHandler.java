package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.AbrigoDAO;

import java.io.IOException;
import java.io.OutputStream;

public class AbrigoDeleteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String erro = "Parâmetro 'id' não fornecido.";
            exchange.sendResponseHeaders(400, erro.getBytes().length);
            exchange.getResponseBody().write(erro.getBytes());
            exchange.close();
            return;
        }

        int id = Integer.parseInt(query.split("=")[1]);

        boolean sucesso = AbrigoDAO.excluirAbrigo(id);
        String resposta = sucesso
                ? "✅ Abrigo excluído com sucesso!"
                : "❌ Abrigo não encontrado ou erro ao excluir.";

        exchange.sendResponseHeaders(sucesso ? 200 : 404, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
