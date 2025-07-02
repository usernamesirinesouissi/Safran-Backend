package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.*;
import safran.pfe.entities.DTO.AddCompetenceRequest;
import safran.pfe.entities.DTO.CompetenceComportementaleDTO;
import safran.pfe.entities.DTO.EvaluationCompetenceRHDTO;
import safran.pfe.entities.DTO.EvaluationCompetenceTechDTO;
import safran.pfe.entities.DTO.EvaluationStageDTO;
import safran.pfe.entities.DTO.ExigenceTechniqueDTO;
import safran.pfe.entities.DTO.NiveauEvaluationDTO;
import safran.pfe.entities.DTO.TypeEvaluationDTO;
import safran.pfe.entities.DTO.UpdateCompetenceNiveauRequest;
import safran.pfe.repo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationStageController {

    @Autowired
    private EvaluationStageRepo evaluationStageRepository;
    
    @Autowired
    private CandidatureRepository candidatureRepository;
    
    @Autowired
    private TypeEvaluationRepository typeEvaluationRepository;
    
    @Autowired
    private CompetenceComportementaleRepository competenceRHRepository;
    
    @Autowired
    private ExigenceTechniqueRepository competenceTechRepository;
    
    @Autowired
    private NiveauEvaluationRepository niveauRepository;
    
    @Autowired
    private EvaluationCompetenceRHRepository evalCompetenceRHRepository;
    
    @Autowired
    private EvaluationCompetenceTechRepository evalCompetenceTechRepository;

    // NOUVEL ENDPOINT : Créer ou récupérer une évaluation pour une candidature et un type
    @PostMapping("/for-candidature/{candidatureId}/type/{typeId}")
    public ResponseEntity<?> getOrCreateEvaluationForCandidatureAndType(
            @PathVariable Long candidatureId,
            @PathVariable Long typeId) {
        
        try {
            // Vérifier que la candidature existe
            Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée"));
            
            // Vérifier que le type d'évaluation existe
            TypeEvaluation typeEvaluation = typeEvaluationRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'évaluation non trouvé"));
            
            // Chercher une évaluation existante pour cette candidature et ce type
            Optional<EvaluationStage> existingEvaluation = evaluationStageRepository
                .findByCandidatureIdAndTypeEvaluationId(candidatureId, typeId);
            
            if (existingEvaluation.isPresent()) {
                // Retourner l'évaluation existante
                EvaluationStageDTO dto = toEvaluationStageDTO(existingEvaluation.get());
                return ResponseEntity.ok(dto);
            } else {
                // Créer une nouvelle évaluation
                EvaluationStage newEvaluation = new EvaluationStage();
                newEvaluation.setCandidature(candidature);
                newEvaluation.setTypeEvaluation(typeEvaluation);
                
                EvaluationStage saved = evaluationStageRepository.save(newEvaluation);
                EvaluationStageDTO dto = toEvaluationStageDTO(saved);
                
                return ResponseEntity.status(HttpStatus.CREATED).body(dto);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Reste du code existant...
    @PutMapping("/{evaluationId}/competences-rh/{competenceId}")
    public ResponseEntity<?> updateCompetenceRH(
            @PathVariable Long evaluationId,
            @PathVariable Long competenceId,
            @RequestBody UpdateCompetenceNiveauRequest request) {
        
        EvaluationCompetenceRH evalCompetence = evalCompetenceRHRepository
            .findByEvaluationIdAndCompetenceId(evaluationId, competenceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "L'évaluation pour la compétence " + competenceId + " n'existe pas dans l'évaluation " + evaluationId));

        NiveauEvaluation newNiveau = niveauRepository.findById(request.getNiveauId())
            .orElseThrow(() -> new ResourceNotFoundException("Le niveau avec l'ID " + request.getNiveauId() + " n'a pas été trouvé."));
            
        evalCompetence.setNiveau(newNiveau);
        evalCompetenceRHRepository.save(evalCompetence);
        
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{evaluationId}/competences-tech/{competenceId}")
    public ResponseEntity<?> updateCompetenceTech(
            @PathVariable Long evaluationId,
            @PathVariable Long competenceId,
            @RequestBody UpdateCompetenceNiveauRequest request) {
        
        EvaluationCompetenceTech evalCompetence = evalCompetenceTechRepository
            .findByEvaluationIdAndCompetenceId(evaluationId, competenceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "L'évaluation pour la compétence " + competenceId + " n'existe pas dans l'évaluation " + evaluationId));

        NiveauEvaluation newNiveau = niveauRepository.findById(request.getNiveauId())
            .orElseThrow(() -> new ResourceNotFoundException("Le niveau avec l'ID " + request.getNiveauId() + " n'a pas été trouvé."));
            
        evalCompetence.setNiveau(newNiveau);
        evalCompetenceTechRepository.save(evalCompetence);
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-candidature/{candidatureId}")
    public ResponseEntity<List<EvaluationStageDTO>> getEvaluationsByCandidatureId(
            @PathVariable Long candidatureId) {
        
        List<EvaluationStage> evaluations = evaluationStageRepository.findAllByCandidatureId(candidatureId);
        
        if (evaluations.isEmpty()) {
            throw new ResourceNotFoundException("Aucune fiche d'évaluation trouvée");
        }
        
        List<EvaluationStageDTO> dtos = evaluations.stream()
            .map(this::toEvaluationStageDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    private EvaluationStageDTO toEvaluationStageDTO(EvaluationStage entity) {
        if (entity == null) return null;
        
        EvaluationStageDTO dto = new EvaluationStageDTO();
        dto.setId(entity.getId());
        dto.setCandidatureId(entity.getCandidature().getId());
        
        if (entity.getTypeEvaluation() != null) {
            TypeEvaluationDTO typeDto = new TypeEvaluationDTO();
            typeDto.setId(entity.getTypeEvaluation().getId());
            typeDto.setNomTypeEval(entity.getTypeEvaluation().getNomTypeEval());
            dto.setTypeEvaluation(typeDto);
        }
        
        return dto;
    }

    @PostMapping("/for-candidature/{candidatureId}")
    public ResponseEntity<?> createForCandidature(@PathVariable Long candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
            .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée"));
        
        if (!"Affecté à un besoin".equals(candidature.getStatutCandidature().getNom())) {
            return ResponseEntity.badRequest()
                .body("La candidature doit être au statut 'Affecté à un besoin'");
        }
        
        if (evaluationStageRepository.existsByCandidatureId(candidatureId)) {
            return ResponseEntity.badRequest()
                .body("Une évaluation existe déjà pour cette candidature");
        }
        
        EvaluationStage evaluation = new EvaluationStage();
        evaluation.setCandidature(candidature);
        EvaluationStage saved = evaluationStageRepository.save(evaluation);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{evaluationId}/type")
    public ResponseEntity<?> setEvaluationType(
            @PathVariable Long evaluationId,
            @RequestParam Long typeId) {
        
        try {
            EvaluationStage evaluation = evaluationStageRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));
            
            TypeEvaluation type = typeEvaluationRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'évaluation non trouvé"));
            
            evaluation.setTypeEvaluation(type);
            evaluationStageRepository.save(evaluation);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Type d'évaluation mis à jour avec succès");
            response.put("evaluationId", evaluationId);
            response.put("typeId", typeId);
            response.put("typeName", type.getNomTypeEval());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/{evaluationId}/competences-rh")
    public ResponseEntity<?> addCompetenceRH(
            @PathVariable Long evaluationId,
            @RequestBody AddCompetenceRequest request) {
        
        try {
            EvaluationStage evaluation = evaluationStageRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée avec l'id : " + evaluationId));
            CompetenceComportementale competence = competenceRHRepository.findById(request.getCompetenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Compétence RH non trouvée avec l'id : " + request.getCompetenceId()));
            NiveauEvaluation niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec l'id : " + request.getNiveauId()));

            EvaluationCompetenceRH evalCompetence = new EvaluationCompetenceRH();
            evalCompetence.setEvaluation(evaluation);
            evalCompetence.setCompetence(competence);
            evalCompetence.setNiveau(niveau);

            EvaluationCompetenceRH saved = evalCompetenceRHRepository.save(evalCompetence);
            EvaluationCompetenceRHDTO responseDto = toRHDTO(saved);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erreur d'intégrité des données: " + e.getRootCause().getMessage());
        }
    }

    @PostMapping("/{evaluationId}/competences-tech")
    public ResponseEntity<?> addCompetenceTech(
            @PathVariable Long evaluationId,
            @RequestBody AddCompetenceRequest request) {
        try {
            EvaluationStage evaluation = evaluationStageRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée avec l'id : " + evaluationId));
            ExigenceTechnique competence = competenceTechRepository.findById(request.getCompetenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Compétence technique non trouvée avec l'id : " + request.getCompetenceId()));
            NiveauEvaluation niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec l'id : " + request.getNiveauId()));

            EvaluationCompetenceTech evalCompetence = new EvaluationCompetenceTech();
            evalCompetence.setEvaluation(evaluation);
            evalCompetence.setCompetence(competence);
            evalCompetence.setNiveau(niveau);

            EvaluationCompetenceTech saved = evalCompetenceTechRepository.save(evalCompetence);
            EvaluationCompetenceTechDTO responseDto = toTechDTO(saved);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erreur d'intégrité des données: " + e.getRootCause().getMessage());
        }
    }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<?> getEvaluation(@PathVariable Long evaluationId) {
        return evaluationStageRepository.findById(evaluationId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{evaluationId}/competences-rh")
    public ResponseEntity<List<EvaluationCompetenceRHDTO>> getCompetencesRHForEvaluation(@PathVariable Long evaluationId) {
        if (!evaluationStageRepository.existsById(evaluationId)) {
            throw new ResourceNotFoundException("Évaluation non trouvée avec l'id : " + evaluationId);
        }
        List<EvaluationCompetenceRHDTO> responseDtos = evalCompetenceRHRepository.findDtosByEvaluationId(evaluationId);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{evaluationId}/competences-tech")
    public ResponseEntity<List<EvaluationCompetenceTechDTO>> getCompetencesTechForEvaluation(@PathVariable Long evaluationId) {
        if (!evaluationStageRepository.existsById(evaluationId)) {
            throw new ResourceNotFoundException("Évaluation non trouvée avec l'id : " + evaluationId);
        }
        List<EvaluationCompetenceTechDTO> responseDtos = evalCompetenceTechRepository.findDtosByEvaluationId(evaluationId);
        return ResponseEntity.ok(responseDtos);
    }

    /*@GetMapping("/by-candidature/{candidatureId}/all")
    public ResponseEntity<List<EvaluationStageDTO>> getAllEvaluationsByCandidatureId(@PathVariable Long candidatureId) {
        Optional<EvaluationStage> evaluations = evaluationStageRepository.findByCandidatureId(candidatureId);
        List<EvaluationStageDTO> dtos = evaluations.stream()
            .map(this::toEvaluationStageDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }*/

    private EvaluationCompetenceRHDTO toRHDTO(EvaluationCompetenceRH entity) {
        if (entity == null) return null;
        EvaluationCompetenceRHDTO dto = new EvaluationCompetenceRHDTO();
        dto.setId(entity.getId());
        dto.setEvaluationId(entity.getEvaluation().getId());
        dto.setCompetence(toCompetenceComportementaleDTO(entity.getCompetence()));
        dto.setNiveau(toNiveauEvaluationDTO(entity.getNiveau()));
        
        return dto;
    }

    private EvaluationCompetenceTechDTO toTechDTO(EvaluationCompetenceTech entity) {
        if (entity == null) return null;
        EvaluationCompetenceTechDTO dto = new EvaluationCompetenceTechDTO();
        dto.setId(entity.getId());
        dto.setEvaluationId(entity.getEvaluation().getId());
        dto.setCompetence(toExigenceTechniqueDTO(entity.getCompetence()));
        dto.setNiveau(toNiveauEvaluationDTO(entity.getNiveau()));
        return dto;
    }

    private CompetenceComportementaleDTO toCompetenceComportementaleDTO(CompetenceComportementale entity) {
        if (entity == null) return null;
        CompetenceComportementaleDTO dto = new CompetenceComportementaleDTO();
        // Remplir les champs du DTO selon votre modèle
        return dto;
    }

    private ExigenceTechniqueDTO toExigenceTechniqueDTO(ExigenceTechnique entity) {
        if (entity == null) return null;
        ExigenceTechniqueDTO dto = new ExigenceTechniqueDTO();
        // Remplir les champs du DTO selon votre modèle
        return dto;
    }

    private NiveauEvaluationDTO toNiveauEvaluationDTO(NiveauEvaluation entity) {
        if (entity == null) return null;
        NiveauEvaluationDTO dto = new NiveauEvaluationDTO();
        // Remplir les champs du DTO selon votre modèle
        return dto;
    }
}
