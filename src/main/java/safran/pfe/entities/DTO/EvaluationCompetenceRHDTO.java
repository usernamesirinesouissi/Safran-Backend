package safran.pfe.entities.DTO;

import safran.pfe.entities.EvaluationCompetenceRH;

public class EvaluationCompetenceRHDTO {
	private Long id; // Add this
    private Long evaluationId;
    private CompetenceComportementaleDTO competence;
    private NiveauEvaluationDTO niveau;
    
    // Constructeur vide (bonne pratique pour la sérialisation/désérialisation)
    public EvaluationCompetenceRHDTO() {}

    // CORRECTION : Le constructeur que JPQL va utiliser
    public EvaluationCompetenceRHDTO(Long id, Long evaluationId, Long competenceId, String competenceNom, Long niveauId, String niveauNom) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.competence = new CompetenceComportementaleDTO(competenceId, competenceNom);
        this.niveau = new NiveauEvaluationDTO(niveauId, niveauNom);
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(Long evaluationId) {
		this.evaluationId = evaluationId;
	}
	public CompetenceComportementaleDTO getCompetence() {
		return competence;
	}
	public void setCompetence(CompetenceComportementaleDTO competence) {
		this.competence = competence;
	}
	public NiveauEvaluationDTO getNiveau() {
		return niveau;
	}
	public void setNiveau(NiveauEvaluationDTO niveau) {
		this.niveau = niveau;
	}

   
}