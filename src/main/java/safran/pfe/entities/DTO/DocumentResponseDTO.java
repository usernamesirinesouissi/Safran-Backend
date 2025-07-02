package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;
import safran.pfe.entities.Document;

import java.util.Date;

@Getter
@Setter
public class DocumentResponseDTO {
    private Long id;
    private String nomFichier;
    private Date dateDepot;
    private TypeDocumentResponseDTO type;
    private CandidatureDetailsDTO candidature; // Changé pour inclure tous les détails
    

    public CandidatureDetailsDTO getCandidature() {
		return candidature;
	}


	public void setCandidature(CandidatureDetailsDTO candidature) {
		this.candidature = candidature;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNomFichier() {
		return nomFichier;
	}


	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}


	public Date getDateDepot() {
		return dateDepot;
	}


	public void setDateDepot(Date dateDepot) {
		this.dateDepot = dateDepot;
	}


	public TypeDocumentResponseDTO getType() {
		return type;
	}


	public void setType(TypeDocumentResponseDTO type) {
		this.type = type;
	}


	

	


	public DocumentResponseDTO(Document document) {
        this.id = document.getId();
        this.nomFichier = document.getNomFichier();
        this.dateDepot = document.getDateDepot();

        // Type document
        if (document.getType() != null) {
            TypeDocumentResponseDTO typeDTO = new TypeDocumentResponseDTO();
            typeDTO.setId(document.getType().getId());
            typeDTO.setNomTypeDoc(document.getType().getNomTypeDoc());
            this.type = typeDTO;
        }

        // Candidature avec détails
        if (document.getCandidature() != null) {
            this.candidature = new CandidatureDetailsDTO(document.getCandidature());
        }

       
    }
}