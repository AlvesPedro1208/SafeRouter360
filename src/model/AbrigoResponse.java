package model;

/**
 * DTO de leitura (response) para a entidade Abrigo.
 */
public class AbrigoResponse {

    private int id;
    private String nome;
    private String endereco;
    private int capacidadeMaxima;
    private int ocupacaoAtual;
    private int vagasRestantes;
    private String telefone;

    public AbrigoResponse(int id,
                          String nome,
                          String endereco,
                          int capacidadeMaxima,
                          int ocupacaoAtual,
                          String telefone) {

        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidadeMaxima = capacidadeMaxima;
        this.ocupacaoAtual = ocupacaoAtual;
        this.vagasRestantes = capacidadeMaxima - ocupacaoAtual;
        this.telefone = telefone;
    }

    /** Construtor de conveniência: cria o DTO a partir do objeto de domínio. */
    public AbrigoResponse(Abrigo abrigo) {
        this(
                abrigo.getIdAbrigo(),
                abrigo.getNome(),
                abrigo.getEndereco(),
                abrigo.getCapacidadeMaxima(),
                abrigo.getOcupacaoAtual(),
                abrigo.getTelefone()
        );
    }

    /* Getters */

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getOcupacaoAtual() {
        return ocupacaoAtual;
    }

    public int getVagasRestantes() {
        return vagasRestantes;
    }

    public String getTelefone() {
        return telefone;
    }
}
