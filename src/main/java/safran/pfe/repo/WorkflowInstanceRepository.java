package safran.pfe.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.User;
import safran.pfe.entities.WorkflowInstance;
import safran.pfe.entities.WorkflowInstanceStatus;

import java.util.List;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, String> {
	
	//List<WorkflowInstance> findByAssignedUserId(String UserId);
	
    List<WorkflowInstance> findByStatus(WorkflowInstanceStatus status);

    List<WorkflowInstance> findByWorkflowDefinitionId(String workflowDefinitionId);

    List<WorkflowInstance> findByWorkflowDefinitionIdIn(List<String> workflowDefinitionIds);

    List<WorkflowInstance> findByWorkflowDefinitionIdAndStatusIn(
            String workflowDefinitionId,
            List<WorkflowInstanceStatus> statuses
    );

    List<WorkflowInstance> findByWorkflowDefinitionIdInAndStatusIn(
            List<String> workflowDefinitionIds,
            List<WorkflowInstanceStatus> statuses
    );

    List<WorkflowInstance> findByDefinitionVersion(String definitionVersion);
    List<WorkflowInstance> findByInitiatedBy(String initiatedBy);
    
    
    List<WorkflowInstance> findByAssignedUser(User user);
    
    // Ou si vous voulez chercher par ID utilisateur :
    @Query("SELECT wi FROM WorkflowInstance wi WHERE wi.assignedUser.id = :userId")
    List<WorkflowInstance> findByAssignedUserId(@Param("userId") String userId);
}
