package model;

public class Sensor {
    private int id;
    private String localizacao;
    private double nivel_cm;
    private String data_hora;

    public Sensor(int id, String localizacao, double nivel_cm, String data_hora) {
        this.id = id;
        this.localizacao = localizacao;
        this.nivel_cm = nivel_cm;
        this.data_hora = data_hora;
    }
}