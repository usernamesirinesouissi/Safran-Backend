package safran.pfe.entities.DTO;

//DTO pour les requêtes
public class AddCompetenceRequest {
 private Long competenceId;
 private Long niveauId;
 
 // Getters et setters
 public Long getCompetenceId() { return competenceId; }
 public void setCompetenceId(Long competenceId) { this.competenceId = competenceId; }
 public Long getNiveauId() { return niveauId; }
 public void setNiveauId(Long niveauId) { this.niveauId = niveauId; } }