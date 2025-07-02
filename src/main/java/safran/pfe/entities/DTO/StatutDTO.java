package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.Statut;

@Data // Comprend @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor
public class StatutDTO {
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



	public static StatutDTO fromEntity(Statut statutEntity) {
        if (statutEntity == null) return null;
        StatutDTO dto = new StatutDTO();
        dto.setId(statutEntity.getId());
        dto.setNomStatut(statutEntity.getNomStatut());
        return dto;
    }
}