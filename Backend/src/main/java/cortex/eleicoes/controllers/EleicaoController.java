package cortex.eleicoes.controllers;

import cortex.eleicoes.models.Estado;
import cortex.eleicoes.services.EleicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eleicao/{ano}/{cargo}/{turno}")
public class EleicaoController {

    @Autowired
    private EleicaoService eleicaoService;

    // votos agregados por estado e suas malhas em geojson
    @GetMapping("/estados")
    public List<Estado> getVotosPorEstado(@PathVariable String ano, @PathVariable String cargo, 
                                          @PathVariable String turno) {
        return eleicaoService.buscarVotosPorEstado(ano, cargo, turno);
    }

    // votos agregados por estado e suas malhas em geojson
    @GetMapping("/estados/{estado}/municipios")
    public List<?> getVotosPorMunicipio(@PathVariable String ano, @PathVariable String cargo,
                                        @PathVariable String turno, @PathVariable String estado) {
        return eleicaoService.buscarVotosPorMunicipio(ano, cargo, turno, estado);
    }
}
