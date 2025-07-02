package safran.pfe.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.NodeType;
import safran.pfe.entities.WorkflowNode;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, String> {
    List<WorkflowNode> findByWorkflowDefinitionId(String workflowDefinitionId);

    List<WorkflowNode> findByWorkflowDefinitionIdAndType(String workflowDefinitionId, NodeType type);

    @Modifying
    @Transactional
    void deleteByWorkflowDefinitionId(String workflowDefinitionId);
}
