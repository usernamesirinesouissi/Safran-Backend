package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import safran.pfe.entities.WorkflowConnection;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WorkflowConnectionRepository extends JpaRepository<WorkflowConnection, String> {

    Optional<WorkflowConnection> findById(String id);

    List<WorkflowConnection> findByWorkflowDefinitionId(String workflowDefinitionId);


    @Modifying
    @Transactional
    void deleteByWorkflowDefinitionId(String workflowDefinitionId);
}