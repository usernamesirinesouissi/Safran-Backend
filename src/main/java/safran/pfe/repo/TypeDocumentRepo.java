package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import safran.pfe.entities.TypeDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDocumentRepo extends JpaRepository<TypeDocument, Long> {
 

}