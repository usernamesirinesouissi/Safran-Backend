package safran.pfe.controllers;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import safran.pfe.entities.NodeType;
import safran.pfe.entities.NodeValidation;
import safran.pfe.entities.Role;
import safran.pfe.entities.TypeStage;
import safran.pfe.entities.User;
import safran.pfe.entities.ValidationInstance;
import safran.pfe.entities.ValidationStatus;
import safran.pfe.entities.WorkflowDefinition;
import safran.pfe.entities.WorkflowInstance;
import safran.pfe.entities.WorkflowNode;
import safran.pfe.entities.DTO.NodeValidationDTO;
import safran.pfe.repo.TypeStageRepo;
import safran.pfe.repo.WorkflowDefinitionRepository;
import safran.pfe.services.WorkflowService;



@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private TypeStageRepo typeStageRepo;
    
    @Autowired 
    private WorkflowDefinitionRepository workflowRepository;
    
    
    
 // Récupérer tous les rôles
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(workflowService.getAllRoles());
    }

    // Récupérer les utilisateurs d'un rôle
    @GetMapping("/roles/{roleId}/users")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Integer roleId) {
        return ResponseEntity.ok(workflowService.getUsersByRole(roleId));
    }
    
    
    @PostMapping("/validations/{validationId}/assign")
    public ResponseEntity<ValidationInstance> assignUserToValidation(
            @PathVariable String validationId,
            @RequestParam String userId,
            @RequestParam String roleName) {  // Changé de Long à String
        
        try {
            ValidationInstance assigned = workflowService.assignUserToValidation(validationId, userId, roleName);
            return ResponseEntity.ok(assigned);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/validations/by-user/{userId}")
    public ResponseEntity<List<ValidationInstance>> getValidationsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(workflowService.getValidationsByUser(userId));
    }

    @GetMapping("/validations/by-role/{roleName}")  // Changé de roleId à roleName
    public ResponseEntity<List<ValidationInstance>> getValidationsByRole(@PathVariable String roleName) {
        return ResponseEntity.ok(workflowService.getValidationsByRole(roleName));
    }
    
    
    //gestion workflow par type (ajout / update/delete)
    @GetMapping("/definitions/{id}/type-stage")
    public ResponseEntity<List<TypeStage>> getTypeStagesByWorkflow(@PathVariable String id) {
        Optional<WorkflowDefinition> workflowOpt = workflowService.getWorkflowDefinitionById(id);

        if (!workflowOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<TypeStage> typeStages = workflowOpt.get().getTypeStages();
        return ResponseEntity.ok(typeStages);
    }
    
    
    @PostMapping("/definitions/{id}/type-stage")
    public ResponseEntity<WorkflowDefinition> addTypeStageToWorkflow(
            @PathVariable String id,
            @RequestBody TypeStage typeStage) {

        Optional<WorkflowDefinition> workflowOpt = workflowService.getWorkflowDefinitionById(id);

        if (!workflowOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        WorkflowDefinition workflow = workflowOpt.get();

        Optional<TypeStage> existingTypeStageOpt = typeStageRepo.findByNomTypeStage(typeStage.getNomTypeStage());

        TypeStage finalTypeStage;
        if (existingTypeStageOpt.isPresent()) {
            finalTypeStage = existingTypeStageOpt.get();
            
            if (finalTypeStage.getWorkflowDefinition() == null || 
                !finalTypeStage.getWorkflowDefinition().equals(workflow)) {
                finalTypeStage.setWorkflowDefinition(workflow);
                finalTypeStage = typeStageRepo.save(finalTypeStage);
            }
        } else {
            typeStage.setWorkflowDefinition(workflow);
            finalTypeStage = typeStageRepo.save(typeStage);
        }

        if (!workflow.getTypeStages().contains(finalTypeStage)) {
            workflow.getTypeStages().add(finalTypeStage);
            workflowRepository.save(workflow);
        }

        return ResponseEntity.ok(workflow);
    }

    
    /*@PostMapping("/definitions/{id}/type-stage")
    public ResponseEntity<WorkflowDefinition> addTypeStageToWorkflow(
            @PathVariable String id,
            @RequestBody TypeStage typeStage) {

        Optional<WorkflowDefinition> workflowOpt = workflowService.getWorkflowDefinitionById(id);
        
        if (!workflowOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        WorkflowDefinition workflow = workflowOpt.get();

        // Vérifier si le TypeStage existe déjà
        Optional<TypeStage> existingTypeStageOpt = typeStageRepo.findByNomTypeStage(typeStage.getNomTypeStage());

        TypeStage finalTypeStage;
        if (existingTypeStageOpt.isPresent()) {
            finalTypeStage = existingTypeStageOpt.get();
        } else {
            // Si le TypeStage n'existe pas, on le crée
            typeStage.setWorkflowDefinition(workflow);
            finalTypeStage = typeStageRepo.save(typeStage);
        }

        // Ajouter le TypeStage s'il n'est pas déjà associé
        if (!workflow.getTypeStages().contains(finalTypeStage)) {
            workflow.getTypeStages().add(finalTypeStage);
            workflowRepository.save(workflow);
        }

        return ResponseEntity.ok(workflow);
    }*/

    
    
    @PutMapping("/definitions/{workflowId}/type-stage/{typeStageId}")
    public ResponseEntity<TypeStage> updateTypeStageInWorkflow(
            @PathVariable String workflowId,
            @PathVariable Long typeStageId,
            @RequestBody TypeStage newTypeStage) {

        Optional<WorkflowDefinition> workflowOpt = workflowService.getWorkflowDefinitionById(workflowId);
        if (!workflowOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<TypeStage> typeStageOpt = typeStageRepo.findById(typeStageId);
        if (!typeStageOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TypeStage typeStage = typeStageOpt.get();

        typeStage.setNomTypeStage(newTypeStage.getNomTypeStage());

        TypeStage updatedTypeStage = typeStageRepo.save(typeStage);

        return ResponseEntity.ok(updatedTypeStage);
    }



    @GetMapping("/definitions")
    public ResponseEntity<List<WorkflowDefinition>> getAllWorkflowDefinitions() {
        return ResponseEntity.ok(workflowService.getLatestWorkflowDefinitions());
    }

    @GetMapping("/definitions/{idOrKey}")
    public ResponseEntity<WorkflowDefinition> getLatestVersionByIdOrKey(@PathVariable String idOrKey) {
        Optional<WorkflowDefinition> latestVersion = workflowService.getLatestVersionByIdOrKey(idOrKey);
        if (latestVersion.isPresent()) {
            return ResponseEntity.ok(latestVersion.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/definitions")
    public ResponseEntity<WorkflowDefinition> createWorkflowDefinition(@RequestBody WorkflowDefinition definition) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("💾 Reçu JSON : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(definition));
            return ResponseEntity.status(HttpStatus.CREATED).body(workflowService.saveWorkflowDefinition(definition));
        } catch (Exception e) {
        	System.out.println("Erreur lors de la création de la définition du workflow : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/definitions/{id}")
    public ResponseEntity<WorkflowDefinition> updateWorkflowDefinition(
            @PathVariable String id, @RequestBody WorkflowDefinition definition) {

        if (!id.equals(definition.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(workflowService.saveWorkflowDefinition(definition));
    }

    @DeleteMapping("/definitions/{id}")
    public ResponseEntity<Void> deleteWorkflowDefinition(@PathVariable String id) {
        workflowService.deleteWorkflowDefinition(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/definitions/{id}/deploy")
    public ResponseEntity<WorkflowDefinition> deployWorkflow(@PathVariable String id) {
        try {
            WorkflowDefinition deployed = workflowService.deployWorkflow(id);
            return ResponseEntity.ok(deployed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/instances")
    public ResponseEntity<WorkflowInstance> startWorkflow(
            @RequestParam String definitionId,
            @RequestParam String initiatedBy,
            @RequestBody Map<String, String> variables) {

        try {
            WorkflowInstance instance = workflowService.startWorkflow(definitionId, initiatedBy, variables);
            return ResponseEntity.status(HttpStatus.CREATED).body(instance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/instances")
    public ResponseEntity<List<WorkflowInstance>> getAllWorkflowInstances() {
        return ResponseEntity.ok(workflowService.getAllWorkflowInstances());
    }

    @GetMapping("/instances/{id}")
    public ResponseEntity<WorkflowInstance> getWorkflowInstanceById(@PathVariable String id) {
        Optional<WorkflowInstance> instance = workflowService.getWorkflowInstanceById(id);
        return instance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/instances/definition/{definitionId}")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowInstancesByDefinitionId(@PathVariable String definitionId) {
        return ResponseEntity.ok(workflowService.getWorkflowInstancesByDefinitionId(definitionId));
    }



    @GetMapping("/validations")
    public ResponseEntity<List<ValidationInstance>> getPendingValidations(@RequestParam String role) {
        return ResponseEntity.ok(workflowService.getPendingValidations(role));
    }

    @GetMapping("/validations/user/{userId}")
    public ResponseEntity<List<ValidationInstance>> getUserValidations(@PathVariable String userId) {
        return ResponseEntity.ok(workflowService.getValidationsAssignedToUser(userId));
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


    @GetMapping("/node-types")
    public ResponseEntity<List<NodeType>> getNodeTypes() {
        return ResponseEntity.ok(workflowService.getNodeTypes());
    }



    @GetMapping("/nodes/{nodeId}/validations")
    public ResponseEntity<List<NodeValidation>> getNodeValidations(@PathVariable String nodeId) {
        Optional<WorkflowNode> node = workflowService.getWorkflowNodeById(nodeId);
        if (node.isPresent()) {
            return ResponseEntity.ok(node.get().getValidations());
        }
        return ResponseEntity.notFound().build();
    }

    /*@PostMapping("/nodes/{nodeId}/validations")
    public ResponseEntity<NodeValidation> addNodeValidation(
            @PathVariable String nodeId,
            @RequestBody NodeValidation validation) {
        try {
            NodeValidation added = workflowService.addNodeValidation(nodeId, validation);
            return ResponseEntity.status(HttpStatus.CREATED).body(added);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }*/
    @PostMapping("/nodes/{nodeId}/validations")
    public ResponseEntity<NodeValidation> addNodeValidation(
        @PathVariable String nodeId,
        @RequestBody NodeValidationDTO validationDTO) {

        try {
            NodeValidation validation = workflowService.addNodeValidation(
                nodeId, 
                validationDTO.getName(),
                validationDTO.getSeverity(),
                validationDTO.getAssignedRole(),
                validationDTO.getAssignedUser(),
                validationDTO.getDescription(),
                validationDTO.isRequired(),
                validationDTO.getTimeoutMinutes(),
                validationDTO.getFormTemplate(),
                validationDTO.getValidationType()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(validation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/nodes/{nodeId}/validations/{validationId}")
    public ResponseEntity<NodeValidation> updateNodeValidation(
            @PathVariable String nodeId,
            @PathVariable String validationId,
            @RequestBody NodeValidation validation) {

        if (!validationId.equals(validation.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            NodeValidation updated = workflowService.updateNodeValidation(nodeId, validation);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/nodes/{nodeId}/validations/{validationId}")
    public ResponseEntity<Void> deleteNodeValidation(
            @PathVariable String nodeId,
            @PathVariable String validationId) {
        try {
            workflowService.deleteNodeValidation(nodeId, validationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/definitions/{id}/latest")
    public ResponseEntity<WorkflowDefinition> getLatestVersionById(@PathVariable String id) {
        Optional<WorkflowDefinition> definition = workflowService.getWorkflowDefinitionById(id);
        if (definition.isPresent()) {
            String key = definition.get().getKey();
            Optional<WorkflowDefinition> latestVersion = workflowService.getLatestVersionByIdOrKey(key);
            if (latestVersion.isPresent()) {
                return ResponseEntity.ok(latestVersion.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/definitions/versions/{key}")
    public ResponseEntity<List<WorkflowDefinition>> getAllVersionsByKey(@PathVariable String key) {
        List<WorkflowDefinition> versions = workflowService.getAllVersionsByKey(key);
        return ResponseEntity.ok(versions);
    }
    

  

} 

