package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.PropositionEcole;
import safran.pfe.repo.PropositionEcoleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ecoles")
@CrossOrigin(origins = "*")
public class EcoleController {

    @Autowired
    private PropositionEcoleRepository repository;

    @GetMapping
    public List<PropositionEcole> getAllEcoles() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropositionEcole> getEcoleById(@PathVariable Long id) {
        Optional<PropositionEcole> ecole = repository.findById(id);
        return ecole.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEcole(@RequestBody PropositionEcole ecole) {
        Optional<PropositionEcole> existingEcole = repository.findByNomEcole(ecole.getNomEcole());

        if (existingEcole.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette école existe déjà dans la base de données.");
        }

        PropositionEcole savedEcole = repository.save(ecole);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEcole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropositionEcole> updateEcole(@PathVariable Long id, @RequestBody PropositionEcole ecoleDetails) {
        PropositionEcole ecole = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("École non trouvée"));

        ecole.setNomEcole(ecoleDetails.getNomEcole());
        return ResponseEntity.ok(repository.save(ecole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEcole(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}