package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class FicheEvalTechResponseDTO {
    private Long id;
    private StatutDTO statut;
    private List<ExigenceDTO> exigences;
    private double scoreTotalTech;

    
    
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

	public List<ExigenceDTO> getExigences() {
		return exigences;
	}

	public void setExigences(List<ExigenceDTO> exigences) {
		this.exigences = exigences;
	}

	public double getScoreTotalTech() {
		return scoreTotalTech;
	}

	public void setScoreTotalTech(double scoreTotalTech) {
		this.scoreTotalTech = scoreTotalTech;
	}

	@Getter @Setter
    public static class StatutDTO {
        private Long id;
        private String nomStatut;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNomStatut() {
			return nomStatut;
		}
		public void setNomStatut(String nomStatut) {
			this.nomStatut = nomStatut;
		}
        
        
        
    }

    @Getter @Setter
    public static class ExigenceDTO {
        private Long id;
        private String nomExigence;
        private double scoreCompetenceTech;
		public Long getId() {
			return id;
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
		public double getScoreCompetenceTech() {
			return scoreCompetenceTech;
		}
		public void setScoreCompetenceTech(double scoreCompetenceTech) {
			this.scoreCompetenceTech = scoreCompetenceTech;
		}
        
        
    }
}