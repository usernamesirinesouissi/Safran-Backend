package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.ExigenceTechnique;
import safran.pfe.repo.ExigenceTechniqueRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/competencetechniques")
@CrossOrigin(origins = "*")
public class CompetenceTechniqueController {

    @Autowired
    private ExigenceTechniqueRepository repository;

    @GetMapping
    public List<ExigenceTechnique> getAllExigencesTechniques() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExigenceTechnique> getExigenceTechniqueById(@PathVariable Long id) {
        Optional<ExigenceTechnique> exigenceTechnique = repository.findById(id);
        return exigenceTechnique.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createExigenceTechnique(@RequestBody ExigenceTechnique exigenceTechnique) {
        Optional<ExigenceTechnique> existingExigence = repository.findByNomExigence(exigenceTechnique.getNomExigence());

        if (existingExigence.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("L'exigence technique existe déjà dans la base de données.");
        }

        ExigenceTechnique savedExigence = repository.save(exigenceTechnique);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExigence);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExigenceTechnique> updateExigenceTechnique(@PathVariable Long id, @RequestBody ExigenceTechnique exigenceTechniqueDetails) {
        ExigenceTechnique exigenceTechnique = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exigence technique non trouvée"));

        exigenceTechnique.setNomExigence(exigenceTechniqueDetails.getNomExigence());
        return ResponseEntity.ok(repository.save(exigenceTechnique));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExigenceTechnique(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}