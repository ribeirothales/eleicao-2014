package cortex.eleicoes.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document("estados")
public class Estado {

    @Id
    private String id; 
    private String sigla;
    private String nome;
    private List<Voto> votos;
    private Map<String, Object> geoJson;

    public Estado() {
    }

    public Estado(String id, String sigla, String nome, List<Voto> votos, Map<String, Object> geoJson) {
        this.id = id;
        this.sigla = sigla;
        this.nome = nome;
        this.votos = votos;
        this.geoJson = geoJson;
    }

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
