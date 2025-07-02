package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.FicheEvalRh;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class FicheRhResponseDTO {
    private Long id;
    private StatutDTO statut;
    private Double scoreTotalRh;
    private List<FicheRhCompetenceDTO> competences;
    
    
    

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




	public Double getScoreTotalRh() {
		return scoreTotalRh;
	}




	public void setScoreTotalRh(Double scoreTotalRh) {
		this.scoreTotalRh = scoreTotalRh;
	}




	public List<FicheRhCompetenceDTO> getCompetences() {
		return competences;
	}




	public void setCompetences(List<FicheRhCompetenceDTO> competences) {
		this.competences = competences;
	}




	public static FicheRhResponseDTO fromEntity(FicheEvalRh entity) {
        if (entity == null) return null;
        FicheRhResponseDTO dto = new FicheRhResponseDTO();
        dto.setId(entity.getId());
        dto.setStatut(StatutDTO.fromEntity(entity.getStatut()));
        dto.setScoreTotalRh(entity.getScoreTotalRh());
        if (entity.getCompetences() != null) {
            dto.setCompetences(
                entity.getCompetences().stream()
                      .map(FicheRhCompetenceDTO::fromEntity)
                      .collect(Collectors.toList())
            );
        }
        return dto;
    }
}