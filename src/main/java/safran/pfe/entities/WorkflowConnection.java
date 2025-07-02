package safran.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workflow_connection")
public class WorkflowConnection {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

	private String name;

    @Column(nullable = false)
    private String sourceId;

    @Column(nullable = false)
    private String targetId;
    
    
    @ManyToOne
    @JoinColumn(name = "source_node_id")
    private WorkflowNode sourceNode;  // Nom correct du champ

    @ManyToOne
    @JoinColumn(name = "target_node_id")
    private WorkflowNode targetNode;  // N

    public WorkflowNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(WorkflowNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public WorkflowNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(WorkflowNode targetNode) {
		this.targetNode = targetNode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_definition_id")
    @JsonIgnore
    private WorkflowDefinition workflowDefinition;

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

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}

	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
	}



}
