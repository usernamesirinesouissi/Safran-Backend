package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.Candidature;
import safran.pfe.entities.CompetenceComportementale;
import safran.pfe.entities.DTO.CandidatureDTOList;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    boolean existsByCandidatIdAndDemandeId(Long candidatId, Long demandeId);

    List<Candidature> findByCandidat(Candidat candidat);
    List<Candidature> findByStatutCandidatureId(Long statutId);
    
  
    @Query("SELECT DISTINCT c FROM Candidature c " +
            "JOIN FETCH c.entretiens e " +
            "JOIN FETCH e.ficheEvalAssociee fe " +
            "JOIN FETCH fe.statutGlobal sg " +
            "JOIN FETCH c.candidat " +
            "LEFT JOIN FETCH c.demande " +
            "WHERE sg.id = :statutGlobalId")
     List<Candidature> findAllWithStatutGlobal(@Param("statutGlobalId") Long statutGlobalId);
    
    
    @Query("SELECT new safran.pfe.entities.DTO.CandidatureDTOList(" +
            "c.id, c.dateDepot, c.score, " +
            "c.candidat.id, c.candidat.nomCandidat, c.candidat.prenomCandidat, c.candidat.photoPath, " +
            "c.demande.id, c.demande.reference, c.demande.intituleProjet, c.demande.typeStage.nomTypeStage, " +
            "c.statutCandidature.id, c.statutCandidature.nom" +
            ") FROM Candidature c")
     List<CandidatureDTOList> findAllAsDTO();
   
    
    
    
}
