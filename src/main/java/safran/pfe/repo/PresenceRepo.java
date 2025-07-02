package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Presence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepo extends JpaRepository<Presence, Long> {

    Optional<Presence> findByCandidatIdAndDatePresence(Long candidatId, LocalDate datePresence);


    List<Presence> findByCandidatIdOrderByDatePresenceAsc(Long candidatId);
}
