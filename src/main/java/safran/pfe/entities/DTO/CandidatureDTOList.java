package safran.pfe.entities.DTO;

import java.time.LocalDate;

public class CandidatureDTOList {

    private Long id;
    private LocalDate dateDepot;
    private int score;
    private CandidatSummaryDTO candidat;
    private DemandeSummaryDTO demande;
    private StatutCandidatureDTO statutCandidature;

    // --- CONSTRUCTEUR PRINCIPAL ---
    // La requête JPQL utilisera ce constructeur. L'ordre est crucial.
    public CandidatureDTOList(Long id, LocalDate dateDepot, int score,
                              Long candidatId, String nomCandidat, String prenomCandidat, String photoPath,
                              Long demandeId, String reference, String intituleProjet, String nomTypeStage,
                              Long statutId, String nomStatut) {
        this.id = id;
        this.dateDepot = dateDepot;
        this.score = score;
        this.candidat = new CandidatSummaryDTO(candidatId, nomCandidat, prenomCandidat, photoPath);
        this.demande = new DemandeSummaryDTO(demandeId, reference, intituleProjet, nomTypeStage);
        this.statutCandidature = new StatutCandidatureDTO(statutId, nomStatut);
    }

    // --- GETTERS ET SETTERS POUR LE DTO PRINCIPAL ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDate dateDepot) { this.dateDepot = dateDepot; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public CandidatSummaryDTO getCandidat() { return candidat; }
    public void setCandidat(CandidatSummaryDTO candidat) { this.candidat = candidat; }
    public DemandeSummaryDTO getDemande() { return demande; }
    public void setDemande(DemandeSummaryDTO demande) { this.demande = demande; }
    public StatutCandidatureDTO getStatutCandidature() { return statutCandidature; }
    public void setStatutCandidature(StatutCandidatureDTO statutCandidature) { this.statutCandidature = statutCandidature; }


    // ====================================================================
    // --- CLASSES DTO INTERNES STATIQUES ---
    // ====================================================================

    public static class CandidatSummaryDTO {
        private Long id;
        private String nomCandidat;
        private String prenomCandidat;
        private String photoPath;

        public CandidatSummaryDTO(Long id, String nomCandidat, String prenomCandidat, String photoPath) {
            this.id = id;
            this.nomCandidat = nomCandidat;
            this.prenomCandidat = prenomCandidat;
            this.photoPath = photoPath;
        }
        // Getters et Setters pour CandidatSummaryDTO
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNomCandidat() { return nomCandidat; }
        public void setNomCandidat(String nomCandidat) { this.nomCandidat = nomCandidat; }
        public String getPrenomCandidat() { return prenomCandidat; }
        public void setPrenomCandidat(String prenomCandidat) { this.prenomCandidat = prenomCandidat; }
        public String getPhotoPath() { return photoPath; }
        public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    }

    public static class DemandeSummaryDTO {
        private Long id;
        private String reference;
        private String intituleProjet;
        private String nomTypeStage;

        public DemandeSummaryDTO(Long id, String reference, String intituleProjet, String nomTypeStage) {
            this.id = id;
            this.reference = reference;
            this.intituleProjet = intituleProjet;
            this.nomTypeStage = nomTypeStage;
        }
        // Getters et Setters pour DemandeSummaryDTO
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getIntituleProjet() { return intituleProjet; }
        public void setIntituleProjet(String intituleProjet) { this.intituleProjet = intituleProjet; }
        public String getNomTypeStage() { return nomTypeStage; }
        public void setNomTypeStage(String nomTypeStage) { this.nomTypeStage = nomTypeStage; }
    }

    public static class StatutCandidatureDTO {
        private Long id;
        private String nom;

        public StatutCandidatureDTO(Long id, String nom) {
            this.id = id;
            this.nom = nom;
        }
        // Getters et Setters pour StatutCandidatureDTO
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
    }
}