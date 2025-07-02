package safran.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "validation_instance")
public class ValidationInstance {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public NodeInstance getNodeInstance() {
		return nodeInstance;
	}

	public void setNodeInstance(NodeInstance nodeInstance) {
		this.nodeInstance = nodeInstance;
	}

	public NodeValidation getValidation() {
		return validation;
	}

	public void setValidation(NodeValidation validation) {
		this.validation = validation;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}

	public String getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(String assignedTo) {
		this.assignedUser = assignedTo;
	}

	public String getAssignedRole() {
		return assignedRole;
	}

	public void setAssignedRole(String assignedRole) {
		this.assignedRole = assignedRole;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getDecisionBy() {
		return decisionBy;
	}

	public void setDecisionBy(String decisionBy) {
		this.decisionBy = decisionBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getDecidedAt() {
		return decidedAt;
	}

	public void setDecidedAt(LocalDateTime decidedAt) {
		this.decidedAt = decidedAt;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_instance_id", nullable = false)
    @JsonIgnore
    private NodeInstance nodeInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_id")
    private NodeValidation validation;

    @Enumerated(EnumType.STRING)
    private ValidationStatus status = ValidationStatus.PENDING;

    @Column(name = "assigned_user")
    private String assignedUser; // ✅ Doit être String

    @Column(name = "assigned_role")
    private String assignedRole; 

    private String decision;

    private String decisionBy;

    private LocalDateTime createdAt;

    private LocalDateTime decidedAt;

    private String comments;
    
    
    @Transient
    public String getAssignedRoleLabel() {
        return this.assignedRole; // Déjà stocké en String
    }

    @Transient
    public String getAssignedUserName() {
        return (this.validation != null && this.validation.getAssignedUser() != null) 
            ? this.validation.getAssignedUser().getUsername() 
            : null;
    }

}
