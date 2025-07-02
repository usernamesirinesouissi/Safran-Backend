package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.NiveauEvaluation;
import safran.pfe.entities.DTO.NiveauEvaluationDTO;
import safran.pfe.repo.NiveauEvaluationRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/niveaux-evaluation")
public class NiveauEvalController {

    @Autowired
    private NiveauEvaluationRepository niveauRepository;

   

    @GetMapping
    public ResponseEntity<List<NiveauEvaluationDTO>> getAllNiveaux() {
        try {
            // On récupère les ENTITÉS depuis la base de données
            List<NiveauEvaluation> niveauxEntites = niveauRepository.findAll();

            // SÉCURITÉ : On vérifie si la liste est nulle (même si findAll retourne souvent une liste vide)
            if (niveauxEntites == null) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // MEILLEURE PRATIQUE : On convertit les Entités en DTOs
            // C'est l'étape la plus importante pour éviter les erreurs de sérialisation
            List<NiveauEvaluationDTO> niveauxDTOs = niveauxEntites.stream()
                .map(this::convertToDto) // ou une méthode de mapping
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(niveauxDTOs);

        } catch (Exception e) {
            // Loggez l'erreur pour voir ce qui se passe côté serveur
            e.printStackTrace(); 
            return ResponseEntity.status(500).build();
        }
    }
     
    private NiveauEvaluationDTO convertToDto(NiveauEvaluation entite) {
        NiveauEvaluationDTO dto = new NiveauEvaluationDTO();
        dto.setId(entite.getId());
        dto.setNomNiveau(entite.getNomNiveau());
        return dto;
    }


    // Récupérer un niveau par son ID
    @GetMapping("/{id}")
    public ResponseEntity<NiveauEvaluation> getNiveauById(@PathVariable Long id) {
        Optional<NiveauEvaluation> niveau = niveauRepository.findById(id);
        return niveau.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    @PostMapping 
    public ResponseEntity<NiveauEvaluation> createNiveau(@RequestBody NiveauEvaluation niveau) {
        try {
            NiveauEvaluation nouveauNiveau = niveauRepository.save(niveau);
            return ResponseEntity.ok(nouveauNiveau);
        } catch (Exception e) {
        
            return ResponseEntity.badRequest().build(); 
        }
    }

    // Mettre à jour un niveau existant
    @PutMapping("/{id}")
    public ResponseEntity<NiveauEvaluation> updateNiveau(
            @PathVariable Long id, 
            @RequestBody NiveauEvaluation niveauDetails) {
        return niveauRepository.findById(id)
                .map(niveau -> {
                    niveau.setNomNiveau(niveauDetails.getNomNiveau());
                    NiveauEvaluation updatedNiveau = niveauRepository.save(niveau);
                    return ResponseEntity.ok(updatedNiveau);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Supprimer un niveau
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        try {
            niveauRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

  
}