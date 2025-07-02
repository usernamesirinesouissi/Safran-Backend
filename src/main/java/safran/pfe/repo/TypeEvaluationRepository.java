package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import safran.pfe.entities.TypeEvaluation;

public interface TypeEvaluationRepository extends JpaRepository<TypeEvaluation, Long> {

  Optional<TypeEvaluation> findByNomTypeEval(String nomTypeEval);
   
}