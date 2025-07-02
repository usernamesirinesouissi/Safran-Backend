package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.FicheRhCompetence;

@Data
@NoArgsConstructor
public class FicheRhCompetenceDTO {
    private Long id; // ID de l'entité FicheRhCompetence (la ligne de la table de jointure/évaluation)
    private CompetenceComportementaleDTO competence; // L'objet compétence lui-même
    private double scoreCompetenceRh;
    
    
    

    public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public CompetenceComportementaleDTO getCompetence() {
		return competence;
	}




	public void setCompetence(CompetenceComportementaleDTO competence) {
		this.competence = competence;
	}




	public double getScoreCompetenceRh() {
		return scoreCompetenceRh;
	}




	public void setScoreCompetenceRh(double scoreCompetenceRh) {
		this.scoreCompetenceRh = scoreCompetenceRh;
	}




	public static FicheRhCompetenceDTO fromEntity(FicheRhCompetence entity) {
        if (entity == null) return null;
        FicheRhCompetenceDTO dto = new FicheRhCompetenceDTO();
        dto.setId(entity.getId());
        dto.setScoreCompetenceRh(entity.getScoreCompetenceRh());
        if (entity.getCompetence() != null) {
            dto.setCompetence(CompetenceComportementaleDTO.fromEntity(entity.getCompetence()));
        }
        return dto;
    }
}