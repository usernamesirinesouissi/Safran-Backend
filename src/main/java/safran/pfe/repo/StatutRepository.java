package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Statut;
import java.util.Optional;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Long> {
    Optional<Statut> findByNomStatut(String nomStatut);

    
    
}