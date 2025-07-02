package safran.pfe.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.EvaluationStage;

public interface EvaluationStageRepo extends JpaRepository<EvaluationStage, Long> {
    
    // Méthode pour charger une évaluation avec toutes ses relations
 
    @Query("SELECT e FROM EvaluationStage e WHERE e.id = :id")
    Optional<EvaluationStage> findByIdWithDetails(@Param("id") Long id);
    
    // Méthode générée automatiquement par Spring Data JPA
    boolean existsByCandidatureId(Long candidatureId);
    
    
    List<EvaluationStage> findAllByCandidatureId(Long candidatureId);
    
    
    //Optional<EvaluationStage> findByCandidatureId(Long candidatureId);
    Optional<EvaluationStage> findByCandidatureIdAndTypeEvaluationId(Long candidatureId, Long typeId);

    
   
}