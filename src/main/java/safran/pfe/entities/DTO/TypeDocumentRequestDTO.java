package safran.pfe.entities.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeDocumentRequestDTO  {

    private Long id;
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