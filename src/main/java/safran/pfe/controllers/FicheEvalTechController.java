package safran.pfe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.DTO.CompetencesRequestDTO;
import safran.pfe.entities.DTO.ExigencesRequestDTO;
import safran.pfe.entities.DTO.FicheEvalTechResponseDTO;
import safran.pfe.entities.DTO.FicheEvalTechResponseDTO.ExigenceDTO;
import safran.pfe.entities.DTO.FicheEvalTechResponseDTO.StatutDTO;
import safran.pfe.entities.ExigenceTechnique;
import safran.pfe.entities.FicheEvalTech;
import safran.pfe.services.FicheEvalTechService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fiches-tech")
public class FicheEvalTechController {

    private final FicheEvalTechService ficheTechService;

    public FicheEvalTechController(FicheEvalTechService ficheTechService) {
        this.ficheTechService = ficheTechService;
    }

    // Créer une fiche technique
    @PostMapping
    public ResponseEntity<FicheEvalTechResponseDTO> createFicheTech(@RequestBody FicheEvalTech fiche) {
        FicheEvalTech createdFiche = ficheTechService.createFicheTech(fiche);
        return ResponseEntity.ok(convertToDto(createdFiche));
    }

    @PostMapping("/fiche-eval/{ficheEvalId}/fiches-tech/{ficheTechId}/exigences")
    public ResponseEntity<FicheEvalTechResponseDTO> addOrUpdateExigences(
        @PathVariable Long ficheEvalId,
        @PathVariable Long ficheTechId,
        @RequestBody ExigencesRequestDTO request
    ) {
        FicheEvalTech updatedFiche = ficheTechService.addOrUpdateExigencesToFiche(ficheTechId, request.getExigences());
        return ResponseEntity.ok(convertToDto(updatedFiche));
    }
    
    @GetMapping("/fiche-eval/{ficheEvalId}/fiches-tech/{ficheTechId}/exigences")
    public ResponseEntity<FicheEvalTechResponseDTO> getExigences(
        @PathVariable Long ficheEvalId,
        @PathVariable Long ficheTechId
    ) {
        FicheEvalTech fiche = ficheTechService.getFicheTechById(ficheTechId);
        return ResponseEntity.ok(convertToDto(fiche));
    } 
    
    
    // Récupérer le score total d'une fiche d'évaluation
    @GetMapping("/{ficheTechId}/score-total")
    public ResponseEntity<Double> getScoreTotal(@PathVariable Long ficheTechId) {
        double scoreTotal = ficheTechService.getScoreTotalTech(ficheTechId);
        return ResponseEntity.ok(scoreTotal);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FicheEvalTechResponseDTO> getFicheTechById(@PathVariable Long id) {
        FicheEvalTech fiche = ficheTechService.getFicheTechById(id);
        return ResponseEntity.ok(convertToDto(fiche));
    }

    // Obtenir le score total technique
    @GetMapping("/{id}/score")
    public ResponseEntity<Double> getScoreTotalTech(@PathVariable Long id) {
        return ResponseEntity.ok(ficheTechService.getScoreTotalTech(id));
    }

    // Conversion vers DTO
    private FicheEvalTechResponseDTO convertToDto(FicheEvalTech fiche) {
        FicheEvalTechResponseDTO dto = new FicheEvalTechResponseDTO();
        dto.setId(fiche.getId());
        dto.setScoreTotalTech(fiche.getScoreTotalTech());

        if (fiche.getStatut() != null) {
            StatutDTO statutDTO = new StatutDTO();
            statutDTO.setId(fiche.getStatut().getId());
            statutDTO.setNomStatut(fiche.getStatut().getNomStatut());
            dto.setStatut(statutDTO);
        }

        List<ExigenceDTO> exigencesDTO = fiche.getCompetences().stream()
            .map(c -> {
                ExigenceDTO exigenceDTO = new ExigenceDTO();
                exigenceDTO.setId(c.getExigenceTechnique().getId());
                exigenceDTO.setNomExigence(c.getExigenceTechnique().getNomExigence());
                exigenceDTO.setScoreCompetenceTech(c.getScoreCompetenceTech());
                return exigenceDTO;
            })
            .collect(Collectors.toList());
        dto.setExigences(exigencesDTO);

        return dto;
    }
    
    
    @PutMapping("/fiche-eval/{ficheEvalId}/fiches-tech/{ficheTechId}/statut") // Changé de PATCH à PUT
    public ResponseEntity<FicheEvalTechResponseDTO> updateStatutFicheTech(
        @PathVariable Long ficheEvalId,
        @PathVariable Long ficheTechId,
        @RequestBody Map<String, Long> payload) {
        
        Long statutId = payload.get("statutId");
        FicheEvalTech updatedFiche = ficheTechService.updateStatutFicheTech(ficheTechId, statutId);
        return ResponseEntity.ok(convertToDto(updatedFiche));
    }

  

}



   /* private final FicheEvalTechService ficheService;

    public FicheEvalTechController(FicheEvalTechService ficheService) {
        this.ficheService = ficheService;
    }

    @PostMapping
    public ResponseEntity<FicheEvalTechResponseDTO> createFicheTech(@RequestBody FicheEvalTech fiche) {
        FicheEvalTech createdFiche = ficheService.createFicheTech(fiche);
        return ResponseEntity.ok(convertToDTO(createdFiche));
    }

    @PostMapping("/{ficheId}/exigences")
    public ResponseEntity<FicheEvalTechResponseDTO> addExigencesToFiche(
        @PathVariable Long ficheId,
        @RequestBody ExigencesRequestDTO request
    ) {
        FicheEvalTech updatedFiche = ficheService.addExigencesToFiche(ficheId, request.getExigences());
        return ResponseEntity.ok(convertToDTO(updatedFiche));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheEvalTechResponseDTO> getFicheTechById(@PathVariable Long id) {
        FicheEvalTech fiche = ficheService.getFicheTechById(id);
        return ResponseEntity.ok(convertToDTO(fiche));
    }

    private FicheEvalTechResponseDTO convertToDTO(FicheEvalTech fiche) {
        FicheEvalTechResponseDTO dto = new FicheEvalTechResponseDTO();
        dto.setId(fiche.getId());
        dto.setScoreTotalTech(fiche.getScoreTotalTech() != null ? fiche.getScoreTotalTech() : 0.0); 

        if (fiche.getStatut() != null) {
            FicheEvalTechResponseDTO.StatutDTO statutDTO = new FicheEvalTechResponseDTO.StatutDTO();
            statutDTO.setId(fiche.getStatut().getId());
            statutDTO.setNomStatut(fiche.getStatut().getNomStatut());
            dto.setStatut(statutDTO);
        }

        dto.setExigences(fiche.getCompetences().stream().map(c -> {
            FicheEvalTechResponseDTO.ExigenceDTO exigenceDTO = new FicheEvalTechResponseDTO.ExigenceDTO();
            exigenceDTO.setId(c.getExigenceTechnique().getId());
            exigenceDTO.setNomExigence(c.getExigenceTechnique().getNomExigence());
            exigenceDTO.setScoreCompetenceTech(c.getScoreCompetenceTech());
            return exigenceDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}*/