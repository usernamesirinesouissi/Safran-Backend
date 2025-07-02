package safran.pfe.entities.DTO;

import lombok.Data;
import java.util.List;

@Data
public class FicheEvalTechDto {
    private Long statusId;       
    private List<Long> techCompetenceIds;    
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public List<Long> getTechCompetenceIds() {
		return techCompetenceIds;
	}
	public void setTechCompetenceIds(List<Long> techCompetenceIds) {
		this.techCompetenceIds = techCompetenceIds;
	}
}