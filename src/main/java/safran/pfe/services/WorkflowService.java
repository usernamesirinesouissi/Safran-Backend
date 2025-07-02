package safran.pfe.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import safran.pfe.entities.NodeInstance;
import safran.pfe.entities.NodeInstanceStatus;
import safran.pfe.entities.NodeType;
import safran.pfe.entities.NodeValidation;
import safran.pfe.entities.Position;
import safran.pfe.entities.Role;
import safran.pfe.entities.Style;
import safran.pfe.entities.TypeStage;
import safran.pfe.entities.User;
import safran.pfe.entities.ValidationInstance;
import safran.pfe.entities.ValidationParameter;
import safran.pfe.entities.ValidationSeverity;
import safran.pfe.entities.ValidationStatus;
import safran.pfe.entities.WorkflowConnection;
import safran.pfe.entities.WorkflowDefinition;
import safran.pfe.entities.WorkflowInstance;
import safran.pfe.entities.WorkflowInstanceStatus;
import safran.pfe.entities.WorkflowNode;
import safran.pfe.entities.WorkflowStatus;
import safran.pfe.repo.NodeInstanceRepository;
import safran.pfe.repo.NodeValidationRepository;
import safran.pfe.repo.RoleRepository;
import safran.pfe.repo.TypeStageRepo;
import safran.pfe.repo.UserRepository;
import safran.pfe.repo.ValidationInstanceRepository;
import safran.pfe.repo.WorkflowConnectionRepository;
import safran.pfe.repo.WorkflowDefinitionRepository;
import safran.pfe.repo.WorkflowInstanceRepository;
import safran.pfe.repo.WorkflowNodeRepository;


@Service
public class WorkflowService {
	
	@Autowired
	private NodeValidationRepository nodeValidationRepository;
	
	@Autowired
	private WorkflowNodeRepository workflowNodeRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;
	
	  @Autowired
	  private TypeStageRepo typeStageRepo;
	  
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepo;

    @Autowired
    private WorkflowNodeRepository workflowNodeRepo;

    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepo;

    @Autowired
    private NodeInstanceRepository nodeInstanceRepo;

    @Autowired
    private ValidationInstanceRepository validationInstanceRepo;

    @Autowired
    private WorkflowConnectionRepository workflowConnectionRepo;

    @Autowired
    private ObjectMapper objectMapper;
    
    
    public List<Role> getAllRoles() {
        return roleRepository.findAll(); // Supposant que roleRepository est injecté
    }

   
    
    
    public List<User> getUsersByRole(Integer roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        
        return userRepository.findByRoles(role);
    }
    // Assignation simplifiée avec les nouveaux champs
    public ValidationInstance assignUserToValidation(String validationId, String userId, String roleName) {
        ValidationInstance validation = validationInstanceRepo.findById(validationId)
            .orElseThrow(() -> new RuntimeException("Validation non trouvée"));

        validation.setAssignedUser(userId);      // Stockage direct de l'ID utilisateur
        validation.setAssignedRole(roleName);    // Stockage direct du nom du rôle

        return validationInstanceRepo.save(validation);
    }

    // Récupération par utilisateur
    public List<ValidationInstance> getValidationsByUser(String userId) {
        return validationInstanceRepo.findByAssignedUser(userId);
    }

    // Récupération par rôle
    public List<ValidationInstance> getValidationsByRole(String roleName) {
        return validationInstanceRepo.findByAssignedRole(roleName);
    }

    /**
     * Retrieves all workflow definitions.
     * @return List of all workflow definitions.
     */
    public List<WorkflowDefinition> getAllWorkflowDefinitions() {
        return workflowDefinitionRepo.findAll();
    }

    /**
     * Retrieves a workflow definition by its ID.
     * @param id The ID of the workflow definition.
     * @return Optional containing the workflow definition if found.
     */
    public Optional<WorkflowDefinition> getWorkflowDefinitionById(String id) {
        return workflowDefinitionRepo.findById(id);
    }

    /**
     * Saves a workflow definition with proper versioning.
     * If the definition already exists, creates a new version instead of modifying the existing one.
     * @param definition The workflow definition to save.
     * @return The saved workflow definition.
     */
    /*@Transactional
    public WorkflowDefinition saveWorkflowDefinition(WorkflowDefinition definition) {
        System.out.println("Saving workflow definition: " + definition);
        System.out.println("Nodes: " + definition.getNodes());
        System.out.println("Connections: " + definition.getConnections());

        LocalDateTime now = LocalDateTime.now();
        WorkflowDefinition newDefinition;

        if (definition.getId() != null) {
            Optional<WorkflowDefinition> existingDefOpt = workflowDefinitionRepo.findById(definition.getId());
            if (existingDefOpt.isPresent()) {
                WorkflowDefinition existingDef = existingDefOpt.get();
                newDefinition = createNewVersionFromExisting(existingDef, definition, now);
            } else {
                definition.setKey(UUID.randomUUID().toString());
                newDefinition = createNewDefinition(definition, now);
            }
        } else if (definition.getKey() != null && !definition.getKey().isEmpty()) {
            List<WorkflowDefinition> existingWithKey = workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(definition.getKey());
            if (!existingWithKey.isEmpty()) {
                WorkflowDefinition latestVersion = existingWithKey.get(0);
                newDefinition = createNewVersionFromExisting(latestVersion, definition, now);
            } else {
                newDefinition = createNewDefinition(definition, now);
            }
        } else {
            definition.setKey(UUID.randomUUID().toString());
            newDefinition = createNewDefinition(definition, now);
        }

        WorkflowDefinition savedDefinition = workflowDefinitionRepo.save(newDefinition);
        Map<String, String> nodeIdMap = cloneNodesAndConnections(definition, savedDefinition);

        return workflowDefinitionRepo.save(savedDefinition);
    }*/
    
    @Transactional
    public WorkflowDefinition saveWorkflowDefinition(WorkflowDefinition definition) {
        System.out.println("Saving workflow definition: " + definition);
        System.out.println("Nodes: " + definition.getNodes());
        System.out.println("Connections: " + definition.getConnections());

        System.out.println("date  :===> " + definition.getUpdatedAt());
        
      
            LocalDateTime now = LocalDateTime.now();
            WorkflowDefinition newDefinition;

            if (definition.getId() != null) {
                Optional<WorkflowDefinition> existingDefOpt = workflowDefinitionRepo.findById(definition.getId());
                if (existingDefOpt.isPresent()) {
                    WorkflowDefinition existingDef = existingDefOpt.get();
                    newDefinition = createNewVersionFromExisting(existingDef, definition, now);
                } else {
                    definition.setKey(UUID.randomUUID().toString());
                    newDefinition = createNewDefinition(definition, now);
                }
            } else if (definition.getKey() != null && !definition.getKey().isEmpty()) {
                List<WorkflowDefinition> existingWithKey = workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(definition.getKey());
                if (!existingWithKey.isEmpty()) {
                    WorkflowDefinition latestVersion = existingWithKey.get(0);
                    newDefinition = createNewVersionFromExisting(latestVersion, definition, now);
                } else {
                    newDefinition = createNewDefinition(definition, now);
                }
            } else {
                definition.setKey(UUID.randomUUID().toString());
                newDefinition = createNewDefinition(definition, now);
            }

            for (TypeStage typeStage : definition.getTypeStages()) {
                Optional<TypeStage> existingTypeStageOpt = typeStageRepo.findById(typeStage.getId()); 

                TypeStage finalTypeStage;
                if (existingTypeStageOpt.isPresent()) {
                    finalTypeStage = existingTypeStageOpt.get();

                    if (finalTypeStage.getWorkflowDefinition() == null ||
                        !finalTypeStage.getWorkflowDefinition().equals(newDefinition)) {
                        finalTypeStage.setWorkflowDefinition(newDefinition);
                        finalTypeStage = typeStageRepo.save(finalTypeStage);
                    }
                } else {
                    typeStage.setWorkflowDefinition(newDefinition);
                    finalTypeStage = typeStageRepo.save(typeStage);
                }

                if (!newDefinition.getTypeStages().contains(finalTypeStage)) {
                    newDefinition.getTypeStages().add(finalTypeStage);
                }
            }

            WorkflowDefinition savedDefinition = workflowDefinitionRepo.save(newDefinition);
            Map<String, String> nodeIdMap = cloneNodesAndConnections(definition, savedDefinition);

            return workflowDefinitionRepo.save(savedDefinition);
    }


    /**
     * Creates a new workflow definition version based on an existing one.
     * @param existingDef The existing workflow definition.
     * @param updatedDef The updated workflow definition.
     * @param now The current timestamp.
     * @return The new workflow definition version.
     */
    private WorkflowDefinition createNewVersionFromExisting(
            WorkflowDefinition existingDef,
            WorkflowDefinition updatedDef,
            LocalDateTime now) {

        String newVersion = getNextVersionNumber(updatedDef.getKey(), existingDef.getVersion());

        WorkflowDefinition newDefinition = new WorkflowDefinition();
        newDefinition.setKey(updatedDef.getKey() != null ? updatedDef.getKey() : existingDef.getKey());
        newDefinition.setName(updatedDef.getName() != null ? updatedDef.getName() : existingDef.getName());
        newDefinition.setDescription(updatedDef.getDescription() != null ? updatedDef.getDescription() : existingDef.getDescription());
        newDefinition.setVersion(newVersion);
        newDefinition.setStatus(WorkflowStatus.DRAFT);

        newDefinition.setCreatedAt(new Date());
        newDefinition.setUpdatedAt(new Date());
        newDefinition.setNodes(new ArrayList<>());
        newDefinition.setConnections(new HashSet<>());

        return ensureUniqueKeyVersion(newDefinition);
    }

    /**
     * Increments the version string (e.g., v1 -> v2).
     * @param currentVersion The current version string.
     * @return The incremented version string.
     */
    private String incrementVersion(String currentVersion) {
        if (currentVersion == null || currentVersion.isEmpty()) {
            return "v1";
        } else if (currentVersion.startsWith("v") && currentVersion.length() > 1) {
            try {
                int versionNum = Integer.parseInt(currentVersion.substring(1));
                return "v" + (versionNum + 1);
            } catch (NumberFormatException e) {
                return currentVersion + ".1";
            }
        } else {
            return currentVersion + ".1";
        }
    }

    /**
     * Retrieves the next version number for a workflow definition.
     * @param key The workflow definition key.
     * @param currentVersion The current version.
     * @return The next version number.
     */
    private String getNextVersionNumber(String key, String currentVersion) {
        List<WorkflowDefinition> existingVersions = workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(key);

        if (existingVersions.isEmpty()) {
            return "v1";
        }

        List<String> versionStrings = existingVersions.stream()
                .map(WorkflowDefinition::getVersion)
                .collect(Collectors.toList());

        int highestVersion = 0;
        for (String version : versionStrings) {
            if (version.startsWith("v") && version.length() > 1) {
                try {
                    int versionNum = Integer.parseInt(version.substring(1));
                    if (versionNum > highestVersion) {
                        highestVersion = versionNum;
                    }
                } catch (NumberFormatException e) {
                    // Ignore non-numeric versions
                }
            }
        }

        return "v" + (highestVersion + 1);
    }

    /**
     * Ensures that the workflow definition has a unique key and version.
     * @param definition The workflow definition.
     * @return The workflow definition with a unique key and version.
     */
    private WorkflowDefinition ensureUniqueKeyVersion(WorkflowDefinition definition) {
        boolean isDuplicate = true;
        int attempt = 0;

        while (isDuplicate && attempt < 5) {
            Optional<WorkflowDefinition> existing = workflowDefinitionRepo
                    .findByKeyAndVersion(definition.getKey(), definition.getVersion());

            if (existing.isPresent()) {
                definition.setVersion(incrementVersion(definition.getVersion()));
                attempt++;
            } else {
                isDuplicate = false;
            }
        }

        if (isDuplicate) {
            throw new RuntimeException("Unable to generate unique version after " + attempt + " attempts");
        }

        return definition;
    }

    /**
     * Creates a completely new workflow definition with a UUID as key.
     * @param definition The workflow definition.
     * @param now The current timestamp.
     * @return The new workflow definition.
     */
    private WorkflowDefinition createNewDefinition(WorkflowDefinition definition, LocalDateTime now) {
        WorkflowDefinition newDefinition = new WorkflowDefinition();

        String workflowKey = (definition.getKey() != null && !definition.getKey().isEmpty())
                ? definition.getKey()
                : UUID.randomUUID().toString();

        newDefinition.setKey(workflowKey);
        newDefinition.setName(definition.getName());
        newDefinition.setDescription(definition.getDescription());
        newDefinition.setVersion("v1");
        newDefinition.setStatus(WorkflowStatus.DRAFT);

        newDefinition.setCreatedAt(new Date());
        newDefinition.setUpdatedAt(new Date());
        newDefinition.setNodes(new ArrayList<>());
        newDefinition.setConnections(new HashSet<>());

        return newDefinition;
    }

    /**
     * Clones nodes and connections from the source definition to the target definition.
     * @param sourceDefinition The source workflow definition.
     * @param targetDefinition The target workflow definition.
     * @return Map of original node IDs to new node IDs.
     */
    private Map<String, String> cloneNodesAndConnections(
            WorkflowDefinition sourceDefinition,
            WorkflowDefinition targetDefinition) {

        Map<String, String> nodeIdMap = new HashMap<>();

        if (sourceDefinition.getNodes() != null) {
            for (WorkflowNode originalNode : sourceDefinition.getNodes()) {
                WorkflowNode newNode = new WorkflowNode();
                String clientNodeId = originalNode.getId();

                newNode.setName(originalNode.getName());
                newNode.setDescription(originalNode.getDescription());
                newNode.setType(originalNode.getType());
                newNode.setValidations(new ArrayList<>());

                if (originalNode.getPosition() != null) {
                    Position position = new Position();
                    position.setX(originalNode.getPosition().getX() != null ? originalNode.getPosition().getX() : 0.0);
                    position.setY(originalNode.getPosition().getY() != null ? originalNode.getPosition().getY() : 0.0);
                    newNode.setPosition(position);
                } else {
                    newNode.setPosition(new Position());
                }

                if (originalNode.getStyle() != null) {
                    Style style = new Style();
                    style.setColor(originalNode.getStyle().getColor() != null ? originalNode.getStyle().getColor() : "defaultColor");
                    style.setIcon(originalNode.getStyle().getIcon() != null ? originalNode.getStyle().getIcon() : "defaultIcon");
                    newNode.setStyle(style);
                } else {
                    Style style = new Style();
                    style.setColor("defaultColor");
                    style.setIcon("defaultIcon");
                    newNode.setStyle(style);
                }

                newNode.setWorkflowDefinition(targetDefinition);
                WorkflowNode savedNode = workflowNodeRepo.save(newNode);

                processNodeValidations(originalNode, savedNode);
                savedNode = workflowNodeRepo.save(savedNode);

                nodeIdMap.put(clientNodeId, savedNode.getId());
                targetDefinition.getNodes().add(savedNode);
            }
        }

        if (sourceDefinition.getConnections() != null) {
            for (WorkflowConnection originalConn : sourceDefinition.getConnections()) {
                WorkflowConnection newConn = new WorkflowConnection();
                newConn.setName(originalConn.getName());

                String sourceId = originalConn.getSourceId();
                String targetId = originalConn.getTargetId();

                newConn.setSourceId(nodeIdMap.containsKey(sourceId) ? nodeIdMap.get(sourceId) : sourceId);
                newConn.setTargetId(nodeIdMap.containsKey(targetId) ? nodeIdMap.get(targetId) : targetId);
                newConn.setWorkflowDefinition(targetDefinition);

                WorkflowConnection savedConn = workflowConnectionRepo.save(newConn);
                targetDefinition.getConnections().add(savedConn);
            }
        }

        return nodeIdMap;
    }

    /**
     * Deploys a workflow definition by setting its status to ACTIVE and deprecating any existing active versions.
     * @param id The ID of the workflow definition to deploy.
     * @return The deployed workflow definition.
     */
    @Transactional
    public WorkflowDefinition deployWorkflow(String id) {
        Optional<WorkflowDefinition> optDefinition = workflowDefinitionRepo.findById(id);
        if (optDefinition.isPresent()) {
            WorkflowDefinition definition = optDefinition.get();

            List<WorkflowDefinition> activeVersions = workflowDefinitionRepo.findByKeyAndStatus(
                    definition.getKey(), WorkflowStatus.ACTIVE);

            for (WorkflowDefinition activeVersion : activeVersions) {
                activeVersion.setStatus(WorkflowStatus.DEPRECATED);
                workflowDefinitionRepo.save(activeVersion);
            }

            definition.setStatus(WorkflowStatus.ACTIVE);
            return workflowDefinitionRepo.save(definition);
        }
        throw new RuntimeException("Workflow definition not found");
    }

    /**
     * Retrieves all versions of a workflow definition by key.
     * @param key The workflow definition key.
     * @return List of workflow definitions ordered by creation date in descending order.
     */
    public List<WorkflowDefinition> getAllVersionsByKey(String key) {
        return workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(key);
    }

    /**
     * Retrieves the active version of a workflow definition by key.
     * @param key The workflow definition key.
     * @return Optional containing the active workflow definition if found.
     */
    public Optional<WorkflowDefinition> getActiveVersionByKey(String key) {
        return workflowDefinitionRepo.findByKeyAndStatus(key, WorkflowStatus.ACTIVE)
                .stream()
                .findFirst();
    }

    /**
     * Starts a workflow instance using the active version of a workflow definition.
     * @param definitionKey The key of the workflow definition.
     * @param initiatedBy The user who initiated the workflow.
     * @param variables The variables to pass to the workflow instance.
     * @return The started workflow instance.
     */
    @Transactional
    public WorkflowInstance startWorkflowByKey(String definitionKey, String initiatedBy, Map<String, String> variables) {
        Optional<WorkflowDefinition> optDefinition = getActiveVersionByKey(definitionKey);

        if (optDefinition.isPresent()) {
            return startWorkflow(optDefinition.get().getId(), initiatedBy, variables);
        }
        throw new RuntimeException("No active workflow definition found for key: " + definitionKey);
    }

    /**
     * Starts a workflow instance by definition ID.
     * @param definitionId The ID of the workflow definition.
     * @param initiatedBy The user who initiated the workflow.
     * @param variables The variables to pass to the workflow instance.
     * @return The started workflow instance.
     */
    @Transactional
    public WorkflowInstance startWorkflow(String definitionId, String initiatedBy, Map<String, String> variables) {
        Optional<WorkflowDefinition> optDefinition = workflowDefinitionRepo.findById(definitionId);
        System.out.println("Workflow definition: " + optDefinition);
        System.out.println("Initiated by: " + optDefinition.get());
        System.out.println("Variables: " + variables);
        if (optDefinition.isPresent()) {
            WorkflowDefinition definition = optDefinition.get();

            if (definition.getStatus() != WorkflowStatus.ACTIVE) {
                throw new RuntimeException("Cannot start instance of non-active workflow definition");
            }

            WorkflowInstance instance = new WorkflowInstance();
            instance.setWorkflowDefinition(definition);
            instance.setStatus(WorkflowInstanceStatus.CREATED);
            instance.setStartedAt(LocalDateTime.now());
            instance.setInitiatedBy(initiatedBy);
            instance.setVariables(variables);

            WorkflowInstance savedInstance = workflowInstanceRepo.save(instance);
            advanceWorkflow(savedInstance);
            return savedInstance;
        }
        throw new RuntimeException("Workflow definition not found");
    }

    /**
     * Deletes a workflow definition by ID.
     * @param id The ID of the workflow definition to delete.
     */
    @Transactional
    public void deleteWorkflowDefinition(String id) {
        workflowDefinitionRepo.deleteById(id);
    }

    /**
     * Advances the workflow instance to the next node.
     * @param instance The workflow instance to advance.
     */
    @Transactional
    public void advanceWorkflow(WorkflowInstance instance) {
        instance.setStatus(WorkflowInstanceStatus.RUNNING);
        workflowInstanceRepo.save(instance);

        if (instance.getNodeInstances().isEmpty()) {
            List<WorkflowNode> startNodes = workflowNodeRepo.findByWorkflowDefinitionIdAndType(
                    instance.getWorkflowDefinition().getId(), NodeType.START);

            if (startNodes.isEmpty()) {
                throw new RuntimeException("No start node found for workflow definition");
            }

            executeNode(instance, startNodes.get(0));
        } else {
            List<NodeInstance> completedNodes = instance.getNodeInstances()
                    .stream()
                    .filter(ni -> ni.getStatus() == NodeInstanceStatus.COMPLETED)
                    .collect(Collectors.toList());

            for (NodeInstance completedNode : completedNodes) {
                List<String> nextNodeIds = instance.getWorkflowDefinition().getConnections()
                        .stream()
                        .filter(conn -> conn.getSourceId().equals(completedNode.getNode().getId()))
                        .map(conn -> conn.getTargetId())
                        .collect(Collectors.toList());

                for (String nextNodeId : nextNodeIds) {
                    WorkflowNode nextNode = workflowNodeRepo.findById(nextNodeId)
                            .orElseThrow(() -> new RuntimeException("Next node not found: " + nextNodeId));

                    boolean nodeExecuted = instance.getNodeInstances()
                            .stream()
                            .anyMatch(ni -> ni.getNode().getId().equals(nextNodeId));

                    if (!nodeExecuted) {
                        executeNode(instance, nextNode);
                    }
                }
            }
        }

        checkWorkflowCompletion(instance);
    }

    /**
     * Executes a node in the workflow instance.
     * @param instance The workflow instance.
     * @param node The node to execute.
     */
    @Transactional
    public void executeNode(WorkflowInstance instance, WorkflowNode node) {
        NodeInstance nodeInstance = new NodeInstance();
        nodeInstance.setWorkflowInstance(instance);
        nodeInstance.setNode(node);
        nodeInstance.setStartedAt(LocalDateTime.now());

        if (node.getType() == NodeType.START || node.getType() == NodeType.END) {
            nodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            nodeInstance.setCompletedAt(LocalDateTime.now());
        } else if (node.getValidations() != null && !node.getValidations().isEmpty()) {
            nodeInstance.setStatus(NodeInstanceStatus.ACTIVE);

            NodeInstance savedNodeInstance = nodeInstanceRepo.save(nodeInstance);

            node.getValidations().forEach(validation -> {
                ValidationInstance validationInstance = new ValidationInstance();
                validationInstance.setNodeInstance(savedNodeInstance);
                validationInstance.setValidation(validation);
                validationInstance.setStatus(ValidationStatus.PENDING);
                validationInstance.setCreatedAt(LocalDateTime.now());

                // Assignation du rôle
                if (validation.getAssignedRole() != null) {
                    validationInstance.setAssignedRole(
                        validation.getAssignedRole().getName() // ERole -> String
                    );
                }

                // Assignation de l'utilisateur
                if (validation.getAssignedUser() != null) {
                    validationInstance.setAssignedUser(
                        validation.getAssignedUser().getId().toString()
                    );
                }

                validationInstanceRepo.save(validationInstance);
            });

            return;
        } else {
            nodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            nodeInstance.setCompletedAt(LocalDateTime.now());
        }

        nodeInstanceRepo.save(nodeInstance);
        instance.getNodeInstances().add(nodeInstance);

        if (nodeInstance.getStatus() == NodeInstanceStatus.COMPLETED) {
            advanceWorkflow(instance);
        }
    }
    /**
     * Checks if the workflow instance has completed.
     * @param instance The workflow instance to check.
     */
    private void checkWorkflowCompletion(WorkflowInstance instance) {

        boolean endNodeReached = instance.getNodeInstances()
                .stream()
                .anyMatch(ni -> ni.getNode().getType() == NodeType.END && ni.getStatus() == NodeInstanceStatus.COMPLETED);

        if (endNodeReached) {
            instance.setStatus(WorkflowInstanceStatus.COMPLETED);
            instance.setCompletedAt(LocalDateTime.now());
            workflowInstanceRepo.save(instance);
            return;
        }


        Set<String> allDefinitionNodeIds = instance.getWorkflowDefinition().getNodes()
                .stream()
                .map(WorkflowNode::getId)
                .collect(Collectors.toSet());

        Set<String> processedNodeIds = instance.getNodeInstances()
                .stream()
                .map(ni -> ni.getNode().getId())
                .collect(Collectors.toSet());


        boolean allNodesProcessed = processedNodeIds.containsAll(allDefinitionNodeIds);

        boolean allNodesCompleted = instance.getNodeInstances()
                .stream()
                .allMatch(ni -> ni.getStatus() == NodeInstanceStatus.COMPLETED || ni.getStatus() == NodeInstanceStatus.ERROR);

        if (allNodesProcessed && allNodesCompleted && instance.getStatus() != WorkflowInstanceStatus.COMPLETED) {
            instance.setStatus(WorkflowInstanceStatus.COMPLETED);
            instance.setCompletedAt(LocalDateTime.now());
            workflowInstanceRepo.save(instance);
        }
    }

    /**
     * Completes a validation instance with a decision.
     * @param validationInstanceId The ID of the validation instance.
     * @param decision The decision (APPROVED/REJECTED).
     * @param decisionBy The user who made the decision.
     * @param comments Comments on the decision.
     * @return The completed validation instance.
     */
    @Transactional
    public ValidationInstance completeValidation(String validationInstanceId, ValidationStatus decision,
                                                 String decisionBy, String comments) {
        Optional<ValidationInstance> optValidationInstance = validationInstanceRepo.findById(validationInstanceId);
        if (!optValidationInstance.isPresent()) {
            throw new RuntimeException("Validation instance not found");
        }

        ValidationInstance validationInstance = optValidationInstance.get();
        NodeInstance nodeInstance = validationInstance.getNodeInstance();
        WorkflowInstance workflowInstance = nodeInstance.getWorkflowInstance();

        validationInstance.setStatus(decision);
        validationInstance.setDecision(decision.name());
        validationInstance.setDecisionBy(decisionBy);
        validationInstance.setDecidedAt(LocalDateTime.now());
        validationInstance.setComments(comments);

        ValidationInstance savedValidationInstance = validationInstanceRepo.save(validationInstance);

        List<ValidationInstance> validations = validationInstanceRepo.findByNodeInstanceId(nodeInstance.getId());
        boolean allComplete = validations.stream()
                .allMatch(v -> v.getStatus() != ValidationStatus.PENDING);

        if (allComplete) {
            boolean anyRejected = validations.stream()
                    .anyMatch(v -> v.getStatus() == ValidationStatus.REJECTED);

            if (anyRejected) {
                nodeInstance.setStatus(NodeInstanceStatus.ERROR);
            } else {
                nodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            }

            nodeInstance.setCompletedAt(LocalDateTime.now());
            nodeInstanceRepo.save(nodeInstance);

            advanceWorkflow(workflowInstance);
        }

        return savedValidationInstance;
    }

    /**
     * Retrieves all pending validations for a specific role.
     * @param role The role to filter validations by.
     * @return List of pending validation instances.
     */
    public List<ValidationInstance> getPendingValidations(String role) {
        return validationInstanceRepo.findByRoleAndStatus(role, ValidationStatus.PENDING);
    }

    /**
     * Retrieves all validations assigned to a specific user.
     * @param userId The user ID to filter validations by.
     * @return List of validation instances assigned to the user.
     */
    public List<ValidationInstance> getValidationsAssignedToUser(String userId) {
        return validationInstanceRepo.findByAssignedUser(userId);
    }

    /**
     * Retrieves all node types.
     * @return List of node types.
     */
    public List<NodeType> getNodeTypes() {
        return Arrays.asList(NodeType.values());
    }

    /**
     * Processes node validations by cloning them from the original node to the saved node.
     * @param originalNode The original workflow node.
     * @param savedNode The saved workflow node.
     */
    @Transactional
    void processNodeValidations(WorkflowNode originalNode, WorkflowNode savedNode) {
        if (originalNode.getValidations() != null && !originalNode.getValidations().isEmpty()) {
            for (NodeValidation validation : originalNode.getValidations()) {
                NodeValidation newValidation = new NodeValidation();
                newValidation.setName(validation.getName());
                newValidation.setAssignedRole(validation.getAssignedRole());
                newValidation.setAssignedUser(validation.getAssignedUser());
                newValidation.setSeverity(validation.getSeverity() != null ? validation.getSeverity() : ValidationSeverity.MEDIUM);

                if (validation.getParameters() != null) {
                    ValidationParameter params = new ValidationParameter();
                    params.setDescription(validation.getParameters().getDescription());
                    params.setRequired(validation.getParameters().isRequired());
                    params.setTimeoutMinutes(validation.getParameters().getTimeoutMinutes());
                    params.setFormTemplate(validation.getParameters().getFormTemplate());
                    params.setValidationType(validation.getParameters().getValidationType());
                    newValidation.setParameters(params);
                } else {
                    ValidationParameter params = new ValidationParameter();
                    params.setRequired(true);
                    params.setTimeoutMinutes(1440);
                    newValidation.setParameters(params);
                }

                newValidation.setNode(savedNode);
                savedNode.getValidations().add(newValidation);
            }
        }
    }

    /**
     * Retrieves a workflow node by its ID.
     * @param nodeId The ID of the workflow node.
     * @return Optional containing the workflow node if found.
     */
    public Optional<WorkflowNode> getWorkflowNodeById(String nodeId) {
        return workflowNodeRepo.findById(nodeId);
    }

    /**
     * Adds a validation to a workflow node.
     * @param nodeId The ID of the workflow node.
     * @param validation The validation to add.
     * @return The added validation.
     */
 // WorkflowService.java
    @Transactional
    public NodeValidation addNodeValidation(
        String nodeId, 
        String name, 
        ValidationSeverity severity,
        Integer roleId,
        Long userId,
        String description,
        boolean required,
        Integer timeoutMinutes,
        String formTemplate,
        String validationType) {

        // Récupération du nœud
        WorkflowNode node = workflowNodeRepository.findById(nodeId)
            .orElseThrow(() -> new RuntimeException("Node non trouvé"));

        // Création des paramètres
        ValidationParameter parameters = new ValidationParameter();
        parameters.setDescription(description);
        parameters.setRequired(required);
        parameters.setTimeoutMinutes(timeoutMinutes);
        parameters.setFormTemplate(formTemplate);
        parameters.setValidationType(validationType);

        // Création de la validation
        NodeValidation validation = new NodeValidation();
        validation.setName(name);
        validation.setSeverity(severity);
        validation.setParameters(parameters); // 👈 Paramètres assignés

        // Assignation rôle/utilisateur
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
            validation.setAssignedRole(role);

            if (userId != null) {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                if (!user.getRoles().contains(role)) {
                    throw new RuntimeException("L'utilisateur n'appartient pas au rôle");
                }
                validation.setAssignedUser(user);
            }
        }

        // Liaison au nœud et sauvegarde
        validation.setNode(node);
        return nodeValidationRepository.save(validation); // 👈 Sauvegarde directe
    }
    /**
     * Updates a validation on a workflow node.
     * @param nodeId The ID of the workflow node.
     * @param validation The validation to update.
     * @return The updated validation.
     */
    @Transactional
    public NodeValidation updateNodeValidation(String nodeId, NodeValidation validation) {
        Optional<WorkflowNode> nodeOpt = workflowNodeRepo.findById(nodeId);
        if (!nodeOpt.isPresent()) {
            throw new RuntimeException("Workflow node not found: " + nodeId);
        }

        WorkflowNode node = nodeOpt.get();

        NodeValidation existingValidation = node.getValidations().stream()
                .filter(v -> v.getId().equals(validation.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Validation not found: " + validation.getId()));

        existingValidation.setName(validation.getName());
        existingValidation.setAssignedRole(validation.getAssignedRole());
        existingValidation.setAssignedUser(validation.getAssignedUser());
        existingValidation.setSeverity(validation.getSeverity());

        if (validation.getParameters() != null) {
            if (existingValidation.getParameters() == null) {
                existingValidation.setParameters(new ValidationParameter());
            }
            ValidationParameter params = existingValidation.getParameters();
            params.setDescription(validation.getParameters().getDescription());
            params.setRequired(validation.getParameters().isRequired());
            params.setTimeoutMinutes(validation.getParameters().getTimeoutMinutes());
            params.setFormTemplate(validation.getParameters().getFormTemplate());
            params.setValidationType(validation.getParameters().getValidationType());
        }

        workflowNodeRepo.save(node);
        return existingValidation;
    }

    /**
     * Deletes a validation from a workflow node.
     * @param nodeId The ID of the workflow node.
     * @param validationId The ID of the validation to delete.
     */
    @Transactional
    public void deleteNodeValidation(String nodeId, String validationId) {
        Optional<WorkflowNode> nodeOpt = workflowNodeRepo.findById(nodeId);
        if (!nodeOpt.isPresent()) {
            throw new RuntimeException("Workflow node not found: " + nodeId);
        }

        WorkflowNode node = nodeOpt.get();

        NodeValidation validationToRemove = node.getValidations().stream()
                .filter(v -> v.getId().equals(validationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Validation not found: " + validationId));

        node.getValidations().remove(validationToRemove);
        workflowNodeRepo.save(node);
    }

    /**
     * Retrieves the latest version of a workflow definition by ID or key.
     * @param idOrKey The ID or key of the workflow definition.
     * @return Optional containing the latest workflow definition if found.
     */
    public Optional<WorkflowDefinition> getLatestVersionByIdOrKey(String idOrKey) {
        Optional<WorkflowDefinition> byId = workflowDefinitionRepo.findById(idOrKey);
        if (byId.isPresent()) {
            String key = byId.get().getKey();
            return workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(key)
                    .stream()
                    .findFirst();
        } else {
            return workflowDefinitionRepo.findByKeyOrderByCreatedAtDesc(idOrKey)
                    .stream()
                    .findFirst();
        }
    }

    /**
     * Retrieves the latest versions of all workflow definitions.
     * @return List of the latest workflow definitions.
     */
    public List<WorkflowDefinition> getLatestWorkflowDefinitions() {
        List<String> uniqueKeys = workflowDefinitionRepo.findAll().stream()
                .map(WorkflowDefinition::getKey)
                .distinct()
                .collect(Collectors.toList());

        List<WorkflowDefinition> latestDefinitions = new ArrayList<>();
        for (String key : uniqueKeys) {
            Optional<WorkflowDefinition> latestVersion = getLatestVersionByIdOrKey(key);
            latestVersion.ifPresent(latestDefinitions::add);
        }

        return latestDefinitions;
    }

    /**
     * Retrieves all workflow instances.
     * @return List of all workflow instances.
     */
    public List<WorkflowInstance> getAllWorkflowInstances() {
        return workflowInstanceRepo.findAll();
    }

    /**
     * Retrieves a workflow instance by its ID.
     * @param id The ID of the workflow instance.
     * @return Optional containing the workflow instance if found.
     */
    public Optional<WorkflowInstance> getWorkflowInstanceById(String id) {
        return workflowInstanceRepo.findById(id);
    }


    /**
     * Retrieves all workflow instances by definition ID.
     * @param definitionId The ID of the workflow definition.
     * @return List of workflow instances for the definition.
     */
    public List<WorkflowInstance> getWorkflowInstancesByDefinitionId(String definitionId) {
        return workflowInstanceRepo.findByWorkflowDefinitionId(definitionId);
    }
    

    
    
    
    /*public WorkflowDefinition addTypesToWorkflow(String id, List<TypeStage> types) {
        WorkflowDefinition workflow = workflowDefinitionRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

        workflow.getTypeStages().addAll(types);
        return workflowDefinitionRepo.save(workflow);
    }

    public List<TypeStage> getTypesByWorkflow(String id) {
        WorkflowDefinition workflow = workflowDefinitionRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

        return workflow.getTypeStages();
    }

    public void removeTypeFromWorkflow(String id, String typeId) {
        WorkflowDefinition workflow = workflowDefinitionRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

        workflow.getTypeStages().removeIf(type -> type.getId().equals(typeId));
        workflowDefinitionRepo.save(workflow);
    }*/
}