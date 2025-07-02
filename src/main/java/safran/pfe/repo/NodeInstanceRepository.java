package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.NodeInstance;

import java.util.List;

@Repository
public interface NodeInstanceRepository extends JpaRepository<NodeInstance, String> {
    List<NodeInstance> findByWorkflowInstanceId(String workflowInstanceId);

}
