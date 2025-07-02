package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import safran.pfe.entities.UO;

import java.util.Optional;

public interface UORepository extends JpaRepository<UO, Long> {
    Optional<UO> findByNomUO(String nomUO);
}
