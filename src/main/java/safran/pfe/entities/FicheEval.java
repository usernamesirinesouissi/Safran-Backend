package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FicheEval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
    @JoinColumn(name = "fiche_rh_id")
    private FicheEvalRh ficheRh = new FicheEvalRh(); 
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
    @JoinColumn(name = "fiche_tech_id")
    private FicheEvalTech ficheTech = new FicheEvalTech();
    
    @ManyToOne
    @JoinColumn(name = "statut_id")
    private Statut statut;
    
  
    @OneToOne(mappedBy = "ficheEvalAssociee", fetch = FetchType.LAZY)
    @JsonBackReference("entretien-ficheeval")
    private Entretien entretienSpecifique;
    
    
    
    
    @ManyToOne
    @JoinColumn(name = "statut_global_id")
    private Statut statutGlobal;
    
    
    
    /*@OneToMany(mappedBy = "ficheEvalOrigine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ficheeval-evaluationstage")
    private List<EvaluationStage> evaluationsDeStage = new ArrayList<>();
    
    
    
    public List<EvaluationStage> getEvaluationsDeStage() {
		return evaluationsDeStage;
	}

	public void setEvaluationsDeStage(List<EvaluationStage> evaluationsDeStage) {
		this.evaluationsDeStage = evaluationsDeStage;
	}*/

	public Statut getStatutGlobal() {
		return statutGlobal;
	}

	public void setStatutGlobal(Statut statutGlobal) {
		this.statutGlobal = statutGlobal;
	}

	public Entretien getEntretienSpecifique() {
		return entretienSpecifique;
	}

	public void setEntretienSpecifique(Entretien entretienSpecifique) {
		this.entretienSpecifique = entretienSpecifique;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	@Column(name = "score_total")
	private Double scoreTotal;

    
    /*@PostLoad
    public void calculateTotalScore() {
        double scoreRh = ficheRh != null ? ficheRh.getScoreTotalRh() : 0.0;
        double scoreTech = ficheTech != null ? ficheTech.getScoreTotalTech() : 0.0;
        this.scoreTotal = (scoreRh + scoreTech) / 2.0;
    }*/


	@PrePersist
	@PreUpdate
	public void calculateTotalScore() {
        System.out.println("Calcul du score total déclenché !");
	    
	    double tempRhScore = 0.0;
	    if (this.ficheRh != null && this.ficheRh.getScoreTotalRh() != null) {
	        tempRhScore = this.ficheRh.getScoreTotalRh();
	        System.out.println("Score RH trouvé: " + tempRhScore);
	    } else {
	        System.out.println("Score RH non trouvé ou null");
	    }

	    double tempTechScore = 0.0;
	    if (this.ficheTech != null && this.ficheTech.getScoreTotalTech() != null) {
	        tempTechScore = this.ficheTech.getScoreTotalTech();
	        System.out.println("Score Tech trouvé: " + tempTechScore);
	    } else {
	        System.out.println("Score Tech non trouvé ou null");
	    }

	    double newScore = (tempRhScore + tempTechScore) / 2.0;
	    System.out.println("Nouveau score total calculé: " + newScore);
	    
	    this.setScoreTotal(newScore);
	    System.out.println("Score total après set: " + this.scoreTotal);
	}



   


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

	public FicheEvalTech getFicheTech() {
		return ficheTech;
	}

	public void setFicheTech(FicheEvalTech ficheTech) {
		this.ficheTech = ficheTech;
	}

	public Double getScoreTotal() {
		return scoreTotal;
	}

	public void setScoreTotal(Double scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
    
    
}