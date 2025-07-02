package safran.pfe.entities.DTO;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class FicheEvalRhResponseDTO {
    private Long id;
    private StatutDTO statut;
    private List<CompetenceDTO> competences;
    private double scoreTotalRh;

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

	public List<CompetenceDTO> getCompetences() {
		return competences;
	}

	public void setCompetences(List<CompetenceDTO> competences) {
		this.competences = competences;
	}

	public double getScoreTotalRh() {
		return scoreTotalRh;
	}

	public void setScoreTotalRh(double scoreTotalRh) {
		this.scoreTotalRh = scoreTotalRh;
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
    public static class CompetenceDTO {
        private Long id;
        private String nom;
        private double scoreCompetenceRh;
        
        
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNom() {
			return nom;
		}
		public void setNom(String nom) {
			this.nom = nom;
		}
		public double getScoreCompetenceRh() {
			return scoreCompetenceRh;
		}
		public void setScoreCompetenceRh(double scoreCompetenceRh) {
			this.scoreCompetenceRh = scoreCompetenceRh;
		}
        
        
    }
    
    
  
}

/*
import lombok.Data;
import java.util.List;

@Data
public class FicheEvalRhResponseDTO {
    private Long id;
    private StatutDTO statut;
    private List<CompetenceDTO> competences;

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

	public List<CompetenceDTO> getCompetences() {
		return competences;
	}

	public void setCompetences(List<CompetenceDTO> competences) {
		this.competences = competences;
	}

	@Data
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

    @Data
    public static class CompetenceDTO {
        private Long id;
        private String nomCompetence;
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
    }
}*/