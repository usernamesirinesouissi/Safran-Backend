package safran.pfe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Demande;
import safran.pfe.entities.TypeStage;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
	List<Demande> findByTypeStage(TypeStage typeStage);
	
	    List<Demande> findByStatutId(Long statutId);

}