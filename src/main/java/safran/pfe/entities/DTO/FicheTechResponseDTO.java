package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.FicheEvalTech;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class FicheTechResponseDTO {
    private Long id;
    private StatutDTO statut;
    private Double scoreTotalTech;
    private List<FicheTechCompetenceDTO> competences; 
    
    // Notez que la prop s'appelle 'competences' dans FicheEvalTech entity

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatutDTO getStatut() {
		return statut;
	}

	public void setStatut(StatutDTO statut) {
		this.statut = statut;
	}

	public Double getScoreTotalTech() {
		return scoreTotalTech;
	}

	public void setScoreTotalTech(Double scoreTotalTech) {
		this.scoreTotalTech = scoreTotalTech;
	}

	public List<FicheTechCompetenceDTO> getCompetences() {
		return competences;
	}

	public void setCompetences(List<FicheTechCompetenceDTO> competences) {
		this.competences = competences;
	}

	public static FicheTechResponseDTO fromEntity(FicheEvalTech entity) {
        if (entity == null) return null;
        FicheTechResponseDTO dto = new FicheTechResponseDTO();
        dto.setId(entity.getId());
        dto.setStatut(StatutDTO.fromEntity(entity.getStatut()));
        dto.setScoreTotalTech(entity.getScoreTotalTech());
        if (entity.getCompetences() != null) {
            dto.setCompetences(
                entity.getCompetences().stream()
                      .map(FicheTechCompetenceDTO::fromEntity)
                      .collect(Collectors.toList())
            );
        }
        return dto;
    }
}