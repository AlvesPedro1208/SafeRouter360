package server;

import com.sun.net.httpserver.HttpServer;
import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServidorHttp {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/sensores", new SensorHandler()); // GET
        server.createContext("/alertas", new AlertaHandler()); // GET
        server.createContext("/abrigos", new AbrigoHandler()); // GET

        server.createContext("/sensores/post", new SensorPostHandler()); // POST

        server.createContext("/sensores/delete", new SensorDeleteHandler());
        server.createContext("/abrigos/delete", new AbrigoDeleteHandler());

        server.setExecutor(null); // executor padrão
        server.start();

        System.out.println("🚀 Servidor HTTP rodando em http://localhost:8000");
    }
}
