package Controllers;

import Database.Models.LeagueEntryDTO;
import Services.LeagueEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/league-entries")
public class LeagueEntryController {

    private final LeagueEntryService service;

    @Autowired
    public LeagueEntryController(LeagueEntryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LeagueEntryDTO>> getAllLeagueEntries() {
        List<LeagueEntryDTO> entries = service.getAllEntries();
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<LeagueEntryDTO> createLeagueEntry(@RequestBody LeagueEntryDTO entry) {
        LeagueEntryDTO savedEntry = service.saveEntry(entry);
        return ResponseEntity.ok(savedEntry);
    }
}
