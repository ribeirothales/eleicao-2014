package cortex.eleicoes.repositories;


import cortex.eleicoes.models.Estado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends MongoRepository<Estado, String> {

    Estado findBySigla(String sigla);
}