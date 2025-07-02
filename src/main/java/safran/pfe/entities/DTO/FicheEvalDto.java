package safran.pfe.entities.DTO;

import lombok.Data;
import java.util.List;

@Data
public class FicheEvalDto {
    private Long statusId;       
    private List<Long> rhCompetenceIds;    
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public List<Long> getRhCompetenceIds() {
		return rhCompetenceIds;
	}
	public void setRhCompetenceIds(List<Long> rhCompetenceIds) {
		this.rhCompetenceIds = rhCompetenceIds;
	}
}