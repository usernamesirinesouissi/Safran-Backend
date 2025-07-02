package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.PropositionEcole;
@Repository
public interface PropositionEcoleRepository extends JpaRepository<PropositionEcole, Long> {
	  Optional<PropositionEcole> findByNomEcole(String nomEcole);

}
