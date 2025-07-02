package safran.pfe.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
public class CompetenceComportementale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nomCompetence;
    
    @ManyToMany(mappedBy = "competencescomportementale")
    @JsonIgnore 
    private List<Demande> demandes;
    
    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FicheRhCompetence> fichesRh = new ArrayList<>();
    
    
    // Solution : Ignorer les références circulaires
    //@JsonIgnoreProperties("competence")
    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL)
    private List<EvaluationCompetenceRH> evaluations;

   
   /* @OneToMany(mappedBy = "competence")
    private Set<NiveauEvaluation> niveaux;
    
    
    
    
    public Set<NiveauEvaluation> getNiveaux() {
		return niveaux;
	}

	public void setNiveaux(Set<NiveauEvaluation> niveaux) {
		this.niveaux = niveaux;
	}*/

	public List<FicheRhCompetence> getFichesRh() {
		return fichesRh;
	}

	public void setFichesRh(List<FicheRhCompetence> fichesRh) {
		this.fichesRh = fichesRh;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	public String getNomCompetence() {
		return nomCompetence;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}
	
	
	public void setNomCompetence(String nomCompetence) {
		this.nomCompetence = nomCompetence;
	}

	
}




/*@ManyToMany(mappedBy = "competences")
@JsonIgnore 
private List<FicheEvalRh> fichesRh;*/

/*public List<FicheEvalRh> getFichesRh() {
	return fichesRh;
}

public void setFichesRh(List<FicheEvalRh> fichesRh) {
	this.fichesRh = fichesRh;
}*/

