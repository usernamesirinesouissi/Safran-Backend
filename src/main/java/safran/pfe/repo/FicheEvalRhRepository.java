package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.FicheEvalRh;
public interface FicheEvalRhRepository extends JpaRepository<FicheEvalRh, Long> {
    @Modifying
    @Query("SELECT f FROM FicheEvalRh f LEFT JOIN FETCH f.competences WHERE f.id = :id")
    FicheEvalRh findByIdWithCompetences(@Param("id") Long id);
    
    
    
    

   
}