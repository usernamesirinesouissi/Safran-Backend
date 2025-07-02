package safran.pfe.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import safran.pfe.entities.DTO.ValidationWithDemandeDTO;
import safran.pfe.entities.Demande;
import safran.pfe.entities.NodeValidation;
import safran.pfe.entities.ValidationInstance;
import safran.pfe.entities.ValidationStatus;
import safran.pfe.repo.NodeValidationRepository;
import safran.pfe.repo.ValidationInstanceRepository;
import safran.pfe.repo.WorkflowNodeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Autowired
    private NodeValidationRepository nodeValidationRepository;

    @Autowired
    private WorkflowNodeRepository workflowNodeRepository;

    @Autowired
    private ValidationInstanceRepository validationInstanceRepository;

    public List<NodeValidation> getAllValidations() {
        return nodeValidationRepository.findAll();
    }

    public Optional<NodeValidation> getValidationById(String id) {
        return nodeValidationRepository.findById(id);
    }

    @Transactional
    public NodeValidation saveValidation(NodeValidation validation) {
        return nodeValidationRepository.save(validation);
    }

    @Transactional
    public void deleteValidation(String id) {
        nodeValidationRepository.deleteById(id);
    }

    public List<NodeValidation> getValidationsByRole(String role) {
        return nodeValidationRepository.findByAssignedRole(role);
    }

    public List<NodeValidation> getValidationsByNodeId(String nodeId) {
        return nodeValidationRepository.findByNodeId(nodeId);
    }
/////////////////////Validation Instance
public List<ValidationWithDemandeDTO> getValidationsForUser(String userId, ValidationStatus status) {

    List<ValidationInstance> validations = validationInstanceRepository.findByAssignedUserAndStatus(userId, status);

    return validations.stream()
            .map(this::mapToValidationWithDemandeDTO)
            .collect(Collectors.toList());
}
    public List<ValidationWithDemandeDTO> getValidationsByDemandeId(Long demandeId) {
        return validationInstanceRepository.findByDemandeId(demandeId).stream()
                .map(this::mapToValidationWithDemandeDTO)
                .collect(Collectors.toList());
    }
    private ValidationWithDemandeDTO mapToValidationWithDemandeDTO(ValidationInstance validation) {
        ValidationWithDemandeDTO dto = new ValidationWithDemandeDTO();
        dto.setValidationId(validation.getId());
        dto.setStatus(validation.getStatus().name());
        dto.setAssignedTo(validation.getAssignedUser());
        dto.setAssignedRole(validation.getAssignedRole());
        dto.setDecision(validation.getDecision());
        dto.setDecisionBy(validation.getDecisionBy());
        dto.setCreatedAt(validation.getCreatedAt());
        dto.setDecidedAt(validation.getDecidedAt());
        dto.setComments(validation.getComments());

        // Fetch and map Demande data
        Demande demande = validation.getNodeInstance().getWorkflowInstance().getDemande();
        dto.setDemandeId(demande.getId());
        dto.setIntituleProjet(demande.getIntituleProjet());
        dto.setDescriptionProjet(demande.getDescriptionProjet());
        dto.setRisquesProjet(demande.getRisquesProjet());
        dto.setObjectifsProjet(demande.getObjectifsProjet());
        dto.setDescriptionLivrables(demande.getDescriptionLivrables());
        dto.setEffortEstime(demande.getEffortEstime());
        dto.setDureeEstime(demande.getDureeEstime());
        dto.setNombreStagiaires(demande.getNombreStagiaires());
        dto.setTypeStage(demande.getTypeStage().getNomTypeStage());
        dto.setStatut(demande.getStatut().getNomStatut());

        return dto;
    }
}