package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.ExigenceTechnique;
@Repository
public interface ExigenceTechniqueRepository extends JpaRepository<ExigenceTechnique, Long> {

	Optional<ExigenceTechnique> findByNomExigence(String nomExigence);
}
