package safran.pfe.entities;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "niveaux_evaluation")
public class NiveauEvaluation {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nomNiveau;

	   
	    @OneToMany(mappedBy = "niveau")
	    private Set<EvaluationCompetenceRH> evaluationsRH;

	    @OneToMany(mappedBy = "niveau")
	    private Set<EvaluationCompetenceTech> evaluationsTech;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		

		public String getNomNiveau() {
			return nomNiveau;
		}

		public void setNomNiveau(String nomNiveau) {
			this.nomNiveau = nomNiveau;
		}

		public Set<EvaluationCompetenceRH> getEvaluationsRH() {
			return evaluationsRH;
		}

		public void setEvaluationsRH(Set<EvaluationCompetenceRH> evaluationsRH) {
			this.evaluationsRH = evaluationsRH;
		}

		public Set<EvaluationCompetenceTech> getEvaluationsTech() {
			return evaluationsTech;
		}

		public void setEvaluationsTech(Set<EvaluationCompetenceTech> evaluationsTech) {
			this.evaluationsTech = evaluationsTech;
		}

	
		
	    
	    
	    
	    
	} 