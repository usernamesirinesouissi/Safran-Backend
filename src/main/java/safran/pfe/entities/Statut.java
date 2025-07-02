package safran.pfe.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@JsonInclude(JsonInclude.Include.NON_NULL) // 
public class Statut {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String nomStatut;
	    
	 

		public List<Demande> getDemandes() {
			return demandes;
		}

		public void setDemandes(List<Demande> demandes) {
			this.demandes = demandes;
		}

		public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }
	    
	    
	    @OneToMany(mappedBy = "statut", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonIgnore
	    private List<Demande> demandes;


	    public String getNomStatut() {
	        return this.nomStatut; 
	    }

	    public void setNomStatut(String nomStatut) {
	        this.nomStatut = nomStatut; 
	    }
}
