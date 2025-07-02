package safran.pfe.entities.DTO;
public class EvaluationStageDTO {
    private Long id;
    private Long candidatureId;
    private TypeEvaluationDTO typeEvaluation; // AJOUT: Inclure le type d'évaluation

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCandidatureId() {
		return candidatureId;
	}
	public void setCandidatureId(Long candidatureId) {
		this.candidatureId = candidatureId;
	}
	 public TypeEvaluationDTO getTypeEvaluation() { return typeEvaluation; }
	    public void setTypeEvaluation(TypeEvaluationDTO typeEvaluation) { this.typeEvaluation = typeEvaluation; }
}