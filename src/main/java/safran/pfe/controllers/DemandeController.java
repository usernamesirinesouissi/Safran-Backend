/*package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.*;
import safran.pfe.repo.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin("*")
public class DemandeController {

    @Autowired
    private DemandeRepository demandeRepository;
   
    @Autowired
    private PropositionEcoleRepository propositionEcoleRepository;
    @Autowired
    private ExigenceTechniqueRepository exigenceTechniqueRepository;
    @Autowired
    private FormationAcademiqueRepository formationAcademiqueRepository;
    
    @Autowired
    private TypeStageRepo typeStageRepo;

    @Autowired
    private CompetenceComportementaleRepository competenceComportementaleRepository;

    @GetMapping("/type-stages")
    public List<TypeStage> getAllTypeStage() {
        return typeStageRepo.findAll();
    }
    
    @GetMapping("/competencescomp")
    public List<CompetenceComportementale> getAllCompetencesComportementales() {
        return competenceComportementaleRepository.findAll();
    }

    @GetMapping("/competencestech")
    public List<ExigenceTechnique> getAllCompetencesTechniques() {
        return exigenceTechniqueRepository.findAll();
    }

    @GetMapping("/formationsacademiques")
    public List<FormationAcademique> getAllFormationsAcademiques() {
        return formationAcademiqueRepository.findAll();
    }

    @GetMapping("/propositionsecoles")
    public List<PropositionEcole> getAllPropositionsEcoles() {
        return propositionEcoleRepository.findAll();
    }
    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        return demande.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Demande createDemande(@RequestBody Demande demande) {
    	
    	if (demande.getTypeStage() != null) {
    	    List<TypeStage> typestageList = demande.getTypeStage().stream()
    	            .map(stage -> typeStageRepo.findById(stage.getId())
    	                    .orElseThrow(() -> new RuntimeException("TypeStage not found")))
    	            .collect(Collectors.toList());
    	    demande.setTypeStage(typestageList);
    	}


        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return demandeRepository.save(demande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @RequestBody Demande demandeDetails) {

        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));


        if (demandeDetails.getIntituleProjet() != null) {
            demande.setIntituleProjet(demandeDetails.getIntituleProjet());
        }
        if (demandeDetails.getDescriptionProjet() != null) {
            demande.setDescriptionProjet(demandeDetails.getDescriptionProjet());
        }
        if (demandeDetails.getRisquesProjet() != null) {
            demande.setRisquesProjet(demandeDetails.getRisquesProjet());
        }
        if (demandeDetails.getObjectifsProjet() != null) {
            demande.setObjectifsProjet(demandeDetails.getObjectifsProjet());
        }
        if (demandeDetails.getDescriptionLivrables() != null) {
            demande.setDescriptionLivrables(demandeDetails.getDescriptionLivrables());
        }
        if (demandeDetails.getEffortEstime() != null) {
            demande.setEffortEstime(demandeDetails.getEffortEstime());
        }
        if (demandeDetails.getDureeEstime() != 0) {
            demande.setDureeEstime(demandeDetails.getDureeEstime());
        }
        if (demandeDetails.getNombreStagiaires() != 0) {
            demande.setNombreStagiaires(demandeDetails.getNombreStagiaires());
        }

      
        if (demandeDetails.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demandeDetails.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demandeDetails.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demandeDetails.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demandeDetails.getFormationacademique() != null) {
            List<FormationAcademique> formations = demandeDetails.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demandeDetails.getPropecole() != null) {
            List<PropositionEcole> propositions = demandeDetails.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("PropositionEcole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        demandeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}*/

/*package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import safran.pfe.services.ValidationService;
import safran.pfe.services.WorkflowService;
import safran.pfe.entities.*;
import safran.pfe.entities.DTO.ValidationWithDemandeDTO;
import safran.pfe.repo.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin("*")
public class DemandeController {
    @Autowired
    private TypeStageRepo typeStageRepository;

    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    @Autowired
    private StatutRepository statutRepository;
    @Autowired
    private PropositionEcoleRepository propositionEcoleRepository;
    @Autowired
    private ExigenceTechniqueRepository exigenceTechniqueRepository;
    @Autowired
    private FormationAcademiqueRepository formationAcademiqueRepository;

    @Autowired
    private CompetenceComportementaleRepository competenceComportementaleRepository;

    @GetMapping("/competencescomp")
    public List<CompetenceComportementale> getAllCompetencesComportementales() {
        return competenceComportementaleRepository.findAll();
    }
    @GetMapping("/typestage")
    public List<TypeStage> getAllTypeStage() {
        return typeStageRepository.findAll();
    }
    @GetMapping("/competencestech")
    public List<ExigenceTechnique> getAllCompetencesTechniques() {
        return exigenceTechniqueRepository.findAll();
    }

    @GetMapping("/formationsacademiques")
    public List<FormationAcademique> getAllFormationsAcademiques() {
        return formationAcademiqueRepository.findAll();
    }

    @GetMapping("/propositionsecoles")
    public List<PropositionEcole> getAllPropositionsEcoles() {
        return propositionEcoleRepository.findAll();
    }
    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        return demande.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Demande createDemande(@RequestBody Demande demande) {

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return demandeRepository.save(demande);
    }

    @PostMapping("/add")
    @Transactional
    public Demande createDemandeVal(@RequestBody Demande demande) {
        System.out.println("Incoming Demande: " + demande);
        System.out.println("TypeStage: " + demande.getTypeStage());

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        if (demande.getTypeStage() != null && demande.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demande.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("TypeStage not found"));
            demande.setTypeStage(typeStage);
        } else {
            throw new RuntimeException("TypeStage must be provided for the Demande");
        }

        return demandeRepository.save(demande);
    }

    @PostMapping("/ajout")
    @Transactional
    public Demande createDemandeValidation(@RequestBody Demande demande) {
        System.out.println("Incoming Demande: " + demande);
        System.out.println("TypeStage: " + demande.getTypeStage());

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        // Fetch and set competencescomportementale
        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        // Fetch and set competencestechnique
        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        // Fetch and set formationacademique
        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        // Fetch and set propecole
        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        // Fetch and set TypeStage
        if (demande.getTypeStage() != null && demande.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demande.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("TypeStage not found"));
            demande.setTypeStage(typeStage);


            WorkflowDefinition workflowDefinition = typeStage.getWorkflowDefinition();
            if (workflowDefinition == null) {
                throw new RuntimeException("No WorkflowDefinition associated with the selected TypeStage");
            }

            if (workflowDefinition.getStatus() != WorkflowStatus.ACTIVE) {
                throw new RuntimeException("Cannot create Demande: WorkflowDefinition is not ACTIVE");
            }

            // Create and set WorkflowInstance
            WorkflowInstance workflowInstance = new WorkflowInstance();
            workflowInstance.setWorkflowDefinition(workflowDefinition);
            workflowInstance.setStatus(WorkflowInstanceStatus.CREATED);
            workflowInstance.setStartedAt(LocalDateTime.now());
            workflowInstance.setInitiatedBy("System");
            workflowInstance.setVariables(new HashMap<>());

            WorkflowInstance savedInstance = workflowInstanceRepository.save(workflowInstance);
            demande.setWorkflowInstance(savedInstance);

            workflowService.advanceWorkflow(savedInstance);
        } else {
            throw new RuntimeException("TypeStage must be provided for the Demande");
        }

        return demandeRepository.save(demande);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Demande> updateDemandeStatus(@PathVariable Long id, @RequestBody Statut status) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));

        Statut statut = statutRepository.findById(status.getId())
                .orElseThrow(() -> new RuntimeException("Statut not found"));
        demande.setStatut(statut);

        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @GetMapping("/{demandeId}/validations")
    public ResponseEntity<List<ValidationWithDemandeDTO>> getValidationsForDemande(@PathVariable Long demandeId) {
        List<ValidationWithDemandeDTO> validations = validationService.getValidationsByDemandeId(demandeId);
        return ResponseEntity.ok(validations);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @RequestBody Demande demandeDetails) {

        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));


        if (demandeDetails.getIntituleProjet() != null) {
            demande.setIntituleProjet(demandeDetails.getIntituleProjet());
        }
        if (demandeDetails.getDescriptionProjet() != null) {
            demande.setDescriptionProjet(demandeDetails.getDescriptionProjet());
        }
        if (demandeDetails.getRisquesProjet() != null) {
            demande.setRisquesProjet(demandeDetails.getRisquesProjet());
        }
        if (demandeDetails.getObjectifsProjet() != null) {
            demande.setObjectifsProjet(demandeDetails.getObjectifsProjet());
        }
        if (demandeDetails.getDescriptionLivrables() != null) {
            demande.setDescriptionLivrables(demandeDetails.getDescriptionLivrables());
        }
        if (demandeDetails.getEffortEstime() != null) {
            demande.setEffortEstime(demandeDetails.getEffortEstime());
        }
        if (demandeDetails.getDureeEstime() != 0) {
            demande.setDureeEstime(demandeDetails.getDureeEstime());
        }
        if (demandeDetails.getNombreStagiaires() != 0) {
            demande.setNombreStagiaires(demandeDetails.getNombreStagiaires());
        }

        if (demandeDetails.getStatut() != null && demandeDetails.getStatut().getId() != null) {
            Statut statut = statutRepository.findById(demandeDetails.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }
        if (demandeDetails.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demandeDetails.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }
        if (demandeDetails.getTypeStage() != null && demandeDetails.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demandeDetails.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("Type not found"));
            demande.setTypeStage(typeStage);
        }
        if (demandeDetails.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demandeDetails.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demandeDetails.getFormationacademique() != null) {
            List<FormationAcademique> formations = demandeDetails.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demandeDetails.getPropecole() != null) {
            List<PropositionEcole> propositions = demandeDetails.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("PropositionEcole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        Optional<Demande> demandeOpt = demandeRepository.findById(id);

        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();

            demandeRepository.delete(demande);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/validations/{id}/complete")
    public ResponseEntity<ValidationInstance> completeValidation(
            @PathVariable String id,
            @RequestParam ValidationStatus decision,
            @RequestParam String decisionBy,
            @RequestParam(required = false) String comments) {

        try {
            ValidationInstance completed = workflowService.completeValidation(id, decision, decisionBy, comments);
            return ResponseEntity.ok(completed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}*/

package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import safran.pfe.services.ValidationService;
import safran.pfe.services.WorkflowService;
import safran.pfe.entities.*;
import safran.pfe.entities.DTO.ValidationWithDemandeDTO;
import safran.pfe.repo.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin("*")
public class DemandeController {
    @Autowired
    private TypeStageRepo typeStageRepository;

    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    @Autowired
    private StatutRepository statutRepository;
    @Autowired
    private PropositionEcoleRepository propositionEcoleRepository;
    @Autowired
    private ExigenceTechniqueRepository exigenceTechniqueRepository;
    @Autowired
    private FormationAcademiqueRepository formationAcademiqueRepository;

    @Autowired
    private CompetenceComportementaleRepository competenceComportementaleRepository;
//////////////////////////////////////////////
    @GetMapping("/competencescomp")
    public List<CompetenceComportementale> getAllCompetencesComportementales() {
        return competenceComportementaleRepository.findAll();
    }
    @GetMapping("/typestage")
    public List<TypeStage> getAllTypeStage() {
        return typeStageRepository.findAll();
    }
    @GetMapping("/competencestech")
    public List<ExigenceTechnique> getAllCompetencesTechniques() {
        return exigenceTechniqueRepository.findAll();
    }

    @GetMapping("/formationsacademiques")
    public List<FormationAcademique> getAllFormationsAcademiques() {
        return formationAcademiqueRepository.findAll();
    }

    @GetMapping("/propositionsecoles")
    public List<PropositionEcole> getAllPropositionsEcoles() {
        return propositionEcoleRepository.findAll();
    }

    ///////////////////get methods//////////////////////////
    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        return demande.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/sujets")
    public List<Demande> getSujetsStage() {
        return demandeRepository.findByStatutId(2L);
    }

    ///////////////////////////////////////////////////////////////////////
    @PostMapping
    public Demande createDemande(@RequestBody Demande demande) {

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return demandeRepository.save(demande);
    }

    @PostMapping("/add")
    @Transactional
    public Demande createDemandeVal(@RequestBody Demande demande) {
        System.out.println("Incoming Demande: " + demande);
        System.out.println("TypeStage: " + demande.getTypeStage());

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        if (demande.getTypeStage() != null && demande.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demande.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("TypeStage not found"));
            demande.setTypeStage(typeStage);
        } else {
            throw new RuntimeException("TypeStage must be provided for the Demande");
        }

        return demandeRepository.save(demande);
    }

    @PostMapping("/ajout")
    @Transactional
    public Demande createDemandeValidation(@RequestBody Demande demande) {
        System.out.println("Incoming Demande: " + demande);
        System.out.println("TypeStage: " + demande.getTypeStage());

        if (demande.getStatut() == null || demande.getStatut().getId() == null) {
            Statut defaultStatut = statutRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default Statut not found"));
            demande.setStatut(defaultStatut);
        } else {
            Statut statut = statutRepository.findById(demande.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }

        // Fetch and set competencescomportementale
        if (demande.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demande.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }

        // Fetch and set competencestechnique
        if (demande.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demande.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        // Fetch and set formationacademique
        if (demande.getFormationacademique() != null) {
            List<FormationAcademique> formations = demande.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        // Fetch and set propecole
        if (demande.getPropecole() != null) {
            List<PropositionEcole> propositions = demande.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("Proposition Ecole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        // Fetch and set TypeStage
        if (demande.getTypeStage() != null && demande.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demande.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("TypeStage not found"));
            demande.setTypeStage(typeStage);


            WorkflowDefinition workflowDefinition = typeStage.getWorkflowDefinition();
            if (workflowDefinition == null) {
                throw new RuntimeException("No WorkflowDefinition associated with the selected TypeStage");
            }

            if (workflowDefinition.getStatus() != WorkflowStatus.ACTIVE) {
                throw new RuntimeException("Cannot create Demande: WorkflowDefinition is not ACTIVE");
            }

            // Create and set WorkflowInstance
            WorkflowInstance workflowInstance = new WorkflowInstance();
            workflowInstance.setWorkflowDefinition(workflowDefinition);
            workflowInstance.setStatus(WorkflowInstanceStatus.CREATED);
            workflowInstance.setStartedAt(LocalDateTime.now());
            workflowInstance.setInitiatedBy("System");
            workflowInstance.setVariables(new HashMap<>());

            WorkflowInstance savedInstance = workflowInstanceRepository.save(workflowInstance);
            demande.setWorkflowInstance(savedInstance);

            workflowService.advanceWorkflow(savedInstance);
        } else {
            throw new RuntimeException("TypeStage must be provided for the Demande");
        }

        return demandeRepository.save(demande);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Demande> updateDemandeStatus(@PathVariable Long id, @RequestBody Statut status) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));

        Statut statut = statutRepository.findById(status.getId())
                .orElseThrow(() -> new RuntimeException("Statut not found"));
        demande.setStatut(statut);

        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @GetMapping("/{demandeId}/validations")
    public ResponseEntity<List<ValidationWithDemandeDTO>> getValidationsForDemande(@PathVariable Long demandeId) {
        List<ValidationWithDemandeDTO> validations = validationService.getValidationsByDemandeId(demandeId);
        return ResponseEntity.ok(validations);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @RequestBody Demande demandeDetails) {

        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));


        if (demandeDetails.getIntituleProjet() != null) {
            demande.setIntituleProjet(demandeDetails.getIntituleProjet());
        }
        if (demandeDetails.getDescriptionProjet() != null) {
            demande.setDescriptionProjet(demandeDetails.getDescriptionProjet());
        }
        if (demandeDetails.getRisquesProjet() != null) {
            demande.setRisquesProjet(demandeDetails.getRisquesProjet());
        }
        if (demandeDetails.getObjectifsProjet() != null) {
            demande.setObjectifsProjet(demandeDetails.getObjectifsProjet());
        }
        if (demandeDetails.getDescriptionLivrables() != null) {
            demande.setDescriptionLivrables(demandeDetails.getDescriptionLivrables());
        }
        if (demandeDetails.getEffortEstime() != null) {
            demande.setEffortEstime(demandeDetails.getEffortEstime());
        }
        if (demandeDetails.getDureeEstime() != 0) {
            demande.setDureeEstime(demandeDetails.getDureeEstime());
        }
        if (demandeDetails.getNombreStagiaires() != 0) {
            demande.setNombreStagiaires(demandeDetails.getNombreStagiaires());
        }

        if (demandeDetails.getStatut() != null && demandeDetails.getStatut().getId() != null) {
            Statut statut = statutRepository.findById(demandeDetails.getStatut().getId())
                    .orElseThrow(() -> new RuntimeException("Statut not found"));
            demande.setStatut(statut);
        }
        if (demandeDetails.getCompetencescomportementale() != null) {
            List<CompetenceComportementale> competences = demandeDetails.getCompetencescomportementale().stream()
                    .map(competence -> competenceComportementaleRepository.findById(competence.getId())
                            .orElseThrow(() -> new RuntimeException("CompetenceComportementale not found")))
                    .collect(Collectors.toList());
            demande.setCompetencescomportementale(competences);
        }
        if (demandeDetails.getTypeStage() != null && demandeDetails.getTypeStage().getId() != null) {
            TypeStage typeStage = typeStageRepository.findById(demandeDetails.getTypeStage().getId())
                    .orElseThrow(() -> new RuntimeException("Type not found"));
            demande.setTypeStage(typeStage);
        }
        if (demandeDetails.getCompetencestechnique() != null) {
            List<ExigenceTechnique> exigences = demandeDetails.getCompetencestechnique().stream()
                    .map(exigence -> exigenceTechniqueRepository.findById(exigence.getId())
                            .orElseThrow(() -> new RuntimeException("ExigenceTechnique not found")))
                    .collect(Collectors.toList());
            demande.setCompetencestechnique(exigences);
        }

        if (demandeDetails.getFormationacademique() != null) {
            List<FormationAcademique> formations = demandeDetails.getFormationacademique().stream()
                    .map(formation -> formationAcademiqueRepository.findById(formation.getId())
                            .orElseThrow(() -> new RuntimeException("FormationAcademique not found")))
                    .collect(Collectors.toList());
            demande.setFormationacademique(formations);
        }

        if (demandeDetails.getPropecole() != null) {
            List<PropositionEcole> propositions = demandeDetails.getPropecole().stream()
                    .map(proposition -> propositionEcoleRepository.findById(proposition.getId())
                            .orElseThrow(() -> new RuntimeException("PropositionEcole not found")))
                    .collect(Collectors.toList());
            demande.setPropecole(propositions);
        }

        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        Optional<Demande> demandeOpt = demandeRepository.findById(id);

        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();

            demandeRepository.delete(demande);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/validations/{id}/complete")
    public ResponseEntity<ValidationInstance> completeValidation(
            @PathVariable String id,
            @RequestParam ValidationStatus decision,
            @RequestParam String decisionBy,
            @RequestParam(required = false) String comments) {

        try {
            ValidationInstance completed = workflowService.completeValidation(id, decision, decisionBy, comments);
            return ResponseEntity.ok(completed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}