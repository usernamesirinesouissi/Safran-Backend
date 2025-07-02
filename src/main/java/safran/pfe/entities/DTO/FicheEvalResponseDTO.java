// Fichier: src/main/java/safran/pfe/entities/DTO/FicheEvalResponseDTO.java
package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import safran.pfe.entities.FicheEval;
import safran.pfe.entities.FicheEvalRh;
import safran.pfe.entities.FicheEvalTech;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.Candidature;
import safran.pfe.entities.Statut;
import safran.pfe.entities.Entretien;

@Getter
@Setter
public class FicheEvalResponseDTO {
    private Long id;

    private Long candidatId;
    private String candidatNomComplet;
    private String candidatEmail;

    private StatutDTO statutGlobal;

    // Infos des fiches RH/Tech
    private StatutDTO statutRh;
    private StatutDTO statutTech;

    // Scores
    private Double scoreTotal;
    private Double scoreRh;
    private Double scoreTech;

    // IDs des fiches liées
    private Long ficheRhId;
    private Long ficheTechId;

    // Infos de l'entretien lié
    private Long entretienId;
    // Le champ 'entretienType' a été supprimé

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }
    public String getCandidatNomComplet() { return candidatNomComplet; }
    public void setCandidatNomComplet(String candidatNomComplet) { this.candidatNomComplet = candidatNomComplet; }
    public String getCandidatEmail() { return candidatEmail; }
    public void setCandidatEmail(String candidatEmail) { this.candidatEmail = candidatEmail; }
    public StatutDTO getStatutGlobal() { return statutGlobal; }
    public void setStatutGlobal(StatutDTO statutGlobal) { this.statutGlobal = statutGlobal; }
    public StatutDTO getStatutRh() { return statutRh; }
    public void setStatutRh(StatutDTO statutRh) { this.statutRh = statutRh; }
    public StatutDTO getStatutTech() { return statutTech; }
    public void setStatutTech(StatutDTO statutTech) { this.statutTech = statutTech; }
    public Double getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(Double scoreTotal) { this.scoreTotal = scoreTotal; }
    public Double getScoreRh() { return scoreRh; }
    public void setScoreRh(Double scoreRh) { this.scoreRh = scoreRh; }
    public Double getScoreTech() { return scoreTech; }
    public void setScoreTech(Double scoreTech) { this.scoreTech = scoreTech; }
    public Long getFicheRhId() { return ficheRhId; }
    public void setFicheRhId(Long ficheRhId) { this.ficheRhId = ficheRhId; }
    public Long getFicheTechId() { return ficheTechId; }
    public void setFicheTechId(Long ficheTechId) { this.ficheTechId = ficheTechId; }
    public Long getEntretienId() { return entretienId; }
    public void setEntretienId(Long entretienId) { this.entretienId = entretienId; }
    // Getter et Setter pour 'entretienType' sont supprimés

    @Getter
    @Setter
    public static class StatutDTO {
        private Long id;
        private String nomStatut;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNomStatut() { return nomStatut; }
        public void setNomStatut(String nomStatut) { this.nomStatut = nomStatut; }

        public static StatutDTO fromEntity(Statut statutEntity) {
            if (statutEntity == null) return null;
            StatutDTO dto = new StatutDTO();
            dto.setId(statutEntity.getId());
            dto.setNomStatut(statutEntity.getNomStatut());
            return dto;
        }
    }

    public static FicheEvalResponseDTO fromEntity(FicheEval entity) {
        if (entity == null) {
            return null;
        }

        FicheEvalResponseDTO dto = new FicheEvalResponseDTO();
        dto.setId(entity.getId());
        dto.setScoreTotal(entity.getScoreTotal());

        if (entity.getStatut() != null) {
            dto.setStatutGlobal(StatutDTO.fromEntity(entity.getStatut()));
        }

        FicheEvalRh ficheRhEntity = entity.getFicheRh();
        if (ficheRhEntity != null) {
            dto.setFicheRhId(ficheRhEntity.getId());
            dto.setScoreRh(ficheRhEntity.getScoreTotalRh());
            if (ficheRhEntity.getStatut() != null) {
                dto.setStatutRh(StatutDTO.fromEntity(ficheRhEntity.getStatut()));
            }
        }

        FicheEvalTech ficheTechEntity = entity.getFicheTech();
        if (ficheTechEntity != null) {
            dto.setFicheTechId(ficheTechEntity.getId());
            dto.setScoreTech(ficheTechEntity.getScoreTotalTech());
            if (ficheTechEntity.getStatut() != null) {
                dto.setStatutTech(StatutDTO.fromEntity(ficheTechEntity.getStatut()));
            }
        }

        Entretien entretienEntity = entity.getEntretienSpecifique();
        if (entretienEntity != null) {
            dto.setEntretienId(entretienEntity.getId());
            // La ligne pour entretienType a été supprimée ici aussi

            Candidature candidatureDeLEntretien = entretienEntity.getCandidature(); // Entretien DOIT avoir getCandidature()
            if (candidatureDeLEntretien != null) {
                Candidat candidat = candidatureDeLEntretien.getCandidat(); // Candidature DOIT avoir getCandidat()
                if (candidat != null) {
                    dto.setCandidatId(candidat.getId());
                    String nomComplet = "";
                    if (candidat.getPrenomCandidat() != null) {
                        nomComplet += candidat.getPrenomCandidat();
                    }
                    if (candidat.getNomCandidat() != null) {
                        if (!nomComplet.isEmpty()) {
                            nomComplet += " ";
                        }
                        nomComplet += candidat.getNomCandidat();
                    }
                    dto.setCandidatNomComplet(nomComplet);
                    dto.setCandidatEmail(candidat.getEmail());
                }
            }
        }
        return dto;
    }
}