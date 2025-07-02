package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.CompetenceComportementale;

@Repository
public interface CompetenceComportementaleRepository extends  JpaRepository<CompetenceComportementale, Long> {

	  Optional<CompetenceComportementale> findByNomCompetence(String nomCompetence);
}
