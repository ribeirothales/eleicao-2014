package cortex.eleicoes.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "votacoes")
public class Voto {
    private String partido;
    private String candidato;
    private int quantidade;
    private double porcentagem;
    private boolean eleito;

    public Voto() {}

    public Voto(String partido, String candidato, int quantidade, double porcentagem, boolean eleito) {
        this.partido = partido;
        this.candidato = candidato;
        this.quantidade = quantidade;
        this.porcentagem = porcentagem;
        this.eleito = eleito;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getCandidato() {
        return candidato;
    }

    public void setCandidato(String candidato) {
        this.candidato = candidato;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }

    public boolean isEleito() {
        return eleito;
    }

    public void setEleito(boolean eleito) {
        this.eleito = eleito;
    }
}
