package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.ParametrageWorkflow;

@Repository
public interface ParametrageRepository extends JpaRepository<ParametrageWorkflow, Long> {
    Optional<ParametrageWorkflow> findByNom(String nom);
    Optional<ParametrageWorkflow> findById(Long id);

}
