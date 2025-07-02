package safran.pfe.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name = "evaluation_competence_tech")
public class EvaluationCompetenceTech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    //@JsonBackReference("evaluation-tech") // Must match the name in @JsonManagedReference
    private EvaluationStage evaluation;
 
  
    
    @ManyToOne(fetch = FetchType.LAZY)
    //@JsonIgnoreProperties("evaluations")
    @JoinColumn(name = "competencetech_id", nullable = false) 
    private ExigenceTechnique competence;

    //@JsonIgnoreProperties({"evaluationsRH", "evaluationsTech"})
    @ManyToOne
    @JoinColumn(name = "niveau_id")
    private NiveauEvaluation niveau;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EvaluationStage getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(EvaluationStage evaluation) {
		this.evaluation = evaluation;
	}

	public ExigenceTechnique getCompetence() {
		return competence;
	}

	public void setCompetence(ExigenceTechnique competence) {
		this.competence = competence;
	}

	public NiveauEvaluation getNiveau() {
		return niveau;
	}

	public void setNiveau(NiveauEvaluation niveau) {
		this.niveau = niveau;
	}
    
    

}