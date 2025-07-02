package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FicheEvalForEntretienCreateRequestDTO {
    @NotNull
    private Long entretienId;

	public Long getEntretienId() {
		return entretienId;
	}

	public void setEntretienId(Long entretienId) {
		this.entretienId = entretienId;
	}
    
    
}