package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.UO;
import safran.pfe.repo.UORepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/uo")
@CrossOrigin(origins = "*")
public class UOController {

    @Autowired
    private UORepository repository;

    @GetMapping
    public List<UO> getAllUOs() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UO> getUOById(@PathVariable Long id) {
        Optional<UO> uo = repository.findById(id);
        return uo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUO(@RequestBody UO uo) {
        Optional<UO> existingUO = repository.findByNomUO(uo.getNomUO());

        if (existingUO.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette UO existe déjà dans la base de données.");
        }

        UO savedUO = repository.save(uo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UO> updateUO(@PathVariable Long id, @RequestBody UO uoDetails) {
        UO uo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UO non trouvée"));

        uo.setNomUO(uoDetails.getNomUO());
        return ResponseEntity.ok(repository.save(uo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUO(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
