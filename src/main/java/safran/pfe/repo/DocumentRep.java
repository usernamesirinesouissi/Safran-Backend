package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.Document;
@Repository
public interface DocumentRep extends JpaRepository<Document, Long> {

	   @Query("SELECT d FROM Document d " +
	           "LEFT JOIN FETCH d.candidature c " +
	           "LEFT JOIN FETCH c.candidat " +
	           "LEFT JOIN FETCH d.type")
	    List<Document> findAllWithCandidatureAndCandidat();
	   
	   
	   @Query("SELECT d FROM Document d " +
	           "LEFT JOIN FETCH d.candidature c " +
	           "LEFT JOIN FETCH c.candidat " +
	           "LEFT JOIN FETCH d.type " +
	           "WHERE d.id = :id")
	    Optional<Document> findByIdWithRelations(@Param("id") Long id);

}
