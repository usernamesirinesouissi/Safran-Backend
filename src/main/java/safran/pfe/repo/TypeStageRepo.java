package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.TypeStage;
import java.util.Optional;

@Repository
public interface TypeStageRepo extends JpaRepository<TypeStage, Long> {
    Optional<TypeStage> findByNomTypeStage(String nomTypeStage);
}