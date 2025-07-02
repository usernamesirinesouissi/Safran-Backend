package safran.pfe.entities.DTO;

public class EvaluationStageCompetenceDTO {
    private Long competenceComportementaleId; // ID de la CompetenceComportementale
    private Long niveauEvaluationId;        // ID du NiveauEvaluation
  

    // Getters and Setters
    public Long getCompetenceComportementaleId() {
        return competenceComportementaleId;
    }

    public void setCompetenceComportementaleId(Long competenceComportementaleId) {
        this.competenceComportementaleId = competenceComportementaleId;
    }

    public Long getNiveauEvaluationId() {
        return niveauEvaluationId;
    }

    public void setNiveauEvaluationId(Long niveauEvaluationId) {
        this.niveauEvaluationId = niveauEvaluationId;
    }

  
}