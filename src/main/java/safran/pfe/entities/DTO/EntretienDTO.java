package safran.pfe.entities.DTO;

import java.util.Date;

public class EntretienDTO {

        private Date dateHeure;
        private int dureeEnMinutes;
        private String lieu;
        private String commentaires;
        private Long candidatureId;

    public EntretienDTO(Date dateHeure, int dureeEnMinutes, String lieu, String commentaires, Long candidatureId) {
        this.dateHeure = dateHeure;
        this.dureeEnMinutes = dureeEnMinutes;
        this.lieu = lieu;
        this.commentaires = commentaires;
        this.candidatureId = candidatureId;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getDureeEnMinutes() {
        return dureeEnMinutes;
    }

    public void setDureeEnMinutes(int dureeEnMinutes) {
        this.dureeEnMinutes = dureeEnMinutes;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Long getCandidatureId() {
        return candidatureId;
    }

    public void setCandidatureId(Long candidatureId) {
        this.candidatureId = candidatureId;
    }
}


