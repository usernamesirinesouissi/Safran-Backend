package safran.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "node_instance")
public class NodeInstance {

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

	public WorkflowInstance getWorkflowInstance() {
		return workflowInstance;
	}

	public void setWorkflowInstance(WorkflowInstance workflowInstance) {
		this.workflowInstance = workflowInstance;
	}

	public WorkflowNode getNode() {
		return node;
	}

	public void setNode(WorkflowNode node) {
		this.node = node;
	}

	public NodeInstanceStatus getStatus() {
		return status;
	}

	public void setStatus(NodeInstanceStatus status) {
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

	public List<ValidationInstance> getValidationInstances() {
		return validationInstances;
	}

	public void setValidationInstances(List<ValidationInstance> validationInstances) {
		this.validationInstances = validationInstances;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_instance_id", nullable = false)
    @JsonIgnore
    private WorkflowInstance workflowInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private WorkflowNode node;

    @Enumerated(EnumType.STRING)
    private NodeInstanceStatus status = NodeInstanceStatus.WAITING;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "nodeInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValidationInstance> validationInstances = new ArrayList<>();


}
