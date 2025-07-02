package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentRequestDTO {
    private String nomFichier;
    private String cheminStockage;
    private Long typeId;
    private Long candidatureId; // Optionnel
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	public String getCheminStockage() {
		return cheminStockage;
	}
	public void setCheminStockage(String cheminStockage) {
		this.cheminStockage = cheminStockage;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getCandidatureId() {
		return candidatureId;
	}
	public void setCandidatureId(Long candidatureId) {
		this.candidatureId = candidatureId;
	}
	

   
}