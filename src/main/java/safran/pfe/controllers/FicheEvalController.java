package safran.pfe.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.FicheEval;
import safran.pfe.entities.FicheEvalRh;
import safran.pfe.entities.FicheEvalTech;
import safran.pfe.entities.FicheRhCompetence;
import safran.pfe.entities.FicheTechCompetence;
import safran.pfe.entities.Statut;
import safran.pfe.entities.DTO.FicheEvalResponseDTO;
import safran.pfe.repo.FicheEvalRepository;
import safran.pfe.services.FicheEvalService;
import safran.pfe.entities.DTO.FicheEvalForEntretienCreateRequestDTO; // Nouveau DTO pour le create
//Assurez-vous que tous vos DTOs sont importés ici
import safran.pfe.entities.DTO.FicheEvalDetailResponseDTO;
import safran.pfe.entities.DTO.FicheRhResponseDTO;
import safran.pfe.entities.DTO.FicheTechResponseDTO;
import safran.pfe.entities.DTO.StatutDTO;
import safran.pfe.entities.DTO.FicheRhCompetenceDTO;
import safran.pfe.entities.DTO.FicheTechCompetenceDTO;
import safran.pfe.entities.DTO.CompetenceComportementaleDTO;
import safran.pfe.entities.DTO.ExigenceTechniqueDTO;
import safran.pfe.entities.DTO.CandidatInfoDTO;
import safran.pfe.entities.DTO.FicheEvalResponseDTO; // Gardez-le si utilisé par d'autres endpoints
@RestController
@RequestMapping("/api/fiche-eval")
public class FicheEvalController {
	@Autowired

    private  FicheEvalService ficheEvalService;
	@Autowired

    private  FicheEvalRepository ficheEvalRepository;

	
	 @GetMapping("/entretien/{entretienId}")
	    public ResponseEntity<FicheEvalDetailResponseDTO> getFicheEvalDetailsByEntretienId(@PathVariable Long entretienId) {
	        Optional<FicheEval> ficheEvalEntityOpt = ficheEvalRepository.findByEntretienSpecifiqueId(entretienId);

	        if (ficheEvalEntityOpt.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }

	        FicheEval ficheEvalEntity = ficheEvalEntityOpt.get();
	     

	        FicheEvalDetailResponseDTO responseDTO = convertToFicheEvalDetailDTO(ficheEvalEntity);
	        return ResponseEntity.ok(responseDTO);
	    }

	    // --- Méthodes de Conversion pour FicheEvalDetailResponseDTO ---

	    private FicheEvalDetailResponseDTO convertToFicheEvalDetailDTO(FicheEval entity) {
	        if (entity == null) return null;
	        FicheEvalDetailResponseDTO dto = new FicheEvalDetailResponseDTO();
	        dto.setId(entity.getId());
	        if (entity.getEntretienSpecifique() != null) {
	            dto.setEntretienId(entity.getEntretienSpecifique().getId());
	            if (entity.getEntretienSpecifique().getCandidature() != null &&
	                entity.getEntretienSpecifique().getCandidature().getCandidat() != null) {
	                dto.setCandidat(convertToCandidatInfoDTO(entity.getEntretienSpecifique().getCandidature().getCandidat()));
	            }
	        }
	        //dto.setStatut(convertToStatutDTO(entity.getStatut()));
	        dto.setStatutGlobal(convertToStatutDTO(entity.getStatutGlobal()));
	        dto.setScoreTotal(entity.getScoreTotal());
	        dto.setFicheRh(convertToFicheRhResponseDTO(entity.getFicheRh()));
	        dto.setFicheTech(convertToFicheTechResponseDTO(entity.getFicheTech()));
	        return dto;
	    }

	 // Dans FicheEvalController.java (ou où que vous ayez placé cette méthode de conversion)

	    private CandidatInfoDTO convertToCandidatInfoDTO(Candidat candidatEntity) {
	        if (candidatEntity == null) return null;
	        CandidatInfoDTO dto = new CandidatInfoDTO();
	        dto.setId(candidatEntity.getId());

	        // Étape 1: Construire la chaîne nomComplet
	        String prenom = candidatEntity.getPrenomCandidat() != null ? candidatEntity.getPrenomCandidat() : "";
	        String nom = candidatEntity.getNomCandidat() != null ? candidatEntity.getNomCandidat() : "";
	        
	        String nomCompletIntermediaire;
	        if (!prenom.isEmpty() && !nom.isEmpty()) {
	            nomCompletIntermediaire = prenom + " " + nom;
	        } else if (!prenom.isEmpty()) {
	            nomCompletIntermediaire = prenom;
	        } else {
	            nomCompletIntermediaire = nom; // Si le prénom est vide mais pas le nom
	        }
	        // Si les deux sont vides, nomCompletIntermediaire sera une chaîne vide

	        // Étape 2: Appeler trim() sur la chaîne construite
	        String nomCompletFinal = nomCompletIntermediaire.trim();

	        // Étape 3: Affecter la chaîne finale au DTO
	        dto.setNomComplet(nomCompletFinal);
	        
	        dto.setEmail(candidatEntity.getEmail());
	        return dto;
	    }

	    private FicheRhResponseDTO convertToFicheRhResponseDTO(FicheEvalRh rhEntity) {
	        if (rhEntity == null) return null;
	        FicheRhResponseDTO dto = new FicheRhResponseDTO();
	        dto.setId(rhEntity.getId());
	        dto.setStatut(convertToStatutDTO(rhEntity.getStatut()));
	        dto.setScoreTotalRh(rhEntity.getScoreTotalRh());
	        if (rhEntity.getCompetences() != null) {
	            dto.setCompetences(
	                rhEntity.getCompetences().stream()
	                    .map(this::convertToFicheRhCompetenceDTO)
	                    .collect(Collectors.toList())
	            );
	        }
	        return dto;
	    }

	    private FicheTechResponseDTO convertToFicheTechResponseDTO(FicheEvalTech techEntity) {
	        if (techEntity == null) return null;
	        FicheTechResponseDTO dto = new FicheTechResponseDTO();
	        dto.setId(techEntity.getId());
	        dto.setStatut(convertToStatutDTO(techEntity.getStatut()));
	        dto.setScoreTotalTech(techEntity.getScoreTotalTech());
	        if (techEntity.getCompetences() != null) { // La liste s'appelle bien 'competences' dans FicheEvalTech
	            dto.setCompetences(
	                techEntity.getCompetences().stream()
	                    .map(this::convertToFicheTechCompetenceDTO)
	                    .collect(Collectors.toList())
	            );
	        }
	        return dto;
	    }

	    private FicheRhCompetenceDTO convertToFicheRhCompetenceDTO(FicheRhCompetence frcEntity) {
	        if (frcEntity == null) return null;
	        FicheRhCompetenceDTO dto = new FicheRhCompetenceDTO();
	        dto.setId(frcEntity.getId()); // ID de l'entité FicheRhCompetence
	        dto.setScoreCompetenceRh(frcEntity.getScoreCompetenceRh());
	        if (frcEntity.getCompetence() != null) {
	            CompetenceComportementaleDTO ccDto = new CompetenceComportementaleDTO();
	            ccDto.setId(frcEntity.getCompetence().getId());
	            ccDto.setNomCompetence(frcEntity.getCompetence().getNomCompetence());
	            dto.setCompetence(ccDto);
	        }
	        return dto;
	    }

	    private FicheTechCompetenceDTO convertToFicheTechCompetenceDTO(FicheTechCompetence ftcEntity) {
	        if (ftcEntity == null) return null;
	        FicheTechCompetenceDTO dto = new FicheTechCompetenceDTO();
	        dto.setId(ftcEntity.getId()); // ID de l'entité FicheTechCompetence
	        dto.setScoreCompetenceTech(ftcEntity.getScoreCompetenceTech());
	        if (ftcEntity.getExigenceTechnique() != null) {
	            ExigenceTechniqueDTO etDto = new ExigenceTechniqueDTO();
	            etDto.setId(ftcEntity.getExigenceTechnique().getId());
	            etDto.setNomExigence(ftcEntity.getExigenceTechnique().getNomExigence());
	            dto.setExigenceTechnique(etDto); // Assurez-vous que FicheTechCompetenceDTO a un champ exigenceTechnique
	        }
	        return dto;
	    }

	    private StatutDTO convertToStatutDTO(Statut statutEntity) {
	        if (statutEntity == null) return null;
	        StatutDTO dto = new StatutDTO();
	        dto.setId(statutEntity.getId());
	        dto.setNomStatut(statutEntity.getNomStatut());
	        return dto;
	    }
	
    @PostMapping("/entretien") 
    public ResponseEntity<FicheEvalResponseDTO> createFicheForEntretien(@RequestBody FicheEvalForEntretienCreateRequestDTO createRequest) {
        if (createRequest == null || createRequest.getEntretienId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            FicheEval createdFiche = ficheEvalService.createFicheEvalForEntretien(createRequest.getEntretienId());
            return ResponseEntity.status(HttpStatus.CREATED).body(FicheEvalResponseDTO.fromEntity(createdFiche));
        } catch (IllegalStateException e) { 
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheEvalResponseDTO> getFicheById(@PathVariable Long id) {
        return ficheEvalService.getFicheById(id)
                .map(fiche -> ResponseEntity.ok(FicheEvalResponseDTO.fromEntity(fiche)))
                .orElse(ResponseEntity.notFound().build());
    }

   /* @GetMapping("/entretien/{entretienId}")
    public ResponseEntity<FicheEvalResponseDTO> getFicheByEntretienId(@PathVariable Long entretienId) {
        return ficheEvalService.getFicheByEntretienId(entretienId)
                .map(fiche -> ResponseEntity.ok(FicheEvalResponseDTO.fromEntity(fiche)))
                .orElse(ResponseEntity.notFound().build());
    }*/

    @GetMapping
    public ResponseEntity<List<FicheEvalResponseDTO>> getAllFiches() {
        List<FicheEval> fiches = ficheEvalService.getAllFiches();
        List<FicheEvalResponseDTO> dtos = fiches.stream()
                                             .map(FicheEvalResponseDTO::fromEntity)
                                             .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/link-rh")
    public ResponseEntity<FicheEvalResponseDTO> linkRh(@PathVariable Long id, @RequestBody FicheEvalRh rhRequest) {
        FicheEval fiche = ficheEvalService.getFicheById(id)
                .orElseThrow(() -> new EntityNotFoundException("FicheEval avec id=" + id + " non trouvé"));
        fiche.setFicheRh(rhRequest);
        FicheEval updatedFiche = ficheEvalRepository.save(fiche);
        return ResponseEntity.ok(FicheEvalResponseDTO.fromEntity(updatedFiche));
    }

    @PutMapping("/{id}/link-tech")
    public ResponseEntity<FicheEvalResponseDTO> linkTech(@PathVariable Long id, @RequestBody FicheEvalTech techRequest) {
        FicheEval fiche = ficheEvalService.getFicheById(id)
                .orElseThrow(() -> new EntityNotFoundException("FicheEval avec id=" + id + " non trouvé"));
        fiche.setFicheTech(techRequest);
        FicheEval updatedFiche = ficheEvalRepository.save(fiche);
        return ResponseEntity.ok(FicheEvalResponseDTO.fromEntity(updatedFiche));
    }
    
    
    
    
    @PutMapping("/{id}/update-score")
    public ResponseEntity<FicheEvalResponseDTO> updateFicheScore(@PathVariable Long id) {
        FicheEval updatedFiche = ficheEvalService.updateFicheEvalScore(id);
        return ResponseEntity.ok(FicheEvalResponseDTO.fromEntity(updatedFiche));
    }
    
}
/*
@RestController
@RequestMapping("/api/fiches-eval")
public class FicheEvalController {

    private final FicheEvalService ficheEvalService;

    public FicheEvalController(FicheEvalService ficheEvalService) {
        this.ficheEvalService = ficheEvalService;
    }

    @PostMapping
    public ResponseEntity<FicheEval> createFicheEval() {
        return ResponseEntity.ok(ficheEvalService.createFicheEval());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheEvalResponseDTO> getFicheEval(@PathVariable Long id) {
        FicheEval fiche = ficheEvalService.getFicheEvalById(id);
        return ResponseEntity.ok(convertToDTO(fiche));
    }

    private FicheEvalResponseDTO convertToDTO(FicheEval fiche) {
        FicheEvalResponseDTO dto = new FicheEvalResponseDTO();
        dto.setId(fiche.getId());
        dto.setScoreTotal(fiche.getScoreTotal());
        
        // Statut RH
        if (fiche.getFicheRh() != null && fiche.getFicheRh().getStatut() != null) {
            FicheEvalResponseDTO.StatutDTO statutRh = new FicheEvalResponseDTO.StatutDTO();
            statutRh.setId(fiche.getFicheRh().getStatut().getId());
            statutRh.setNomStatut(fiche.getFicheRh().getStatut().getNomStatut());
            dto.setStatutRh(statutRh);
        }
        
        // Statut Tech
        if (fiche.getFicheTech() != null && fiche.getFicheTech().getStatut() != null) {
            FicheEvalResponseDTO.StatutDTO statutTech = new FicheEvalResponseDTO.StatutDTO();
            statutTech.setId(fiche.getFicheTech().getStatut().getId());
            statutTech.setNomStatut(fiche.getFicheTech().getStatut().getNomStatut());
            dto.setStatutTech(statutTech);
        }
        
        return dto;
    }
}*/