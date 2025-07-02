package safran.pfe.entities.DTO;

import java.time.LocalDateTime;

public class ValidationWithDemandeDTO  {

    private String validationId;
    private String status;
    private String assignedTo;
    private String assignedRole;
    private String decision;
    private String decisionBy;
    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;
    private String comments;

    private Long demandeId;
    private String intituleProjet;
    private String descriptionProjet;
    private String risquesProjet;
    private String objectifsProjet;
    private String descriptionLivrables;
    private String effortEstime;
    private int dureeEstime;
    private int nombreStagiaires;
    private String typeStage;
    private String statut;

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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

    public Long getDemandeId() {
        return demandeId;
    }

    public void setDemandeId(Long demandeId) {
        this.demandeId = demandeId;
    }

    public String getIntituleProjet() {
        return intituleProjet;
    }

    public void setIntituleProjet(String intituleProjet) {
        this.intituleProjet = intituleProjet;
    }

    public String getDescriptionProjet() {
        return descriptionProjet;
    }

    public void setDescriptionProjet(String descriptionProjet) {
        this.descriptionProjet = descriptionProjet;
    }

    public String getRisquesProjet() {
        return risquesProjet;
    }

    public void setRisquesProjet(String risquesProjet) {
        this.risquesProjet = risquesProjet;
    }

    public String getObjectifsProjet() {
        return objectifsProjet;
    }

    public void setObjectifsProjet(String objectifsProjet) {
        this.objectifsProjet = objectifsProjet;
    }

    public String getDescriptionLivrables() {
        return descriptionLivrables;
    }

    public void setDescriptionLivrables(String descriptionLivrables) {
        this.descriptionLivrables = descriptionLivrables;
    }

    public String getEffortEstime() {
        return effortEstime;
    }

    public void setEffortEstime(String effortEstime) {
        this.effortEstime = effortEstime;
    }

    public int getDureeEstime() {
        return dureeEstime;
    }

    public void setDureeEstime(int dureeEstime) {
        this.dureeEstime = dureeEstime;
    }

    public int getNombreStagiaires() {
        return nombreStagiaires;
    }

    public void setNombreStagiaires(int nombreStagiaires) {
        this.nombreStagiaires = nombreStagiaires;
    }

    public String getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(String typeStage) {
        this.typeStage = typeStage;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
