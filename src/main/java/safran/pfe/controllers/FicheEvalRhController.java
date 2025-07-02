package safran.pfe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.FicheEvalRh;
import safran.pfe.entities.DTO.CompetencesRequestDTO;
import safran.pfe.entities.DTO.FicheEvalRhResponseDTO;
// Si vous avez une classe interne StatutDTO dans FicheEvalRhResponseDTO
// import safran.pfe.entities.DTO.FicheEvalRhResponseDTO.StatutDTO;
// Si StatutDTO est une classe séparée (recommandé)
import safran.pfe.entities.DTO.FicheEvalRhResponseDTO.CompetenceDTO;
import safran.pfe.entities.DTO.FicheEvalRhResponseDTO.StatutDTO; // Utilisé par convertToDto
import safran.pfe.entities.DTO.StatutUpdateDTO;
// Utilisé par convertToDto
import safran.pfe.services.FicheEvalRhService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fiches-rh")
@CrossOrigin(origins = "http://localhost:4200",
methods = {RequestMethod.GET, RequestMethod.POST, 
         RequestMethod.PUT, RequestMethod.PATCH, 
         RequestMethod.DELETE, RequestMethod.OPTIONS},
allowedHeaders = "*",
maxAge = 3600)
public class FicheEvalRhController {

    private final FicheEvalRhService ficheService;

    // Constructeur pour l'injection de dépendances
    public FicheEvalRhController(FicheEvalRhService ficheService) {
        this.ficheService = ficheService;
    }

    @GetMapping
    public ResponseEntity<List<FicheEvalRhResponseDTO>> getAllFichesRh() {
        List<FicheEvalRh> fiches = ficheService.getAllFichesRh();
        List<FicheEvalRhResponseDTO> dtos = fiches.stream()
                                                 .map(this::convertToDto) // Assurez-vous que convertToDto est bien défini
                                                 .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<FicheEvalRhResponseDTO> createFicheRh(@RequestBody FicheEvalRh fiche) {
        // Attention: Recevoir une entité directement peut être risqué si elle contient des
        // sous-objets non managés ou des IDs qui pourraient entrer en conflit.
        // Un FicheEvalRhCreateRequestDTO serait plus sûr.
        FicheEvalRh createdFiche = ficheService.createFicheRh(fiche);
        return ResponseEntity.ok(convertToDto(createdFiche));
    }

    @PostMapping("/fiche-eval/{ficheEvalId}/fiches-rh/{ficheRhId}/competences")
    public ResponseEntity<FicheEvalRhResponseDTO> addOrUpdateCompetences(
        @PathVariable Long ficheEvalId,
        @PathVariable Long ficheRhId,
        @RequestBody CompetencesRequestDTO request // S'assurer que CompetencesRequestDTO est bien défini
    ) {
        // La méthode de service gère maintenant la logique de synchro complexe
        FicheEvalRh updatedFiche = ficheService.addOrUpdateCompetencesToFiche(ficheEvalId, ficheRhId, request.getCompetences());
        return ResponseEntity.ok(convertToDto(updatedFiche)); // Renvoie le DTO de la fiche mise à jour
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheEvalRhResponseDTO> getFicheRhById(@PathVariable Long id) {
        FicheEvalRh fiche = ficheService.getFicheRhById(id);
        return ResponseEntity.ok(convertToDto(fiche));
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<Double> getScoreTotalRh(@PathVariable Long id) {
        return ResponseEntity.ok(ficheService.getScoreTotalRh(id));
    }

    // Méthode de conversion vers FicheEvalRhResponseDTO
    // Assurez-vous que FicheEvalRhResponseDTO et ses DTOs internes (StatutDTO, CompetenceDTO) sont bien définis.
    private FicheEvalRhResponseDTO convertToDto(FicheEvalRh fiche) {
        if (fiche == null) {
            return null;
        }
        FicheEvalRhResponseDTO dto = new FicheEvalRhResponseDTO();
        dto.setId(fiche.getId());
        dto.setScoreTotalRh(fiche.getScoreTotalRh()); // Sera recalculé par @PostLoad ou @PreUpdate

     // ...
        if (fiche.getStatut() != null) {
            // Si StatutDTO est une classe séparée :
            StatutDTO statutDTO = new StatutDTO(); // Vous créez une instance de votre classe externe safran.pfe.entities.DTO.StatutDTO
            statutDTO.setId(fiche.getStatut().getId());
            statutDTO.setNomStatut(fiche.getStatut().getNomStatut());
            dto.setStatut(statutDTO); // ERREUR ICI: dto.setStatut s'attend à FicheEvalRhResponseDTO.StatutDTO (classe interne)
       
          
        }

        if (fiche.getCompetences() != null) {
            List<CompetenceDTO> competencesDTO = fiche.getCompetences().stream()
                .map(c -> { // c est une FicheRhCompetence
                    CompetenceDTO compDto = new CompetenceDTO(); // Classe interne de FicheEvalRhResponseDTO
                    if (c.getCompetence() != null) {
                        compDto.setId(c.getCompetence().getId()); // ID de CompetenceComportementale
                        compDto.setNom(c.getCompetence().getNomCompetence());
                    }
                    compDto.setScoreCompetenceRh(c.getScoreCompetenceRh());
                    // Vous pourriez aussi vouloir l'ID de FicheRhCompetence (c.getId()) dans le DTO si le frontend en a besoin.
                    // compDto.setFicheRhCompetenceId(c.getId());
                    return compDto;
                })
                .collect(Collectors.toList());
            dto.setCompetences(competencesDTO);
        }
        return dto;
    }
    
    
    @PutMapping("/fiche-eval/{ficheEvalId}/fiches-rh/{ficheRhId}/statut") // Changé de PATCH à PUT
    public ResponseEntity<FicheEvalRhResponseDTO> updateStatutFicheRh(
        @PathVariable Long ficheEvalId,
        @PathVariable Long ficheRhId,
        @RequestBody StatutUpdateDTO statutUpdateDTO
    ) {
        FicheEvalRh updatedFiche = ficheService.updateStatutFicheRh(ficheEvalId, ficheRhId, statutUpdateDTO.getStatutId());
        return ResponseEntity.ok(convertToDto(updatedFiche));
    }
}