package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FicheEvalRh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = true) // Permettre null
    private Statut statut;
    
    @OneToMany(mappedBy = "ficheRh", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference

    private List<FicheRhCompetence> competences = new ArrayList<>();
    
    private Double scoreTotalRh;
    
    
    @PrePersist
    @PreUpdate
    @PostLoad
    public void calculateScoreRh() {
        if (this.competences == null || this.competences.isEmpty()) {
            this.scoreTotalRh = 0.0;
            return;
        }
        
        this.scoreTotalRh = this.competences.stream()
            .mapToDouble(FicheRhCompetence::getScoreCompetenceRh)
            .average()
            .orElse(0.0);
        
        System.out.println("Score RH recalculé: " + this.scoreTotalRh); // Log pour débogage
    }

    
    
    
	public List<FicheRhCompetence> getCompetences() {
		return competences;
	}




	public void setCompetences(List<FicheRhCompetence> competences) {
		this.competences = competences;
	}




	public Double getScoreTotalRh() {
		return scoreTotalRh;
	}




	public void setScoreTotalRh(Double scoreTotalRh) {
		this.scoreTotalRh = scoreTotalRh;
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}
	

  
    
    
}


/*@ManyToMany
@JoinTable(
    name = "fiche_rh_competence",
    joinColumns = @JoinColumn(name = "fiche_id"),
    inverseJoinColumns = @JoinColumn(name = "competence_id")
)
private List<CompetenceComportementale> competences;*/


/*public List<CompetenceComportementale> getCompetences() {
	return competences;
}

public void setCompetences(List<CompetenceComportementale> competences) {
	this.competences = competences;
}*/