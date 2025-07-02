package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.FicheTechCompetence;

@Data
@NoArgsConstructor
public class FicheTechCompetenceDTO {
    private Long id; // ID de l'entité FicheTechCompetence
    private ExigenceTechniqueDTO exigenceTechnique; // L'objet exigence lui-même
    private double scoreCompetenceTech;

    
    
    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public ExigenceTechniqueDTO getExigenceTechnique() {
		return exigenceTechnique;
	}



	public void setExigenceTechnique(ExigenceTechniqueDTO exigenceTechnique) {
		this.exigenceTechnique = exigenceTechnique;
	}



	public double getScoreCompetenceTech() {
		return scoreCompetenceTech;
	}



	public void setScoreCompetenceTech(double scoreCompetenceTech) {
		this.scoreCompetenceTech = scoreCompetenceTech;
	}



	public static FicheTechCompetenceDTO fromEntity(FicheTechCompetence entity) {
        if (entity == null) return null;
        FicheTechCompetenceDTO dto = new FicheTechCompetenceDTO();
        dto.setId(entity.getId());
        dto.setScoreCompetenceTech(entity.getScoreCompetenceTech());
        if (entity.getExigenceTechnique() != null) {
            dto.setExigenceTechnique(ExigenceTechniqueDTO.fromEntity(entity.getExigenceTechnique()));
        }
        return dto;
    }
}