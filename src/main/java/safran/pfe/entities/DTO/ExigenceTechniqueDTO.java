package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.ExigenceTechnique;

@Data
@NoArgsConstructor
public class ExigenceTechniqueDTO {
    private Long id;
    private String nomExigence;
    // Ajoutez d'autres champs d'ExigenceTechnique si nécessaire pour l'affichage

    public Long getId() {
		return id;
	}

	public ExigenceTechniqueDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExigenceTechniqueDTO(Long id, String nomExigence) {
		super();
		this.id = id;
		this.nomExigence = nomExigence;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomExigence() {
		return nomExigence;
	}

	public void setNomExigence(String nomExigence) {
		this.nomExigence = nomExigence;
	}

	public static ExigenceTechniqueDTO fromEntity(ExigenceTechnique entity) {
        if (entity == null) return null;
        ExigenceTechniqueDTO dto = new ExigenceTechniqueDTO();
        dto.setId(entity.getId());
        dto.setNomExigence(entity.getNomExigence());
        return dto;
    }
}