package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter @Setter
public class FicheEvalTech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = true) // Permettre null
    private Statut statut;

    @OneToMany(mappedBy = "ficheTech", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FicheTechCompetence> competences = new ArrayList<>();
    
    
 
	@Column(name = "total_score_tech")
    private Double scoreTotalTech; 
    
    
	@PrePersist
	@PreUpdate
	@PostLoad
	public void calculateScoreTech() {
	    if (competences != null && !competences.isEmpty()) {
	        this.scoreTotalTech = competences.stream()
	            .mapToDouble(FicheTechCompetence::getScoreCompetenceTech)
	            .average()
	            .orElse(0.0);
	    } else {
	        this.scoreTotalTech = 0.0;
	    }
	    System.out.println("Score technique calculé: " + this.scoreTotalTech); // Log de débogage
	}
    
    



	public List<FicheTechCompetence> getCompetences() {
		return competences;
	}


	public void setCompetences(List<FicheTechCompetence> competences) {
		this.competences = competences;
	}





	

	public Double getScoreTotalTech() {
		return scoreTotalTech;
	}



	public void setScoreTotalTech(Double scoreTotalTech) {
		this.scoreTotalTech = scoreTotalTech;
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
    name = "fiche_tech_competence",
    joinColumns = @JoinColumn(name = "fiche_id"),
    inverseJoinColumns = @JoinColumn(name = "competence_id")
)
private List<ExigenceTechnique> competences;
public List<ExigenceTechnique> getCompetences() {
	return competences;
}

public void setCompetences(List<ExigenceTechnique> competences) {
	this.competences = competences;
}*/