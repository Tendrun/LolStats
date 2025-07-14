package Controllers;

import Database.Models.LeagueEntryDTO;
import Services.CouchDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/league-entries")
public class LeagueEntryController {

    private final CouchDbService service;

    @Autowired
    public LeagueEntryController(CouchDbService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LeagueEntryDTO> createLeagueEntry(@RequestBody LeagueEntryDTO entry) {
        LeagueEntryDTO savedEntry = service.save(entry);
        return ResponseEntity.ok(savedEntry);
    }
}
