package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.TypeEvaluation;
import safran.pfe.repo.TypeEvaluationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-evaluations")
public class TypeEvalController {

    @Autowired
    private TypeEvaluationRepository typeEvaluationRepository;

    // Ajouter un nouveau type d'évaluation
    @PostMapping
    public ResponseEntity<TypeEvaluation> createTypeEvaluation(@RequestBody TypeEvaluation typeEvaluation) {
        TypeEvaluation savedType = typeEvaluationRepository.save(typeEvaluation);
        return ResponseEntity.ok(savedType);
    }

    // Récupérer tous les types d'évaluation
    @GetMapping
    public ResponseEntity<List<TypeEvaluation>> getAllTypeEvaluations() {
        List<TypeEvaluation> types = typeEvaluationRepository.findAll();
        return ResponseEntity.ok(types);
    }

    // Récupérer un type par son ID
    @GetMapping("/{id}")
    public ResponseEntity<TypeEvaluation> getTypeEvaluationById(@PathVariable Long id) {
        Optional<TypeEvaluation> type = typeEvaluationRepository.findById(id);
        return type.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un type d'évaluation
    @PutMapping("/{id}")
    public ResponseEntity<TypeEvaluation> updateTypeEvaluation(
            @PathVariable Long id,
            @RequestBody TypeEvaluation typeEvaluation) {
        
        if (!typeEvaluationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        typeEvaluation.setId(id);
        TypeEvaluation updatedType = typeEvaluationRepository.save(typeEvaluation);
        return ResponseEntity.ok(updatedType);
    }

    // Supprimer un type d'évaluation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEvaluation(@PathVariable Long id) {
        if (!typeEvaluationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        typeEvaluationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}