package safran.pfe.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.ValidationInstance;
import safran.pfe.entities.ValidationStatus;

import java.util.List;
@Repository
public interface ValidationInstanceRepository extends JpaRepository<ValidationInstance, String> {

    List<ValidationInstance> findByAssignedUser(String user);
    
    @Query("SELECT v FROM ValidationInstance v WHERE v.assignedUser = :user AND v.status = :status")
    List<ValidationInstance> findByUserAndStatus(@Param("user") String user, @Param("status") ValidationStatus status);
    
    List<ValidationInstance> findByNodeInstanceId(String nodeInstanceId);
    
    @Query("SELECT v FROM ValidationInstance v WHERE v.nodeInstance.workflowInstance.demande.id = :demandeId")
    List<ValidationInstance> findByDemandeId(@Param("demandeId") Long demandeId);

    List<ValidationInstance> findByStatus(ValidationStatus status);

    List<ValidationInstance> findByAssignedRole(String assignedRole);

    @Query("SELECT v FROM ValidationInstance v WHERE v.assignedRole = :role AND v.status = :status")
    List<ValidationInstance> findByRoleAndStatus(@Param("role") String role, @Param("status") ValidationStatus status);

    // Nouvelles méthodes de recherche combinée
    List<ValidationInstance> findByAssignedUserAndStatus(String assignedUser, ValidationStatus status);
    List<ValidationInstance> findByAssignedRoleAndStatus(String assignedRole, ValidationStatus status);

    // Supprimer l'ancienne méthode problématique
    // List<ValidationInstance> findByAssignedToAndStatus(String userId, ValidationStatus status); ❌
}