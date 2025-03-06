package Database;

import Database.Models.LeagueEntryDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueEntryDTORepository extends MongoRepository<LeagueEntryDTO, String> {

}
