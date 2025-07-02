package safran.pfe.entities.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank; // Assurez-vous d'avoir la dépendance spring-boot-starter-validation

@Getter
@Setter
public class TypeDocumentResponseDTO  {
    private Long id;

    @NotBlank(message = "Le nom du type de document ne peut pas être vide")
    private String nomTypeDoc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomTypeDoc() {
		return nomTypeDoc;
	}

	public void setNomTypeDoc(String nomTypeDoc) {
		this.nomTypeDoc = nomTypeDoc;
	}
    
    
    
}