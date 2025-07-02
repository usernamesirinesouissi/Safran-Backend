package safran.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "node_validation")

@JsonIgnoreProperties(ignoreUnknown = true) 
public class NodeValidation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    @JsonIgnore
    private WorkflowNode node;

    @Embedded
    private ValidationParameter parameters;
    
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role assignedRole;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

  

    public Role getAssignedRole() {
		return assignedRole;
	}

	public void setAssignedRole(Role assignedRole) {
		this.assignedRole = assignedRole;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	@Enumerated(EnumType.STRING)
    private ValidationSeverity severity;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ValidationParameter getParameters() {
		return parameters;
	}

	public void setParameters(ValidationParameter parameters) {
		this.parameters = parameters;
	}



	public ValidationSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(ValidationSeverity severity) {
		this.severity = severity;
	}

	public WorkflowNode getNode() {
		return node;
	}

	public void setNode(WorkflowNode node) {
		this.node = node;
	}

	
	
	
	

}
