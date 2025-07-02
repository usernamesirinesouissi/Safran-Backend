package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String intituleProjet;
    private String descriptionProjet;
    private String risquesProjet;
    private String objectifsProjet;
    private String descriptionLivrables;
    private String effortEstime;
    private int dureeEstime;
    private int nombreStagiaires;
    @Column(unique = true, nullable = true)
    private String reference;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int nombreStagiairesAffectes;



    @ManyToOne
    private TypeStage typeStage;

    @ManyToOne
    private Statut statut;
    
    @ManyToMany
    private List<CompetenceComportementale> competencescomportementale;

    @ManyToMany
    private List<ExigenceTechnique> competencestechnique;

    @ManyToMany
    private List<FormationAcademique> formationacademique;

    @ManyToMany
    private List<PropositionEcole> propecole;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "workflow_id")
    @JsonManagedReference
    private WorkflowInstance workflowInstance;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Candidature> candidatures;

    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public List<CompetenceComportementale> getCompetencescomportementale() {
        return competencescomportementale;
    }

    public void setCompetencescomportementale(List<CompetenceComportementale> competencescomportementale) {
        this.competencescomportementale = competencescomportementale;
    }

    public List<ExigenceTechnique> getCompetencestechnique() {
        return competencestechnique;
    }

    public void setCompetencestechnique(List<ExigenceTechnique> competencestechnique) {
        this.competencestechnique = competencestechnique;
    }

    public List<FormationAcademique> getFormationacademique() {
        return formationacademique;
    }

    public void setFormationacademique(List<FormationAcademique> formationacademique) {
        this.formationacademique = formationacademique;
    }

    public List<PropositionEcole> getPropecole() {
        return propecole;
    }

    public void setPropecole(List<PropositionEcole> propecole) {
        this.propecole = propecole;
    }

    public TypeStage getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(TypeStage typeStage) {
        this.typeStage = typeStage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
