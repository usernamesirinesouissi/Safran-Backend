package safran.pfe.entities;

import java.util.List;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Data
@Getter
@Setter
public class ExigenceTechnique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomExigence;
    
    
    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = true)
    private Statut statut;
    
    @ManyToMany(mappedBy = "competencestechnique")
    @JsonBackReference // ⬅️ Empêche la sérialisation des demandes

    private List<Demande> demandes;
    
    
    @OneToMany(mappedBy = "exigenceTechnique")
    @JsonIgnore
    private List<FicheTechCompetence> ficheTechCompetences;

    
    //@JsonIgnoreProperties("competence")
    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL)
    private List<EvaluationCompetenceTech> evaluations;

    public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	public List<FicheTechCompetence> getFicheTechCompetences() {
		return ficheTechCompetences;
	}

	public void setFicheTechCompetences(List<FicheTechCompetence> ficheTechCompetences) {
		this.ficheTechCompetences = ficheTechCompetences;
	}

	public String getNomExigence() {
        return this.nomExigence;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNomExigence(String nomExigence) {
        this.nomExigence = nomExigence;
    }


  

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}
	

}

/* @ManyToMany(mappedBy = "competences")
private List<FicheEvalTech> fichesTech;*/
/*public List<FicheEvalTech> getFichesTech() {
	return fichesTech;
}

public void setFichesTech(List<FicheEvalTech> fichesTech) {
	this.fichesTech = fichesTech;
}*/
