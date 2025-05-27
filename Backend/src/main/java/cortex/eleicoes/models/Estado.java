package cortex.eleicoes.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Map;

@Document(collection = "estados") // Assumindo nome da coleção
public class Estado {

    @Id
    private String id; // Pode ser o ID do IBGE

    @Indexed(unique = true) // Índice único para busca rápida por sigla
    private String sigla;

    private String nome;
    private List<Voto> votos;
    private Map<String, Object> geoJson;

    // Construtores, Getters e Setters (Omitidos para brevidade)
    // Necessário adicionar construtores, getters e setters para o código compilar

    public Estado() {}

    // Exemplo de construtor (adapte conforme necessidade)
    public Estado(String id, String sigla, String nome, List<Voto> votos, Map<String, Object> geoJson) {
        this.id = id;
        this.sigla = sigla;
        this.nome = nome;
        this.votos = votos;
        this.geoJson = geoJson;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Voto> getVotos() {
        return votos;
    }

    public void setVotos(List<Voto> votos) {
        this.votos = votos;
    }

    public Map<String, Object> getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(Map<String, Object> geoJson) {
        this.geoJson = geoJson;
    }
}

