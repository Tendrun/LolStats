package Services;

import Database.LeagueEntryDTORepository;
import Database.Models.LeagueEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueEntryService {

    private final LeagueEntryDTORepository repository;

    @Autowired
    public LeagueEntryService(LeagueEntryDTORepository repository) {
        this.repository = repository;
    }

    public List<LeagueEntryDTO> getAllEntries() {
        return repository.findAll();
    }

    public LeagueEntryDTO saveEntry(LeagueEntryDTO entry) {
        return repository.save(entry);
    }
}
