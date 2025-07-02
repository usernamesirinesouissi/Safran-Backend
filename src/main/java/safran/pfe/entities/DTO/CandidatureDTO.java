package safran.pfe.entities.DTO;

import java.time.LocalDate;

public class CandidatureDTO {
    private Long id;
    private LocalDate dateDepot;
    private int score;
    private String demandeReference;
    private String demandeIntitule;
    private String statutCandidatureLabel;

    // Constructor
    public CandidatureDTO(Long id, LocalDate dateDepot, int score, String demandeReference, String demandeIntitule, String statutCandidatureLabel) {
        this.id = id;
        this.dateDepot = dateDepot;
        this.score = score;
        this.demandeReference = demandeReference;
        this.demandeIntitule = demandeIntitule;
        this.statutCandidatureLabel = statutCandidatureLabel;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDemandeReference() {
        return demandeReference;
    }

    public void setDemandeReference(String demandeReference) {
        this.demandeReference = demandeReference;
    }

    public String getDemandeIntitule() {
        return demandeIntitule;
    }

    public void setDemandeIntitule(String demandeIntitule) {
        this.demandeIntitule = demandeIntitule;
    }

    public String getStatutCandidatureLabel() {
        return statutCandidatureLabel;
    }

    public void setStatutCandidatureLabel(String statutCandidatureLabel) {
        this.statutCandidatureLabel = statutCandidatureLabel;
    }
}
