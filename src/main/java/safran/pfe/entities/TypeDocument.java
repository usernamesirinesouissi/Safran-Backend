package safran.pfe.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TypeDocument {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(unique = true, nullable = false)
	    private String nomTypeDoc; 
	    
	    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonIgnore
	    private List<Document> documents = new ArrayList<>();
    
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

	

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	
    
    
    
}