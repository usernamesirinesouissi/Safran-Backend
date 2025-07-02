package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.CompetenceComportementale;

@Data
@NoArgsConstructor
public class CompetenceComportementaleDTO {
    private Long id;
    private String nomCompetence;
    
    
    
    
    public CompetenceComportementaleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompetenceComportementaleDTO(Long id, String nomCompetence) {
        this.id = id;
        this.nomCompetence = nomCompetence;
    }

    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNomCompetence() {
		return nomCompetence;
	}



	public void setNomCompetence(String nomCompetence) {
		this.nomCompetence = nomCompetence;
	}



	public static CompetenceComportementaleDTO fromEntity(CompetenceComportementale entity) {
        if (entity == null) return null;
        CompetenceComportementaleDTO dto = new CompetenceComportementaleDTO();
        dto.setId(entity.getId());
        dto.setNomCompetence(entity.getNomCompetence());
        return dto;
    }
}