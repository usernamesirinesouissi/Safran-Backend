package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.NodeValidation;

import java.util.List;

@Repository
public interface NodeValidationRepository extends JpaRepository<NodeValidation, String> {
    List<NodeValidation> findByAssignedRole(String role);
    List<NodeValidation> findByNodeId(String nodeId);
}