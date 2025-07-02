package safran.pfe.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDateTime;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workflow_instance")
public class WorkflowInstance {

	
	  @ManyToOne
	    @JoinColumn(name = "assigned_user_id")
	    private User assignedUser;
	
	
    public User getAssignedUser() {
		return assignedUser;
	}


	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}


	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
	}


	public String getDefinitionVersion() {
		return definitionVersion;
	}


	public void setDefinitionVersion(String definitionVersion) {
		this.definitionVersion = definitionVersion;
	}


	public WorkflowInstanceStatus getStatus() {
		return status;
	}


	public void setStatus(WorkflowInstanceStatus status) {
		this.status = status;
	}


	public LocalDateTime getStartedAt() {
		return startedAt;
	}


	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}


	public LocalDateTime getCompletedAt() {
		return completedAt;
	}


	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}


	public String getInitiatedBy() {
		return initiatedBy;
	}


	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}


	public List<NodeInstance> getNodeInstances() {
		return nodeInstances;
	}


	public void setNodeInstances(List<NodeInstance> nodeInstances) {
		this.nodeInstances = nodeInstances;
	}


	public Demande getDemande() {
		return demande;
	}


	public void setDemande(Demande demande) {
		this.demande = demande;
	}


	public Map<String, String> getVariables() {
		return variables;
	}


	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}


	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "workflow_definition_id", nullable = false)
    private WorkflowDefinition workflowDefinition;

    // Add this field to explicitly track the definition version used
    private String definitionVersion;

    @Enumerated(EnumType.STRING)
    private WorkflowInstanceStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private String initiatedBy;

    @OneToMany(mappedBy = "workflowInstance", cascade = CascadeType.ALL)
    private List<NodeInstance> nodeInstances = new ArrayList<>();

    @OneToOne(mappedBy = "workflowInstance", cascade = CascadeType.ALL,orphanRemoval = false)
    @JsonBackReference
    private Demande demande;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "workflow_instance_variables", joinColumns = @JoinColumn(name = "workflow_instance_id"))
    @MapKeyColumn(name = "variables_key")
    @Column(name = "var_value")
    private Map<String, String> variables = new HashMap<>();


    @PrePersist
    protected void onCreate() {

        if (this.workflowDefinition != null) {
            this.definitionVersion = this.workflowDefinition.getVersion();
        }
    }
}