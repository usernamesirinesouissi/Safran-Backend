package safran.pfe.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TypeStage {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomTypeStage;
	private String DureeStage;

	@OneToMany(mappedBy = "typeStage", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Demande> demandes = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "workflow_id",nullable = true)
	@JsonIgnore
	private WorkflowDefinition workflowDefinition;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomTypeStage() {
		return nomTypeStage;
	}

	public void setNomTypeStage(String nomTypeStage) {
		this.nomTypeStage = nomTypeStage;
	}

	public String getDureeStage() {
		return DureeStage;
	}

	public void setDureeStage(String dureeStage) {
		DureeStage = dureeStage;
	}


	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}

	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}
}


/* kol type aando workflow mtaa validation 
 o kol type yefra9 ala lekhor  */
 