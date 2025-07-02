package safran.pfe.entities.DTO;

import safran.pfe.entities.ValidationSeverity;

public class NodeValidationDTO {
    private String name;
    private ValidationSeverity severity;
    private Integer assignedRole;
    private Long assignedUser;
    
    public Integer getAssignedRole() {
		return assignedRole;
	}
	public void setAssignedRole(Integer assignedRole) {
		this.assignedRole = assignedRole;
	}
	public Long getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(Long assignedUser) {
		this.assignedUser = assignedUser;
	}
	private String description;          // Ajouté
    private boolean required;            // Ajouté
    private Integer timeoutMinutes;      // Ajouté
    private String formTemplate;         // Ajouté
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public Integer getTimeoutMinutes() {
		return timeoutMinutes;
	}
	public void setTimeoutMinutes(Integer timeoutMinutes) {
		this.timeoutMinutes = timeoutMinutes;
	}
	public String getFormTemplate() {
		return formTemplate;
	}
	public void setFormTemplate(String formTemplate) {
		this.formTemplate = formTemplate;
	}
	public String getValidationType() {
		return validationType;
	}
	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}
	private String validationType;    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ValidationSeverity getSeverity() {
		return severity;
	}
	public void setSeverity(ValidationSeverity severity) {
		this.severity = severity;
	}

    
    // Getters/Setters
}