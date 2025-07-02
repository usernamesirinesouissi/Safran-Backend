package safran.pfe.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter @Setter
public class FicheRhCompetence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "fiche_rh_id")
    @JsonBackReference

    private FicheEvalRh ficheRh;
    
    @ManyToOne
    @JoinColumn(name = "competence_id")
    private CompetenceComportementale competence;
    
    @Column(name = "score_competence_rh") 
    private double scoreCompetenceRh;
    
    
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FicheEvalRh getFicheRh() {
		return ficheRh;
	}

	public void setFicheRh(FicheEvalRh ficheRh) {
		this.ficheRh = ficheRh;
	}

	public CompetenceComportementale getCompetence() {
		return competence;
	}

	public void setCompetence(CompetenceComportementale competence) {
		this.competence = competence;
	}

	public double getScoreCompetenceRh() {
		return scoreCompetenceRh;
	}

	public void setScoreCompetenceRh(double scoreCompetence) {
	    this.scoreCompetenceRh = scoreCompetence; // Correction ici
	}
    
    
    
    
}