package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.EvaluationCompetenceRH;
import safran.pfe.entities.EvaluationCompetenceTech;
import safran.pfe.entities.DTO.EvaluationCompetenceTechDTO;
import org.springframework.stereotype.Repository;


@Repository
public interface EvaluationCompetenceTechRepository extends JpaRepository<EvaluationCompetenceTech, Long>{
	
	    //List<EvaluationCompetenceTechDTO> findDtosByEvaluationId(@Param("evaluationId") Long evaluationId);
	
	@Query("SELECT new safran.pfe.entities.DTO.EvaluationCompetenceTechDTO(" +
    "ect.id, " +
    "ect.evaluation.id, " +
    "ect.competence.id, ect.competence.nomExigence, " + // Assumer le nom de champ "nom"
    "ect.niveau.id, ect.niveau.nomNiveau" + // Assumer les noms de champs
    ") " +
    "FROM EvaluationCompetenceTech ect " +
    "WHERE ect.evaluation.id = :evaluationId")
List<EvaluationCompetenceTechDTO> findDtosByEvaluationId(@Param("evaluationId") Long evaluationId);
	
	Optional<EvaluationCompetenceTech> findByEvaluationIdAndCompetenceId(Long evaluationId, Long competenceId);
}
