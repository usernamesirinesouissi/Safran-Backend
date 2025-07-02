package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.StatutCandidature;

@Repository
public interface StatutCandidatureRepository extends JpaRepository<StatutCandidature, Long> {
}
