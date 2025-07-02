package safran.pfe.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.FicheEval;
public interface FicheEvalRepository extends JpaRepository<FicheEval, Long> {

    @Query("SELECT f FROM FicheEval f LEFT JOIN FETCH f.ficheRh LEFT JOIN FETCH f.ficheTech WHERE f.id = :id")
    FicheEval findByIdWithDetails(@Param("id") Long id);
    
    Optional<FicheEval> findByEntretienSpecifiqueId(Long entretienId);
    
}