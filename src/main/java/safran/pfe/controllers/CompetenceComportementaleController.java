package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.CompetenceComportementale;
import safran.pfe.repo.CompetenceComportementaleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/competences")
@CrossOrigin(origins = "*")
public class CompetenceComportementaleController {
    
    @Autowired
    private CompetenceComportementaleRepository repository;

    @GetMapping
    public List<CompetenceComportementale> getAllCompetences() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetenceComportementale> getCompetenceById(@PathVariable Long id) {
        Optional<CompetenceComportementale> competence = repository.findById(id);
        return competence.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCompetence(@RequestBody CompetenceComportementale competence) {
        Optional<CompetenceComportementale> existingCompetence = repository.findByNomCompetence(competence.getNomCompetence());
        
        if (existingCompetence.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La compétence existe déjà dans la base de données.");
        }
        
        // Sinon, on l'ajoute à la base de données
        CompetenceComportementale savedCompetence = repository.save(competence);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompetence);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetenceComportementale> updateCompetence(@PathVariable Long id, @RequestBody CompetenceComportementale competenceDetails) {
        CompetenceComportementale competence = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competence not found"));

        competence.setNomCompetence(competenceDetails.getNomCompetence());
        return ResponseEntity.ok(repository.save(competence));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
