package model;

import dao.ConexaoOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Representa um abrigo de pessoas desalojadas na plataforma SafeRoute360.
 */
public class Abrigo {

    /* Atributos */
    private int idAbrigo;
    private String nome;
    private String endereco;
    private int capacidadeMaxima;
    private int ocupacaoAtual;
    private String telefone;

    /* Construtores */

    public Abrigo(int idAbrigo,
                  String nome,
                  String endereco,
                  int capacidadeMaxima,
                  int ocupacaoAtual,
                  String telefone) {
        this.idAbrigo = idAbrigo;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidadeMaxima = capacidadeMaxima;
        this.ocupacaoAtual = ocupacaoAtual;
        this.telefone = telefone;
    }

    /* Métodos */

    /** Retorna quantas vagas ainda restam. */
    public int vagasRestantes() {
        return capacidadeMaxima - ocupacaoAtual;
    }

    /** Indica se o abrigo está lotado. */
    public boolean estaCheio() {
        return ocupacaoAtual >= capacidadeMaxima;
    }

    /**
     * Registra a entrada de pessoas.
     * @param quantidade pessoas que chegaram
     * @return true se conseguiu acomodar, false se ultrapassaria a capacidade
     */
    public boolean registrarEntrada(int quantidade) {
        if (ocupacaoAtual + quantidade > capacidadeMaxima) {
            return false;
        }
        ocupacaoAtual += quantidade;
        return true;
    }

    /**
     * Registra a saída de pessoas.
     * Garante que a ocupação não fique negativa.
     */
    public String registrarSaida(int quantidade) {
        ocupacaoAtual = Math.max(0, ocupacaoAtual - quantidade);
        return "Ocupação atual, após a saída de " + quantidade + " pessoas do abrigo: "  + ocupacaoAtual;
    }

    /* =======================  Getters e Setters ======================= */
    public int getIdAbrigo()                       { return idAbrigo; }
    public String getNome()                  { return nome; }
    public String getEndereco()              { return endereco; }
    public int getCapacidadeMaxima()               { return capacidadeMaxima; }
    public int getOcupacaoAtual()                 { return ocupacaoAtual; }
    public String getTelefone()              { return telefone; }
}
