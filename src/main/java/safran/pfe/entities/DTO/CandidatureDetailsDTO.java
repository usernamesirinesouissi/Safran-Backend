package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import safran.pfe.entities.Candidature;
import safran.pfe.entities.StatutCandidature;
import safran.pfe.entities.Demande;
import safran.pfe.entities.Candidat;

import java.time.LocalDate;

@Getter
@Setter
public class CandidatureDetailsDTO {
    private Long id;
    private LocalDate dateDepot;
    private int score;
    private CandidatMinimalDTO candidat;
    
    

    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public LocalDate getDateDepot() {
		return dateDepot;
	}



	public void setDateDepot(LocalDate dateDepot) {
		this.dateDepot = dateDepot;
	}



	public int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}



	public CandidatMinimalDTO getCandidat() {
		return candidat;
	}



	public void setCandidat(CandidatMinimalDTO candidat) {
		this.candidat = candidat;
	}



	public CandidatureDetailsDTO(Candidature candidature) {
        this.id = candidature.getId();
        this.dateDepot = candidature.getDateDepot();
        this.score = candidature.getScore();
        
     
     
        
        // Détails du candidat
        if (candidature.getCandidat() != null) {
            this.candidat = new CandidatMinimalDTO(candidature.getCandidat());
        }
    }
}


@Getter
@Setter
class CandidatMinimalDTO {
    private Long id;
    private String nomCandidat;
    private String prenomCandidat;
    private String email;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomCandidat() {
		return nomCandidat;
	}

	public void setNomCandidat(String nomCandidat) {
		this.nomCandidat = nomCandidat;
	}

	public String getPrenomCandidat() {
		return prenomCandidat;
	}

	public void setPrenomCandidat(String prenomCandidat) {
		this.prenomCandidat = prenomCandidat;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CandidatMinimalDTO(Candidat candidat) {
        this.id = candidat.getId();
        this.nomCandidat = candidat.getNomCandidat();
        this.prenomCandidat = candidat.getPrenomCandidat();
        this.email = candidat.getEmail();
    }
}