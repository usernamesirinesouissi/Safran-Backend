package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.FormationAcademique;
import safran.pfe.repo.FormationAcademiqueRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formations-academiques")
@CrossOrigin(origins = "*")
public class FormationAcadController {

    @Autowired
    private FormationAcademiqueRepository repository;

    @GetMapping
    public List<FormationAcademique> getAllFormationsAcademiques() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormationAcademique> getFormationAcademiqueById(@PathVariable Long id) {
        Optional<FormationAcademique> formation = repository.findById(id);
        return formation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createFormationAcademique(@RequestBody FormationAcademique formation) {
        Optional<FormationAcademique> existingFormation = repository.findByNomFormation(formation.getNomFormation());

        if (existingFormation.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette formation académique existe déjà dans la base de données.");
        }

        FormationAcademique savedFormation = repository.save(formation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFormation);
    }


    @PutMapping("/{id}")
    public ResponseEntity<FormationAcademique> updateFormationAcademique(@PathVariable Long id, @RequestBody FormationAcademique formationDetails) {
        FormationAcademique formation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation académique non trouvée"));

        formation.setNomFormation(formationDetails.getNomFormation());
        return ResponseEntity.ok(repository.save(formation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormationAcademique(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}