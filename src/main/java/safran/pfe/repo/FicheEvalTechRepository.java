package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import safran.pfe.entities.FicheEvalTech;

public interface FicheEvalTechRepository extends JpaRepository<FicheEvalTech, Long> {
}