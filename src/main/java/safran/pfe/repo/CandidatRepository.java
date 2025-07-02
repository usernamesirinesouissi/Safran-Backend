package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.CompetenceComportementale;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
}
