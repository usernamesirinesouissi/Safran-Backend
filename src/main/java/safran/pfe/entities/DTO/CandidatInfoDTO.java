package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.Candidat;

@Data
@NoArgsConstructor
public class CandidatInfoDTO {
    private Long id;
    private String nomComplet;
    private String email;
    
    
    

    public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getNomComplet() {
		return nomComplet;
	}




	public void setNomComplet(String nomComplet) {
		this.nomComplet = nomComplet;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public static CandidatInfoDTO fromEntity(Candidat entity) {
        if (entity == null) return null;
        CandidatInfoDTO dto = new CandidatInfoDTO();
        dto.setId(entity.getId());
        String prenom = entity.getPrenomCandidat() != null ? entity.getPrenomCandidat() : "";
        String nom = entity.getNomCandidat() != null ? entity.getNomCandidat() : "";
        dto.setNomComplet((prenom + " " + nom).trim());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}