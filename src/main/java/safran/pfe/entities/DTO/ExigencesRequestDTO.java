package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class ExigencesRequestDTO {
    private List<ExigenceScoreDTO> exigences;

    public List<ExigenceScoreDTO> getExigences() {
		return exigences;
	}

	public void setExigences(List<ExigenceScoreDTO> exigences) {
		this.exigences = exigences;
	}




	@Getter @Setter
    public static class ExigenceScoreDTO {
        private Long exigenceId;
        private double score;
        
        
		public Long getExigenceId() {
			return exigenceId;
		}
		public void setExigenceId(Long exigenceId) {
			this.exigenceId = exigenceId;
		}
		public double getScore() {
			return score;
		}
		public void setScore(double score) {
			this.score = score;
		}
        
        
        
    }
}