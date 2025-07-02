package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class CompetencesRequestDTO {
 private List<CompetenceScoreDTO> competences;

 public List<CompetenceScoreDTO> getCompetences() {
	return competences;
}

public void setCompetences(List<CompetenceScoreDTO> competences) {
	this.competences = competences;
}

@Getter @Setter
 public static class CompetenceScoreDTO {
     private Long competenceId;
     private double score;
	public Long getCompetenceId() {
		return competenceId;
	}
	public void setCompetenceId(Long competenceId) {
		this.competenceId = competenceId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
     
 }
}