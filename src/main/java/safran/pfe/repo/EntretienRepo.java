package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Entretien;

import java.util.List;

@Repository
public interface EntretienRepo extends JpaRepository<Entretien,Long> {
    List<Entretien> findByCandidature_Candidat_Id(Long candidatId);

}
