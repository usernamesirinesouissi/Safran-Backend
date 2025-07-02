package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.EvaluationCompetenceRH;
import safran.pfe.entities.DTO.EvaluationCompetenceRHDTO;
import safran.pfe.entities.DTO.EvaluationCompetenceTechDTO;
import org.springframework.stereotype.Repository;


@Repository
public interface EvaluationCompetenceRHRepository extends JpaRepository<EvaluationCompetenceRH, Long>{
	
	    //List<EvaluationCompetenceRHDTO> findDtosByEvaluationId(@Param("evaluationId") Long evaluationId);
	    
@Query("SELECT new safran.pfe.entities.DTO.EvaluationCompetenceRHDTO(" +
           "ecr.id, " +
           "ecr.evaluation.id, " +
           "ecr.competence.id, ecr.competence.nomCompetence, " + // Assumer les noms de champs
           "ecr.niveau.id, ecr.niveau.nomNiveau" + // Assumer les noms de champs
           ") " +
           "FROM EvaluationCompetenceRH ecr " +
           "WHERE ecr.evaluation.id = :evaluationId")
    List<EvaluationCompetenceRHDTO> findDtosByEvaluationId(@Param("evaluationId") Long evaluationId); 


Optional<EvaluationCompetenceRH> findByEvaluationIdAndCompetenceId(Long evaluationId, Long competenceId);
}
