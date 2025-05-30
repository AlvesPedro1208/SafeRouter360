package model;

public class AlertaResponse {
    private int id;
    private int id_sensor;
    private double nivel_cm;
    private String mensagem;
    private String data_hora;

    public AlertaResponse(int id, int id_sensor, double nivel_cm, String mensagem, String data_hora) {
        this.id = id;
        this.id_sensor = id_sensor;
        this.nivel_cm = nivel_cm;
        this.mensagem = mensagem;
        this.data_hora = data_hora;
    }

    // Getters (se quiser expor no JSON com GSON de forma explícita)
}
