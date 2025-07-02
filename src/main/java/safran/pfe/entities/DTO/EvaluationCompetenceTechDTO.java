package safran.pfe.entities.DTO;

import safran.pfe.entities.EvaluationCompetenceTech;

public class EvaluationCompetenceTechDTO {
	  private Long id; // Add this
	    private Long evaluationId;
	    private ExigenceTechniqueDTO competence;
	    private NiveauEvaluationDTO niveau;
	    
	    // Constructeur vide
	    public EvaluationCompetenceTechDTO() {}

	    // CORRECTION : Le constructeur que JPQL va utiliser
	    public EvaluationCompetenceTechDTO(Long id, Long evaluationId, Long competenceId, String competenceNom, Long niveauId, String niveauNom) {
	        this.id = id;
	        this.evaluationId = evaluationId;
	        this.competence = new ExigenceTechniqueDTO(competenceId, competenceNom);
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
		public ExigenceTechniqueDTO getCompetence() {
			return competence;
		}
		public void setCompetence(ExigenceTechniqueDTO competence) {
			this.competence = competence;
		}
		public NiveauEvaluationDTO getNiveau() {
			return niveau;
		}
		public void setNiveau(NiveauEvaluationDTO niveau) {
			this.niveau = niveau;
		}

   
}