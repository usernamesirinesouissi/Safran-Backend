package safran.pfe.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class EvaluationStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidature_id")
    //@JsonIgnoreProperties("evaluationsStage")
    private Candidature candidature;

    @ManyToOne
    @JoinColumn(name = "type_evaluation_id")
    private TypeEvaluation typeEvaluation;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference("evaluation-rh")
    private Set<EvaluationCompetenceRH> competencesRH = new HashSet<>();

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference("evaluation-tech")
    private Set<EvaluationCompetenceTech> competencesTech = new HashSet<>();


	public TypeEvaluation getTypeEvaluation() {
		return typeEvaluation;
	}

	public void setTypeEvaluation(TypeEvaluation typeEvaluation) {
		this.typeEvaluation = typeEvaluation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Candidature getCandidature() {
		return candidature;
	}

	public void setCandidature(Candidature candidature) {
		this.candidature = candidature;
	}

	public Set<EvaluationCompetenceRH> getCompetencesRH() {
		return competencesRH;
	}

	public void setCompetencesRH(Set<EvaluationCompetenceRH> competencesRH) {
		this.competencesRH = competencesRH;
	}

	public Set<EvaluationCompetenceTech> getCompetencesTech() {
		return competencesTech;
	}

	public void setCompetencesTech(Set<EvaluationCompetenceTech> competencesTech) {
		this.competencesTech = competencesTech;
	}
	
	

}