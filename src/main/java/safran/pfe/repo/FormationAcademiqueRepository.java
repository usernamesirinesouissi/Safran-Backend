package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.CompetenceComportementale;
import safran.pfe.entities.FormationAcademique;
@Repository
public interface FormationAcademiqueRepository extends  JpaRepository<FormationAcademique, Long> {

	 Optional<FormationAcademique> findByNomFormation(String nomFormation);

	

}
