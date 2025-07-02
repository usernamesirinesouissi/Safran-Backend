package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TypeEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nomTypeEval;
    
    @OneToMany(mappedBy = "typeEvaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EvaluationStage> evaluationsStage = new ArrayList<>(); // Nom au pluriel
    
    
	public List<EvaluationStage> getEvaluationsStage() {
		return evaluationsStage;
	}

	public void setEvaluationsStage(List<EvaluationStage> evaluationsStage) {
		this.evaluationsStage = evaluationsStage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomTypeEval() {
		return nomTypeEval;
	}

	public void setNomTypeEval(String nomTypeEval) {
		this.nomTypeEval = nomTypeEval;
	} 
    
}