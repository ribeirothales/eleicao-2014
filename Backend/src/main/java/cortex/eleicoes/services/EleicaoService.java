package cortex.eleicoes.services;

import cortex.eleicoes.models.Estado;
import cortex.eleicoes.models.Municipio;
import cortex.eleicoes.repositories.EstadoRepository;
import cortex.eleicoes.repositories.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EleicaoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<Estado> buscarVotosPorEstado(String ano, String cargo, String turno) {
        return estadoRepository.findAll();
    }

    public List<Municipio> buscarVotosPorMunicipio(String ano, String cargo, String turno, String estado) {
        if (estado.matches("\\d+")) {
            return municipioRepository.findByEstadoCodigo(estado);
        } else {
            return municipioRepository.findByEstadoSigla(estado);
        }
    }
}
