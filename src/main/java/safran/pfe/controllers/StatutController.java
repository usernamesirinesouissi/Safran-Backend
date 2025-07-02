package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.Statut;
import safran.pfe.repo.StatutRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuts")
@CrossOrigin(origins = "*")
public class StatutController {

    @Autowired
    private StatutRepository repository;

    @GetMapping
    public List<Statut> getAllStatuts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statut> getStatutById(@PathVariable Long id) {
        Optional<Statut> statut = repository.findById(id);
        return statut.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createStatut(@RequestBody Statut statut) {
        Optional<Statut> existingStatut = repository.findByNomStatut(statut.getNomStatut());

        if (existingStatut.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce statut existe déjà dans la base de données.");
        }

        Statut savedStatut = repository.save(statut);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStatut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statut> updateStatut(@PathVariable Long id, @RequestBody Statut statutDetails) {
        Statut statut = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

        statut.setNomStatut(statutDetails.getNomStatut());
        return ResponseEntity.ok(repository.save(statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatut(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}