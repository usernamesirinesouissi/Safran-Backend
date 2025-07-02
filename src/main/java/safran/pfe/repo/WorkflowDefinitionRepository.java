package safran.pfe.repo;


import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.WorkflowDefinition;
import safran.pfe.entities.WorkflowStatus;




@Repository
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, String> {
    Optional<WorkflowDefinition> findByKey(String key);
    List<WorkflowDefinition> findByStatus(WorkflowStatus status);

    List<WorkflowDefinition> findByKeyOrderByCreatedAtDesc(String key);


    List<WorkflowDefinition> findByKeyAndStatus(String key, WorkflowStatus status);

    Optional<WorkflowDefinition> findByKeyAndVersion(String key, String version);


}

