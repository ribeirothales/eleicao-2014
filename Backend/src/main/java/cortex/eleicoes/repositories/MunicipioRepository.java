package cortex.eleicoes.repositories;

import cortex.eleicoes.models.Municipio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends MongoRepository<Municipio, String> {

    List<Municipio> findByEstadoSigla(String sigla);

    List<Municipio> findByEstadoCodigo(String codigo);
    
}