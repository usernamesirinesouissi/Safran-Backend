package safran.pfe.entities;

import java.util.List;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity

@Table(name = "formation_academique")
public class FormationAcademique {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrémenté
	    private Long id;

	    private String nomFormation;

	    // Getters et setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getNomFormation() {
	        return nomFormation;
	    }

	    public void setNomFormation(String nomFormation) {
	        this.nomFormation = nomFormation;
	    }
    
    @ManyToMany(mappedBy = "formationacademique")
    private List<Demande> demandes;

 
    


}