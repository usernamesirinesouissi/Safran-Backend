package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.TypeStage;
import safran.pfe.entities.WorkflowDefinition;
import safran.pfe.repo.TypeStageRepo;
import safran.pfe.services.WorkflowService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-stages")
@CrossOrigin(origins = "*")
public class TypeStageController {

    @Autowired
    private TypeStageRepo repository;

   @GetMapping
    public List<TypeStage> getAllTypeStages() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeStage> getTypeStageById(@PathVariable Long id) {
        Optional<TypeStage> typeStage = repository.findById(id);
        return typeStage.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTypeStage(@RequestBody TypeStage typeStage) {
        Optional<TypeStage> existingTypeStage = repository.findByNomTypeStage(typeStage.getNomTypeStage());

        if (existingTypeStage.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce type de stage existe déjà dans la base de données.");
        }

        // Vérifier que workflowDefinition est bien null si absent
        if (typeStage.getWorkflowDefinition() == null) {
            typeStage.setWorkflowDefinition(null);
        }

        TypeStage savedTypeStage = repository.save(typeStage);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTypeStage);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TypeStage> updateTypeStage(@PathVariable Long id, @RequestBody TypeStage typeStageDetails) {
        TypeStage typeStage = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de stage non trouvé"));

        typeStage.setNomTypeStage(typeStageDetails.getNomTypeStage());
        return ResponseEntity.ok(repository.save(typeStage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeStage(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
/*
    @Autowired
    private WorkflowService workflowService;

    @GetMapping
    public List<TypeStage> getAllTypeStages() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeStage> getTypeStageById(@PathVariable Long id) {
        Optional<TypeStage> typeStage = repository.findById(id);
        return typeStage.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTypeStage(@RequestBody TypeStage typeStage) {
        Optional<TypeStage> existingTypeStage = repository.findByNomTypeStage(typeStage.getNomTypeStage());

        if (existingTypeStage.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce type de stage existe déjà.");
        }

        // Vérifie si un workflow est défini
        if (typeStage.getWorkflowDefinition() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Un workflow doit être associé à ce type de stage.");
        }

        TypeStage savedTypeStage = repository.save(typeStage);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTypeStage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeStage> updateTypeStage(@PathVariable Long id, @RequestBody TypeStage typeStageDetails) {
        TypeStage typeStage = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de stage non trouvé"));

        typeStage.setNomTypeStage(typeStageDetails.getNomTypeStage());
        typeStage.setWorkflowDefinition(typeStageDetails.getWorkflowDefinition());
        
        return ResponseEntity.ok(repository.save(typeStage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeStage(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/workflow")
    public ResponseEntity<WorkflowDefinition> getWorkflowForTypeStage(@PathVariable Long id) {
        Optional<TypeStage> typeStage = repository.findById(id);
        return typeStage.map(ts -> ResponseEntity.ok(ts.getWorkflowDefinition()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}*/