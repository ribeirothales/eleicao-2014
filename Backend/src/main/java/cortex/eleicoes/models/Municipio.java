package cortex.eleicoes.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document("municipios")
public class Municipio {

    @Id
    private String id; 
    private String nome;
    private String estadoSigla;
    private String estadoCodigo;
    private List<Voto> votos;
    private Map<String, Object> geoJson;

    public Municipio() {
    }

    public Municipio(String id, String nome, String estadoSigla, String estadoCodigo, List<Voto> votos, Map<String, Object> geoJson) {
        this.id = id;
        this.nome = nome;
        this.estadoSigla = estadoSigla;
        this.estadoCodigo = estadoCodigo;
        this.votos = votos;
        this.geoJson = geoJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstadoSigla() {
        return estadoSigla;
    }

    public void setEstadoSigla(String estadoSigla) {
        this.estadoSigla = estadoSigla;
    }

    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(String estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
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