package cortex.eleicoes.repositories;

import cortex.eleicoes.models.Estado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoRepository extends MongoRepository<Estado, String> {

    // Busca por sigla (útil para DataLoader ou outras operações)
    Estado findBySigla(String sigla);

    // Método otimizado para buscar estados filtrando por ano, cargo e turno nos votos
    // A query exata depende da estrutura do objeto Voto e como os campos ano, cargo, turno estão armazenados.
    // Este é um EXEMPLO de query que assume que 'ano', 'cargo', 'turno' são campos dentro de cada elemento da lista 'votos'.
    // Ajuste a query conforme a estrutura real do seu modelo Voto.
    // Usar projeção { 'sigla': 1, 'nome': 1 } para retornar apenas campos essenciais pode otimizar ainda mais.
    @Query("{ 'votos': { $elemMatch: { 'ano': ?0, 'cargo': ?1, 'turno': ?2 } } }") // Placeholder - ajuste a query!
    List<Estado> findByVotosAnoCargoTurno(String ano, String cargo, String turno);

    // @Query(value = "{ 'votos': { $elemMatch: { 'ano': ?0, 'cargo': ?1, 'turno': ?2 } } }", fields = "{ 'sigla': 1, 'nome': 1, 'id': 1 }")
    // List<EstadoProjection> findProjecaoByVotosAnoCargoTurno(String ano, String cargo, String turno);
    // (Onde EstadoProjection seria uma interface definindo os campos sigla, nome, id)

}

