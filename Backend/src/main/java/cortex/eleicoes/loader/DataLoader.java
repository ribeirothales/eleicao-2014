package cortex.eleicoes.loader;

import cortex.eleicoes.dto.EstadoDTO;
import cortex.eleicoes.models.Estado;
import cortex.eleicoes.models.Municipio;
import cortex.eleicoes.models.Voto;
import cortex.eleicoes.repositories.EstadoRepository;
import cortex.eleicoes.repositories.MunicipioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    private Map<String, EstadoDTO> estadosPorSigla = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        carregarEstadosDoIBGE();

        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/dados/resultado.json");

        if (inputStream == null) {
            throw new IllegalArgumentException("❌ Arquivo 'resultado.json' não encontrado em /resources/dados");
        }

        List<Object> dados = objectMapper.readValue(inputStream, List.class);
        int totalEstados = 0;
        int totalMunicipios = 0;

        for (Object item : dados) {
            if (!(item instanceof List<?> linha)) continue;

            if (linha.size() > 3 && "UF".equals(linha.get(0))) {
                String sigla = (String) linha.get(1);
                String nome = (String) linha.get(2);

                List<Voto> votos = new ArrayList<>();
                for (int i = 4; i < linha.size(); i++) {
                    List<?> votoBruto = (List<?>) linha.get(i);
                    votos.add(new Voto(
                            (String) votoBruto.get(0),
                            (String) votoBruto.get(1),
                            ((Number) votoBruto.get(2)).intValue(),
                            ((Number) votoBruto.get(3)).doubleValue(),
                            "S".equals(votoBruto.get(4))
                    ));
                }

                EstadoDTO estadoDTO = estadosPorSigla.get(sigla.toUpperCase());

                if (estadoDTO == null) {
                    System.err.println("Estado com sigla '" + sigla + "' não encontrado na API do IBGE. Pulando...");
                    continue;
                }

                Estado estado = estadoRepository.findById(String.valueOf(estadoDTO.getId())).orElse(new Estado());
                estado.setId(String.valueOf(estadoDTO.getId()));
                estado.setSigla(sigla);
                estado.setNome(nome);
                estado.setVotos(votos);

                Map<String, Object> geoJson = buscarGeoJson("estados", estadoDTO.getId());
                if (geoJson != null) {
                    estado.setGeoJson(geoJson);
                    System.out.println("GeoJSON encontrado para estado: " + sigla);
                } else {
                    System.out.println("Nenhum GeoJSON encontrado para estado: " + sigla);
                }

                estadoRepository.save(estado);
                totalEstados++;
                System.out.println("Estado salvo/atualizado: " + sigla + " - " + nome);
            }

            else if (linha.size() > 5 && "MU".equals(linha.get(2))) {
                String nomeMunicipio = (String) linha.get(0);
                String codigoIBGE = (String) linha.get(1);
                String estadoSigla = (String) linha.get(5);

                List<Voto> votos = new ArrayList<>();
                for (int i = 6; i < linha.size(); i++) {
                    List<?> votoBruto = (List<?>) linha.get(i);
                    votos.add(new Voto(
                            (String) votoBruto.get(0),
                            (String) votoBruto.get(1),
                            ((Number) votoBruto.get(2)).intValue(),
                            ((Number) votoBruto.get(3)).doubleValue(),
                            "S".equals(votoBruto.get(4))
                    ));
                }

                EstadoDTO estadoDTO = estadosPorSigla.get(estadoSigla.toUpperCase());
                if (estadoDTO == null) {
                    System.err.println("Estado com sigla '" + estadoSigla + "' não encontrado para município '" + nomeMunicipio + "'. Pulando...");
                    continue;
                }

                Map<String, Object> geoJsonMunicipio = buscarGeoJsonMunicipio(codigoIBGE);
                if (geoJsonMunicipio != null) {
                    System.out.println(" GeoJSON encontrado para município: " + nomeMunicipio);
                } else {
                    System.out.println(" Nenhum GeoJSON encontrado para município: " + nomeMunicipio);
                }

                Municipio municipio = new Municipio(codigoIBGE, nomeMunicipio, estadoSigla, String.valueOf(estadoDTO.getId()), votos, geoJsonMunicipio);
                municipioRepository.save(municipio);
                totalMunicipios++;
                System.out.println("  → Município salvo: " + nomeMunicipio + " (" + codigoIBGE + ")");
            }
        }

        System.out.println("\nTotal de estados salvos/atualizados: " + totalEstados);
        System.out.println("Total de municípios salvos: " + totalMunicipios);
    }

    private void carregarEstadosDoIBGE() {
        RestTemplate restTemplate = new RestTemplate();
        EstadoDTO[] estadosArray = restTemplate.getForObject(
                "https://servicodados.ibge.gov.br/api/v1/localidades/estados",
                EstadoDTO[].class
        );
        if (estadosArray != null) {
            estadosPorSigla = Arrays.stream(estadosArray)
                    .collect(Collectors.toMap(
                            e -> e.getSigla().toUpperCase(),
                            e -> e
                    ));
        }
    }

    private Map<String, Object> buscarGeoJson(String tipo, int id) {
        try {
            String url = "https://servicodados.ibge.gov.br/api/v3/malhas/" + tipo + "/" + id + "?formato=application/vnd.geo+json";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.geo+json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao buscar GeoJSON de " + tipo + " ID " + id + ": " + e.getMessage());
            return null;
        }
    }

    private Map<String, Object> buscarGeoJsonMunicipio(String idMunicipio) {
        try {
            String url = "https://servicodados.ibge.gov.br/api/v3/malhas/municipios/" + idMunicipio + "?formato=application/vnd.geo+json";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.geo+json");
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
    
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
    
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Erro ao buscar GeoJSON do município ID " + idMunicipio + ": " + e.getMessage());
            return null;
        }
    }
}